/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.plugin.debugger.evaluate

import com.intellij.psi.PsiFile
import org.jetbrains.jet.plugin.refactoring.extractFunction.AnalysisResult
import org.jetbrains.jet.plugin.refactoring.extractFunction.AnalysisResult.ErrorMessage
import org.jetbrains.jet.plugin.codeInsight.CodeInsightUtils
import org.jetbrains.jet.plugin.refactoring.createTempCopy
import org.jetbrains.jet.lang.psi.codeFragmentUtil.skipVisibilityCheck
import com.intellij.psi.PsiElement
import org.jetbrains.jet.plugin.refactoring.extractFunction.ExtractionData
import java.util.Collections
import org.jetbrains.jet.plugin.refactoring.extractFunction.performAnalysis
import org.jetbrains.jet.plugin.refactoring.extractFunction.AnalysisResult.Status
import com.intellij.debugger.engine.evaluation.EvaluateExceptionUtil
import org.jetbrains.jet.plugin.refactoring.extractFunction.validate
import org.jetbrains.jet.plugin.refactoring.extractFunction.ExtractionOptions
import org.jetbrains.jet.plugin.refactoring.runReadAction
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiModificationTrackerImpl
import org.jetbrains.jet.lang.psi.*
import org.jetbrains.jet.plugin.intentions.InsertExplicitTypeArguments
import org.jetbrains.jet.plugin.refactoring.extractFunction.ExtractionGeneratorOptions
import org.jetbrains.jet.plugin.refactoring.extractFunction.generateDeclaration
import org.jetbrains.jet.plugin.actions.internal.KotlinInternalMode
import org.jetbrains.jet.lang.psi.psiUtil.replaced

fun getFunctionForExtractedFragment(
        codeFragment: JetCodeFragment,
        breakpointFile: PsiFile,
        breakpointLine: Int
): JetNamedFunction? {

    fun getErrorMessageForExtractFunctionResult(analysisResult: AnalysisResult): String {
        if (KotlinInternalMode.enabled) {
            logger.error("Couldn't extract function for debugger:\n" +
                                 "FILE NAME: ${breakpointFile.getName()}\n" +
                                 "BREAKPOINT LINE: ${breakpointLine}\n" +
                                 "CODE FRAGMENT:\n${codeFragment.getText()}\n" +
                                 "ERRORS:\n${analysisResult.messages.map { "$it: ${it.renderMessage()}" }.joinToString("\n")}\n" +
                                 "FILE TEXT: \n${breakpointFile.getText()}\n")
        }
        return analysisResult.messages.map { errorMessage ->
            val message = when(errorMessage) {
                ErrorMessage.NO_EXPRESSION -> "Cannot perform an action without an expression"
                ErrorMessage.NO_CONTAINER -> "Cannot perform an action at this breakpoint ${breakpointFile.getName()}:${breakpointLine}"
                ErrorMessage.SUPER_CALL -> "Cannot perform an action for expression with super call"
                ErrorMessage.DENOTABLE_TYPES -> "Cannot perform an action because following types are unavailable from debugger scope"
                ErrorMessage.ERROR_TYPES -> "Cannot perform an action because this code fragment contains erroneous types"
                ErrorMessage.DECLARATIONS_OUT_OF_SCOPE,
                ErrorMessage.OUTPUT_AND_EXIT_POINT,
                ErrorMessage.MULTIPLE_EXIT_POINTS,
                ErrorMessage.DECLARATIONS_ARE_USED_OUTSIDE -> "Cannot perform an action for this expression"
                else -> throw AssertionError("Unexpected error: $errorMessage")
            }
            errorMessage.additionalInfo?.let { "$message: ${it.joinToString(", ")}" } ?: message
        }.joinToString(", ")
    }

    fun generateFunction(): JetNamedFunction? {
        checkForSyntacticErrors(codeFragment)

        val originalFile = breakpointFile as JetFile

        val tmpFile = originalFile.createTempCopy { it }
        tmpFile.skipVisibilityCheck = true

        val contextElement = getExpressionToAddDebugExpressionBefore(tmpFile, codeFragment.getContext(), breakpointLine)
        if (contextElement == null) return null

        // Don't evaluate smth when breakpoint is on package directive (ex. for package classes)
        if (contextElement is JetFile) {
            throw EvaluateExceptionUtil.createEvaluateException("Cannot perform an action at this breakpoint ${breakpointFile.getName()}:${breakpointLine}")
        }

        addImportsToFile(codeFragment.importsAsImportList(), tmpFile)

        val newDebugExpression = addDebugExpressionBeforeContextElement(codeFragment, contextElement)
        if (newDebugExpression == null) return null

        val targetSibling = tmpFile.getDeclarations().firstOrNull()
        if (targetSibling == null) return null

        val analysisResult = ExtractionData(
                tmpFile, Collections.singletonList(newDebugExpression), targetSibling, ExtractionOptions(false, true)
        ).performAnalysis()
        if (analysisResult.status != Status.SUCCESS) {
            throw EvaluateExceptionUtil.createEvaluateException(getErrorMessageForExtractFunctionResult(analysisResult))
        }

        val validationResult = analysisResult.descriptor!!.validate()
        if (!validationResult.conflicts.isEmpty()) {
            throw EvaluateExceptionUtil.createEvaluateException("Following declarations are unavailable in debug scope: ${validationResult.conflicts.keySet()?.map { it.getText() }?.makeString(",")}")
        }

        return validationResult.descriptor
                .generateDeclaration(ExtractionGeneratorOptions(inTempFile = true))
                .declaration as JetNamedFunction
    }

    return runReadAction { generateFunction() }
}

private fun addImportsToFile(newImportList: JetImportList?, tmpFile: JetFile) {
    if (newImportList != null) {
        val tmpFileImportList = tmpFile.getImportList()
        val packageDirective = tmpFile.getPackageDirective()
        val psiFactory = JetPsiFactory(tmpFile)
        if (tmpFileImportList == null) {
            tmpFile.addAfter(psiFactory.createNewLine(), packageDirective)
            tmpFile.addAfter(newImportList, tmpFile.getPackageDirective())
        }
        else {
            tmpFileImportList.replace(newImportList)
        }
        tmpFile.addAfter(psiFactory.createNewLine(), packageDirective)
    }
}

private fun JetFile.getElementInCopy(e: PsiElement): PsiElement? {
    val offset = e.getTextRange()?.getStartOffset()
    if (offset == null) {
        return null
    }
    var elementAt = this.findElementAt(offset)
    while (elementAt == null ||
           elementAt!!.getText() != e.getText() ||
           elementAt!!.getTextRange()?.getEndOffset() != e.getTextRange()?.getEndOffset()) {
        elementAt = elementAt?.getParent()
    }
    return elementAt
}

private fun getExpressionToAddDebugExpressionBefore(tmpFile: JetFile, contextElement: PsiElement?, line: Int): PsiElement? {
    fun shouldStop(el: PsiElement?, p: PsiElement?) = p is JetBlockExpression || el is JetDeclaration

    if (contextElement == null) {
        val lineStart = CodeInsightUtils.getStartLineOffset(tmpFile, line)
        if (lineStart == null) return null

        val elementAtOffset = tmpFile.findElementAt(lineStart)
        if (elementAtOffset == null) return null

        return CodeInsightUtils.getTopmostElementAtOffset(elementAtOffset, lineStart) ?: elementAtOffset
    }

    var elementAt = tmpFile.getElementInCopy(contextElement)

    var parent = elementAt?.getParent()
    if (shouldStop(elementAt, parent)) {
        return elementAt
    }

    var parentOfParent = parent?.getParent()

    while (parent != null && parentOfParent != null) {
        if (shouldStop(parent, parentOfParent)) {
            break
        }

        parent = parent?.getParent()
        parentOfParent = parent?.getParent()
    }

    return parent
}

private fun addDebugExpressionBeforeContextElement(codeFragment: JetCodeFragment, contextElement: PsiElement): JetExpression? {
    val psiFactory = JetPsiFactory(codeFragment)

    val elementBefore = when {
        contextElement is JetProperty && !contextElement.isLocal() -> {
            wrapInRunFun(contextElement.getDelegateExpressionOrInitializer()!!)
        }
        contextElement is JetFunctionLiteral -> {
            val block = contextElement.getBodyExpression()!!
            block.getStatements().first ?: block.getLastChild()
        }
        contextElement is JetDeclarationWithBody && !contextElement.hasBlockBody()-> {
            wrapInRunFun(contextElement.getBodyExpression()!!)
        }
        contextElement is JetDeclarationWithBody && contextElement.hasBlockBody()-> {
            val block = contextElement.getBodyExpression() as JetBlockExpression
            block.getStatements().first ?: block.getLastChild()
        }
        contextElement is JetWhenEntry -> {
            val entryExpression = contextElement.getExpression()
            if (entryExpression is JetBlockExpression) {
                entryExpression.getStatements().first ?: entryExpression.getLastChild()
            }
            else {
                wrapInRunFun(entryExpression!!)
            }
        }
        else -> {
            contextElement
        }
    }

    val parent = elementBefore?.getParent()
    if (parent == null || elementBefore == null) return null

    parent.addBefore(psiFactory.createNewLine(), elementBefore)

    val debugExpression = codeFragment.getContentElement()
    if (debugExpression == null) return null

    val newDebugExpression = parent.addBefore(debugExpression, elementBefore)
    if (newDebugExpression == null) return null

    parent.addBefore(psiFactory.createNewLine(), elementBefore)

    return newDebugExpression as JetExpression
}

private fun replaceByRunFunction(expression: JetExpression): JetCallExpression {
    val callExpression = JetPsiFactory(expression).createExpression("run { \n${expression.getText()} \n}") as JetCallExpression
    val replaced = expression.replaced(callExpression)
    val typeArguments = InsertExplicitTypeArguments.createTypeArguments(replaced)
    if (typeArguments?.getArguments()?.isNotEmpty() ?: false) {
        val calleeExpression = replaced.getCalleeExpression()
        replaced.addAfter(typeArguments!!, calleeExpression)
    }
    return replaced
}

private fun wrapInRunFun(expression: JetExpression): PsiElement? {
    val replacedBody = replaceByRunFunction(expression)

    // Increment modification tracker to clear ResolveCache after changes in function body
    (PsiManager.getInstance(expression.getProject()).getModificationTracker() as PsiModificationTrackerImpl).incCounter()

    return replacedBody.getFunctionLiteralArguments().first().getFunctionLiteral().getBodyExpression()?.getFirstChild()
}

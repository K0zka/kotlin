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

package org.jetbrains.jet.asJava.light

import com.intellij.psi.*
import org.jetbrains.jet.lang.psi.JetEnumEntry

class KotlinLightEnumConstant(
        manager: PsiManager,
        origin: JetEnumEntry,
        enumConstant: PsiEnumConstant,
        containingClass: PsiClass
) : KotlinLightField<JetEnumEntry, PsiEnumConstant>(manager, origin, enumConstant, containingClass), PsiEnumConstant {
    override fun copy() = KotlinLightEnumConstant(getManager()!!, getOrigin(), getDelegate(), getContainingClass()!!)

    // NOTE: we don't use "delegation by" because the compiler would generate method calls to ALL of PsiEnumConstant members,
    // but we need only members whose implementations are not present in KotlinLightField
    override fun getArgumentList(): PsiExpressionList? = getDelegate().getArgumentList()
    override fun getInitializingClass(): PsiEnumConstantInitializer? = getDelegate().getInitializingClass()
    override fun getOrCreateInitializingClass(): PsiEnumConstantInitializer = getDelegate().getOrCreateInitializingClass()
    override fun resolveConstructor(): PsiMethod? = getDelegate().resolveConstructor()
    override fun resolveMethod(): PsiMethod? = getDelegate().resolveMethod()
    override fun resolveMethodGenerics(): JavaResolveResult = getDelegate().resolveMethodGenerics()
}

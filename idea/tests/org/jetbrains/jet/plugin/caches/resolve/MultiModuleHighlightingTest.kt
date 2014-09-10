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

package org.jetbrains.jet.plugin.caches.resolve

import org.jetbrains.jet.plugin.PluginTestCaseBase
import com.intellij.openapi.module.StdModuleTypes
import com.intellij.openapi.roots.ModuleRootModificationUtil
import org.jetbrains.jet.plugin.project.PluginJetFilesProvider
import com.intellij.codeInsight.daemon.DaemonAnalyzerTestCase
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.PsiTestUtil
import java.io.File
import com.intellij.openapi.util.io.FileUtil

class MultiModuleHighlightingTest : DaemonAnalyzerTestCase() {

    private val TEST_DATA_PATH = PluginTestCaseBase.getTestDataPathBase() + "/multiModuleHighlighting/"

    fun testVisibility() {
        val module1 = module("m1")
        val module2 = module("m2")

        module2.addDependency(module1)

        checkHighlightingInAllFiles()
    }

    fun testDependency() {
        val module1 = module("m1")
        val module2 = module("m2")
        val module3 = module("m3")
        val module4 = module("m4")

        module2.addDependency(module1)

        module1.addDependency(module2)

        module3.addDependency(module2)

        module4.addDependency(module1)
        module4.addDependency(module2)
        module4.addDependency(module3)

        checkHighlightingInAllFiles()
    }

    fun testTestRoot() {
        val module1 = module("m1", hasTestRoot = true)

        checkHighlightingInAllFiles()
    }

    private fun checkHighlightingInAllFiles() {
        PluginJetFilesProvider.allFilesInProject(myProject!!).forEach { file ->
            configureByExistingFile(file.getVirtualFile()!!)
            checkHighlighting(myEditor, true, false)
        }
    }

    private fun module(name: String, hasTestRoot: Boolean = false): Module {
        val srcDir = TEST_DATA_PATH + "${getTestName(true)}/$name"
        val moduleWithSrcRootSet = createModuleFromTestData(srcDir, "$name", StdModuleTypes.JAVA, true)!!
        if (hasTestRoot) {
            setTestRoot(moduleWithSrcRootSet, name)
        }
        return moduleWithSrcRootSet
    }

    private fun setTestRoot(module: Module, name: String) {
        val testDir = TEST_DATA_PATH + "${getTestName(true)}/${name}Test"
        val testRootDirInTestData = File(testDir)
        val testRootDir = createTempDirectory()!!
        FileUtil.copyDir(testRootDirInTestData, testRootDir)
        val testRoot = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(testRootDir)!!
        object : WriteCommandAction.Simple<Unit>(getProject()) {
            override fun run() {
                testRoot.refresh(false, true)
            }
        }.execute().throwException()
        PsiTestUtil.addSourceRoot(module, testRoot, true)
    }

    private fun Module.addDependency(other: Module) = ModuleRootModificationUtil.addDependency(this, other)
}
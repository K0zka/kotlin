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

package org.jetbrains.jet.lang.resolve.lazy

import com.intellij.openapi.project.Project
import org.jetbrains.jet.lang.psi.JetFile
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.jet.context.GlobalContext
import org.jetbrains.jet.lang.resolve.java.new.JvmAnalyzerFacade
import org.jetbrains.jet.analyzer.new.ModuleInfo
import org.jetbrains.jet.lang.resolve.name.Name
import org.jetbrains.jet.lang.resolve.java.new.JvmPlatformParameters

public fun createResolveSessionForFiles(
        project: Project,
        syntheticFiles: Collection<JetFile>,
        addBuiltIns: Boolean
): ResolveSession {
    val globalContext = GlobalContext()
    val testModule = TestModule(addBuiltIns)
    val resolverForProject = JvmAnalyzerFacade.setupResolverForProject(globalContext, project, listOf(testModule)) {
        JvmPlatformParameters(syntheticFiles, GlobalSearchScope.allScope(project)) { testModule }
    }
    val moduleDescriptor = resolverForProject.descriptorByModule[testModule]
    return resolverForProject.analyzerByModuleDescriptor[moduleDescriptor]!!.lazyResolveSession
}

private class TestModule(val dependsOnBuiltins: Boolean) : ModuleInfo<TestModule> {
    override val name: Name = Name.special("<Test module for lazy resolve>")
    override fun dependencies() = listOf(this)
    override fun dependencyOnBuiltins() =
            if (dependsOnBuiltins)
                ModuleInfo.DependenciesOnBuiltins.LAST
            else
                ModuleInfo.DependenciesOnBuiltins.NONE
}

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

import org.jetbrains.jet.analyzer.ModuleInfo
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.OrderEntry
import com.intellij.openapi.roots.ModuleSourceOrderEntry
import com.intellij.openapi.roots.ModuleOrderEntry
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.JdkOrderEntry
import com.intellij.openapi.module.Module
import org.jetbrains.jet.lang.resolve.name.Name
import java.util.LinkedHashSet
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.module.impl.scopes.LibraryScopeBase
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.jet.utils.emptyOrSingletonList
import com.intellij.openapi.roots.OrderEnumerator

public abstract class IdeaModuleInfo : ModuleInfo {
    abstract fun contentScope(): GlobalSearchScope
}

private fun orderEntryToModuleInfo(project: Project, orderEntry: OrderEntry, productionOnly: Boolean): List<IdeaModuleInfo> {
    fun Module.toInfos() = if (productionOnly) listOf(productionSourceInfo()) else listOf(testSourceInfo(), productionSourceInfo())

    return when (orderEntry) {
        is ModuleSourceOrderEntry -> {
            orderEntry.getOwnerModule().toInfos()
        }
        is ModuleOrderEntry -> {
            orderEntry.getModule()?.toInfos().orEmpty()
        }
        is LibraryOrderEntry -> {
            val library = orderEntry.getLibrary() ?: return listOf()
            emptyOrSingletonList(LibraryInfo(project, library))
        }
        is JdkOrderEntry -> {
            val sdk = orderEntry.getJdk() ?: return listOf()
            emptyOrSingletonList(SdkInfo(project, sdk))
        }
        else -> {
            throw IllegalStateException("Unexpected order entry $orderEntry")
        }
    }
}

fun ideaModelDependencies(module: Module, productionOnly: Boolean): List<IdeaModuleInfo> {
    //NOTE: lib dependencies can be processed several times during recursive traversal
    val result = LinkedHashSet<IdeaModuleInfo>()
    val dependencyEnumerator = ModuleRootManager.getInstance(module).orderEntries().compileOnly().recursively().exportedOnly()
    if (productionOnly) {
        dependencyEnumerator.productionOnly()
    }
    dependencyEnumerator.forEach {
        orderEntry ->
        result.addAll(orderEntryToModuleInfo(module.getProject(), orderEntry!!, productionOnly))
        true
    }
    return result.toList()
}

public data class ModuleProductionSourceInfo(val module: Module) : IdeaModuleInfo() {
    override val name = Name.special("<production sources for module ${module.getName()}>")

    override fun contentScope() = module.getModuleScope(false)

    override fun dependencies() = ideaModelDependencies(module, productionOnly = true)

    override fun friendModules() = listOf(module.testSourceInfo())
}

//TODO: (module refactoring) do not create ModuleTestSourceInfo when there are not test roots for module
public data class ModuleTestSourceInfo(val module: Module) : IdeaModuleInfo() {
    override val name = Name.special("<test sources for module ${module.getName()}>")

    override fun contentScope() = module.getModuleScope().intersectWith(GlobalSearchScope.notScope(module.getModuleScope(false)))

    override fun dependencies() = ideaModelDependencies(module, productionOnly = false)
}

public fun Module.productionSourceInfo(): ModuleProductionSourceInfo = ModuleProductionSourceInfo(this)
public fun Module.testSourceInfo(): ModuleTestSourceInfo = ModuleTestSourceInfo(this)

public data class LibraryInfo(val project: Project, val library: Library) : IdeaModuleInfo() {
    override val name: Name = Name.special("<library ${library.getName()}>")

    override fun contentScope() = LibraryWithoutSourceScope(project, library)

    override fun dependencies(): List<IdeaModuleInfo> {
        //TODO: (module refactoring) heuristic dependencies for libraries
        val orderEntry = ModuleManager.getInstance(project).getModules().stream().flatMap {
            ModuleRootManager.getInstance(it).getOrderEntries().stream()
        }.firstOrNull { it is JdkOrderEntry } as? JdkOrderEntry
        val sdk = orderEntry?.getJdk()
        return if (sdk != null) listOf(SdkInfo(project, sdk), this) else listOf(this)
    }
}

private data class LibrarySourceInfo(val project: Project, val library: Library) : IdeaModuleInfo() {
    override val name: Name = Name.special("<sources for library ${library.getName()}>")

    override fun contentScope() = GlobalSearchScope.EMPTY_SCOPE

    override fun dependencies(): List<IdeaModuleInfo> {
        return listOf(this) + LibraryInfo(project, library).dependencies()
    }
}

//TODO: (module refactoring) there should be separate SdkSourceInfo but there are no kotlin source in existing sdks for now :)
private data class SdkInfo(val project: Project, val sdk: Sdk) : IdeaModuleInfo() {
    override val name: Name = Name.special("<library ${sdk.getName()}>")

    override fun contentScope() = SdkScope(project, sdk)

    override fun dependencies(): List<IdeaModuleInfo> = listOf(this)
}

private object NotUnderContentRootModuleInfo : IdeaModuleInfo() {
    override val name: Name = Name.special("<special module for files not under source root>")

    override fun contentScope() = GlobalSearchScope.EMPTY_SCOPE

    //TODO: (module refactoring) dependency on runtime can be of use here
    override fun dependencies(): List<IdeaModuleInfo> = listOf(this)
}

private data class LibraryWithoutSourceScope(project: Project, private val library: Library) :
        LibraryScopeBase(project, library.getFiles(OrderRootType.CLASSES), array<VirtualFile>()) {
}

//TODO: (module refactoring) android sdk has modified scope
private data class SdkScope(project: Project, private val sdk: Sdk) :
        LibraryScopeBase(project, sdk.getRootProvider().getFiles(OrderRootType.CLASSES), array<VirtualFile>())
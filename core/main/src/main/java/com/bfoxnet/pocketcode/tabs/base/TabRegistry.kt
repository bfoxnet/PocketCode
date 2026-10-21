package com.bfoxnet.pocketcode.tabs.base

import com.bfoxnet.pocketcode.activities.main.MainViewModel
import com.rk.extension.api.XedExtensionPoint
import com.bfoxnet.pocketcode.file.BuiltinFileType
import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.file.FileTypeManager
import com.bfoxnet.pocketcode.tabs.image.ImageTab

@XedExtensionPoint
fun interface TabFactory {
    fun createTab(file: FileObject, projectRoot: FileObject?, scopeRoot: FileObject?, viewModel: MainViewModel): Tab
}

object TabRegistry {
    private val registeredTabs = mutableMapOf<String, TabFactory>()

    @XedExtensionPoint
    fun registerTab(tabFactory: TabFactory, fileExtensions: List<String>) {
        fileExtensions.forEach { registeredTabs[it] = tabFactory }
    }

    @XedExtensionPoint
    fun unregisterTab(tabFactory: TabFactory) {
        registeredTabs.values.remove(tabFactory)
    }

    fun getTab(
        file: FileObject,
        projectRoot: FileObject?,
        scopeRoot: FileObject?,
        viewModel: MainViewModel,
        readOnly: Boolean,
        customTitle: String?,
    ): Tab {
        val ext = file.getExtension()
        val type = FileTypeManager.fromExtension(ext)

        if (registeredTabs.containsKey(ext)) {
            return registeredTabs[ext]!!.createTab(file, projectRoot, scopeRoot, viewModel)
        }

        return when (type) {
            BuiltinFileType.IMAGE -> ImageTab(file, projectRoot, scopeRoot)
            else ->
                viewModel.editorManager.createEditorTab(
                    file = file,
                    projectRoot = projectRoot,
                    scopeRoot = scopeRoot,
                    isReadOnly = readOnly,
                    customTitle = customTitle,
                )
        }
    }
}

package com.bfoxnet.pocketcode.drawer

import com.bfoxnet.pocketcode.activities.main.ui.fileTreeViewModel
import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.file.FileWrapper
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.utils.application
import com.bfoxnet.pocketcode.utils.logError
import com.bfoxnet.pocketcode.utils.readObject
import com.bfoxnet.pocketcode.utils.toast
import com.bfoxnet.pocketcode.utils.writeObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

object DrawerPersistence {
    private val saveMutex = Mutex()

    private const val DRAWER_TABS = "drawerTabs"
    private const val CURRENT_DRAWER_TAB = "currentDrawerTab"
    private const val EXPANDED_FILE_TREE_NODES = "expandedFileTree"

    var stateRestored = false

    suspend fun saveState(viewModel: DrawerViewModel) {
        if (!stateRestored) return

        saveMutex.withLock {
            val filesDir = application?.filesDir ?: return@withLock

            val file = FileWrapper(filesDir.child(DRAWER_TABS))
            val serializableList = ArrayList(viewModel.drawerTabs.value)
            file.writeObject(serializableList)

            val currentTabFile = FileWrapper(filesDir.child(CURRENT_DRAWER_TAB))
            val currentTab = viewModel.currentDrawerTab
            if (currentTab != null) {
                currentTabFile.writeObject(currentTab)
            } else {
                currentTabFile.delete()
            }

            val expandedNodeFile = FileWrapper(filesDir.child(EXPANDED_FILE_TREE_NODES))
            fileTreeViewModel.get()?.getExpandedNodes()?.let { expandedNodeFile.writeObject(it) }
        }
    }

    suspend fun restoreState(viewModel: DrawerViewModel) {
        if (stateRestored) return

        saveMutex.withLock {
            val filesDir = application?.filesDir ?: return@withLock

            runCatching {
                val loadedTabs =
                    withContext(Dispatchers.IO) {
                        val file = FileWrapper(filesDir.child(DRAWER_TABS))

                        if (file.exists() && file.canRead()) {
                            file.readObject() as? ArrayList<DrawerTab> ?: emptyList()
                        } else {
                            emptyList()
                        }
                    }

                // Update the existing state list on Main thread
                withContext(Dispatchers.Main) { viewModel.forcePushDrawerTabs(loadedTabs) }

                val currentTabFile = FileWrapper(filesDir.child(CURRENT_DRAWER_TAB))
                if (currentTabFile.exists() && currentTabFile.canRead()) {
                    viewModel.selectDrawerTab(currentTabFile.readObject() as DrawerTab)
                }

                val expandedNodeFile = FileWrapper(filesDir.child(EXPANDED_FILE_TREE_NODES))
                if (expandedNodeFile.exists() && expandedNodeFile.canRead()) {
                    fileTreeViewModel
                        .get()
                        ?.setExpandedNodes(expandedNodeFile.readObject() as Map<FileObject, Set<FileObject>>)
                }
            }
                .onFailure {
                    logError(it)
                    toast(strings.project_restore_failed)
                }
        }
    }
}

package com.bfoxnet.pocketcode.components

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bfoxnet.pocketcode.activities.main.MainActivity
import com.bfoxnet.pocketcode.activities.main.MainViewModel
import com.bfoxnet.pocketcode.activities.main.ui.drawerStateRef
import com.bfoxnet.pocketcode.activities.main.ui.fileTreeViewModel
import com.bfoxnet.pocketcode.activities.main.ui.searchViewModel
import com.bfoxnet.pocketcode.commands.ToolbarConfiguration
import com.bfoxnet.pocketcode.drawer.DrawerViewModel
import com.bfoxnet.pocketcode.file.toFileObject
import com.bfoxnet.pocketcode.filetree.FileTreeTab
import com.bfoxnet.pocketcode.icons.CreateNewFile
import com.bfoxnet.pocketcode.icons.XedIcons
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.search.CodeSearchDialog
import com.bfoxnet.pocketcode.search.FileSearchDialog
import com.bfoxnet.pocketcode.tabs.editor.ToolbarAction
import com.bfoxnet.pocketcode.utils.application
import com.bfoxnet.pocketcode.utils.errorDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object GlobalDialogs {
    val addDialog = MutableStateFlow(false)

    val fileSearchDialog = MutableStateFlow(false)

    val codeSearchDialog = MutableStateFlow(false)
}

@Composable
fun GlobalToolbarActions(viewModel: MainViewModel) {
    val commands by ToolbarConfiguration.globalCommands.collectAsStateWithLifecycle()

    if (viewModel.visibleTabs.isEmpty() || viewModel.currentVisibleTab?.showGlobalActions == true) {
        for (command in commands) {
            if (command.isSupported()) ToolbarAction(command)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalDialogHost(viewModel: MainViewModel, drawerViewModel: DrawerViewModel) {
    val scope = rememberCoroutineScope()

    val drawerTabs by drawerViewModel.drawerTabs.collectAsStateWithLifecycle()
    val currentDrawerTabIndex by drawerViewModel.currentDrawerTabIndex.collectAsStateWithLifecycle()
    val currentDrawerTab = drawerTabs.getOrNull(currentDrawerTabIndex)

    val addDialog by GlobalDialogs.addDialog.collectAsStateWithLifecycle()
    val fileSearchDialog by GlobalDialogs.fileSearchDialog.collectAsStateWithLifecycle()
    val codeSearchDialog by GlobalDialogs.codeSearchDialog.collectAsStateWithLifecycle()

    if (fileSearchDialog && currentDrawerTab is FileTreeTab) {
        FileSearchDialog(
            mainViewModel = viewModel,
            searchViewModel = searchViewModel.get()!!,
            projectFile = currentDrawerTab.root,
            onFinish = { GlobalDialogs.fileSearchDialog.value = false },
            onSelect = { projectFile, fileObject ->
                scope.launch {
                    if (fileObject.isFile()) {
                        viewModel.editorManager.openFile(
                            fileObject = fileObject,
                            projectRoot = projectFile,
                            checkDuplicate = true,
                            switchToTab = true,
                        )
                        drawerStateRef.get()?.close()
                    } else {
                        fileTreeViewModel.get()?.goToFolder(projectFile, fileObject)
                        drawerStateRef.get()?.open()
                    }
                }
            },
        )
    }

    if (codeSearchDialog && currentDrawerTab is FileTreeTab) {
        CodeSearchDialog(
            mainViewModel = viewModel,
            searchViewModel = searchViewModel.get()!!,
            projectFile = currentDrawerTab.root,
            onFinish = { GlobalDialogs.codeSearchDialog.value = false },
        )
    }

    if (addDialog) {
        ModalBottomSheet(onDismissRequest = { GlobalDialogs.addDialog.value = false }) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp)) {
                AddDialogItem(resId = drawables.file, title = stringResource(strings.temp_file)) {
                    GlobalDialogs.addDialog.value = false

                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "application/octet-stream"
                    intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt")

                    val activities =
                        application!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

                    if (activities.isEmpty()) {
                        errorDialog(strings.unsupported_feature)
                        return@AddDialogItem
                    }

                    val title = viewModel.getNextUntitledTitle()
                    viewModel.editorManager.addEditorTab(file = null, customTitle = title)
                }

                AddDialogItem(icon = XedIcons.CreateNewFile, title = stringResource(strings.new_file)) {
                    GlobalDialogs.addDialog.value = false
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "application/octet-stream"
                    intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt")

                    val activities =
                        application!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
                    if (activities.isEmpty()) {
                        errorDialog(strings.unsupported_feature)
                    } else {
                        MainActivity.instance?.apply {
                            fileManager.createNewFile(mimeType = "*/*", title = "newfile.txt") {
                                if (it != null) {
                                    lifecycleScope.launch {
                                        viewModel.editorManager.openFile(
                                            fileObject = it,
                                            projectRoot = null,
                                            checkDuplicate = true,
                                            switchToTab = true,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                AddDialogItem(resId = drawables.file_symlink, title = stringResource(strings.open_file)) {
                    GlobalDialogs.addDialog.value = false
                    MainActivity.instance?.apply {
                        fileManager.requestOpenFile(mimeType = "*/*") {
                            if (it != null) {
                                lifecycleScope.launch {
                                    viewModel.editorManager.openFile(
                                        fileObject = it.toFileObject(expectedIsFile = true),
                                        checkDuplicate = true,
                                        projectRoot = null,
                                        switchToTab = true,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

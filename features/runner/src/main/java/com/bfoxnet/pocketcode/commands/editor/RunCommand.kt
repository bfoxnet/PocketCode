package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.Command
import com.bfoxnet.pocketcode.commands.CommandProvider
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.filetree.FileTreeTab
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.runner.RunnerManager
import com.bfoxnet.pocketcode.runner.RunnerUI
import com.bfoxnet.pocketcode.tabs.editor.EditorTab
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
object RunCommand : Command() {
    override val id: String = "editor.run"

    override fun getLabel(): String = strings.run.getString()

    override fun execute(context: ActionContext) {
        launchRunner(context, forceSelection = false)
    }

    override fun onLongClick(context: ActionContext): Boolean {
        launchRunner(context, forceSelection = true)
        return true
    }

    private fun launchRunner(context: ActionContext, forceSelection: Boolean) {
        val mainViewModel = commandContext.mainViewModel
        val drawerViewModel = commandContext.drawerViewModel

        val currentTab = mainViewModel.currentTab as? EditorTab
        val currentDrawerTab = drawerViewModel.currentDrawerTab as? FileTreeTab
        val projectRoot = currentTab?.projectRoot ?: currentDrawerTab?.root
        val fileObject = currentTab?.file

        RunnerManager.run(
            activity = context.currentActivity,
            fileObject = fileObject,
            projectRoot = projectRoot,
            forceSelection = forceSelection,
            beforeRun = {
                if (currentTab != null) {
                    CommandProvider.SaveCommand.execute(context)
                }
            },
            onMultipleRunners = {
                RunnerUI.runnersToShow.value = it
                RunnerUI.showRunnerDialog.value = true
            },
        )
    }

    override fun isSupported(): Boolean {
        val mainViewModel = commandContext.mainViewModel
        val drawerViewModel = commandContext.drawerViewModel

        val currentTab = mainViewModel.currentTab as? EditorTab
        val currentDrawerTab = drawerViewModel.currentDrawerTab as? FileTreeTab
        val projectRoot = currentTab?.projectRoot ?: currentDrawerTab?.root
        val fileObject = currentTab?.file

        return RunnerManager.isRunnable(fileObject, projectRoot)
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.run)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_F5)
}

package com.bfoxnet.pocketcode.git

import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.filetree.FileTreeTab
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import java.io.File

object GitInitCommand : GlobalCommand() {
    override val id: String = "git.init"

    override fun getLabel(): String = strings.git_init.getString()

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.git)

    override fun isSupported(): Boolean {
        val drawerViewModel = commandContext.drawerViewModel
        val currentTab = drawerViewModel.currentDrawerTab
        return currentTab is FileTreeTab
    }

    override fun execute(context: ActionContext) {
        val drawerViewModel = commandContext.drawerViewModel
        val currentTab = drawerViewModel.currentDrawerTab as? FileTreeTab ?: return
        val rootPath = currentTab.root.getAbsolutePath()

        val gitViewModel = gitViewModel
        gitViewModel?.initRepository(File(rootPath)) { gitViewModel.loadRepository(rootPath) }
    }
}

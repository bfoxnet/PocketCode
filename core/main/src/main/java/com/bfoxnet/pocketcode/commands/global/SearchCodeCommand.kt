package com.bfoxnet.pocketcode.commands.global

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.components.GlobalDialogs
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class SearchCodeCommand : GlobalCommand() {
    override val id: String = "global.search_code"

    override fun getLabel(): String = strings.search_code.getString()

    override fun execute(context: ActionContext) {
        GlobalDialogs.codeSearchDialog.value = true
    }

    override fun isEnabled(): Boolean {
        return commandContext.drawerViewModel.currentDrawerTab != null
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.search)

    override val defaultKeybinds: KeyCombination =
        KeyCombination(keyCode = KeyEvent.KEYCODE_F, ctrl = true, shift = true)
}

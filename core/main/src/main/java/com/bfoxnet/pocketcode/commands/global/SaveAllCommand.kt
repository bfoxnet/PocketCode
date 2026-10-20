package com.bfoxnet.pocketcode.commands.global

import android.view.KeyEvent
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveAllCommand : GlobalCommand() {
    override val id: String = "global.save_all"

    override fun getLabel(): String = strings.save_all.getString()

    override fun execute(context: ActionContext) {
        commandContext.mainViewModel.editorTabs.forEach { DefaultScope.launch(Dispatchers.IO) { it.save() } }
    }

    override fun isEnabled(): Boolean {
        return commandContext.mainViewModel.editorTabs.any { it.editorState.isDirty } || Settings.auto_save
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.save)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_S, ctrl = true, alt = true)
}

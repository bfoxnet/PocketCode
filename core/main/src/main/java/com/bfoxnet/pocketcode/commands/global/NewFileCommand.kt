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

class NewFileCommand : GlobalCommand() {
    override val id: String = "global.new_file"

    override fun getLabel(): String = strings.new_file.getString()

    override fun execute(context: ActionContext) {
        GlobalDialogs.addDialog.value = true
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.add)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_N, ctrl = true)
}

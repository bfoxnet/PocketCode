package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class CopyCommand : EditorCommand() {
    override val id: String = "editor.copy"

    override fun getLabel(): String = strings.copy.getString()

    override fun execute(context: EditorActionContext) {
        context.editor.copyText()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.copy)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_C, ctrl = true)
}

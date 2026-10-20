package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class SelectWordCommand : EditorCommand() {
    override val id: String = "editor.select_word"

    override fun getLabel(): String = strings.select_word.getString()

    override fun execute(context: EditorActionContext) {
        context.editor.selectCurrentWord()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.select)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_W, ctrl = true)
}

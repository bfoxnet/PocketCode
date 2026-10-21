package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.EditorNonActionContext
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class CutCommand : EditorCommand() {
    override val id: String = "editor.cut"

    override fun getLabel(): String = strings.cut.getString()

    override fun execute(context: EditorActionContext) {
        context.editor.cutText()
    }

    override fun isEnabled(context: EditorNonActionContext): Boolean {
        return context.editorTab.editorState.editable
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.cut)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_X, ctrl = true)
}

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

class DuplicateLineCommand : EditorCommand() {
    override val id: String = "editor.duplicate_line"

    override fun getLabel(): String = strings.duplicate_line.getString()

    override fun execute(context: EditorActionContext) {
        context.editor.duplicateLine()
    }

    override fun isEnabled(context: EditorNonActionContext): Boolean {
        return context.editorTab.editorState.editable
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.duplicate_line)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_D, ctrl = true)
}

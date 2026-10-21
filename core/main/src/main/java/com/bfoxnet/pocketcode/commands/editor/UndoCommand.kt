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

class UndoCommand : EditorCommand() {
    override val id: String = "editor.undo"

    override val repeatOnHold: Boolean = true

    override fun getLabel(): String = strings.undo.getString()

    override fun execute(context: EditorActionContext) {
        val editor = context.editor
        if (editor.canUndo()) editor.undo()
        context.editorTab.editorState.updateUndoRedo()
    }

    override fun isEnabled(context: EditorNonActionContext): Boolean {
        val editorState = context.editorTab.editorState
        return editorState.editable && editorState.canUndo
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.undo)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_Z, ctrl = true)
}

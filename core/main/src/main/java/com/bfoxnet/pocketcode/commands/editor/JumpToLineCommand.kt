package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class JumpToLineCommand : EditorCommand() {
    override val id: String = "editor.jump_to_line"

    override fun getLabel(): String = strings.jump_to_line.getString()

    override fun execute(context: EditorActionContext) {
        context.editorTab.editorState.apply {
            showJumpToLineDialog = true
            val line = context.editor.cursor.leftLine + 1
            val column = context.editor.cursor.leftColumn + 1
            jumpToLineValue = "$line:$column"
        }
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.arrow_outward)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_G, ctrl = true)
}

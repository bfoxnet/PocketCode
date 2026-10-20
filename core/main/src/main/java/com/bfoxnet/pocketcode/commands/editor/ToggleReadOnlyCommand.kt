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
import com.bfoxnet.pocketcode.tabs.editor.EditorTab

class ToggleReadOnlyCommand : EditorCommand() {
    override val id: String = "editor.editable"

    override fun getLabel(): String {
        val editorTab = commandContext.mainViewModel.currentTab as? EditorTab
        return if (editorTab?.editorState?.editable == true) {
            strings.read_mode.getString()
        } else {
            strings.edit_mode.getString()
        }
    }

    override fun execute(context: EditorActionContext) {
        val editorState = context.editorTab.editorState
        context.editorTab.removeNotice("binary_file")
        editorState.editable = !editorState.editable
    }

    override fun isEnabled(context: EditorNonActionContext): Boolean {
        return !context.editorTab.isReadOnly
    }

    override fun getIcon(): Icon {
        val editorTab = commandContext.mainViewModel.currentTab as? EditorTab
        return if (editorTab?.editorState?.editable == true) {
            Icon.ResourceIcon(drawables.lock)
        } else {
            Icon.ResourceIcon(drawables.edit)
        }
    }

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_E, ctrl = true)
}

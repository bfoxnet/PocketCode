package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.EditorNonActionContext
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaveCommand : EditorCommand() {
    override val id: String = "editor.save"

    override fun getLabel(): String = strings.save.getString()

    override fun execute(context: EditorActionContext) {
        DefaultScope.launch(Dispatchers.IO) { context.editorTab.save() }
    }

    override fun isEnabled(context: EditorNonActionContext): Boolean {
        return !context.editorTab.isReadOnly && (context.editorTab.editorState.isDirty || Settings.auto_save)
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.save)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_S, ctrl = true)
}

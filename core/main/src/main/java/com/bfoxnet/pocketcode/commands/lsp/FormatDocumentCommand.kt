package com.bfoxnet.pocketcode.commands.lsp

import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.tabs.editor.EditorTab

class FormatDocumentCommand : EditorCommand() {
    override val id: String = "editor.format_document"

    override fun getLabel(): String = strings.format_document.getString()

    override fun execute(context: EditorActionContext) {
        context.editorTab.registerTask(EditorTab.FORMAT_DOCUMENT_TASK_ID)
        context.editor.formatCodeAsync()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.auto_fix)
}

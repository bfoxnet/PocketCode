package com.bfoxnet.pocketcode.commands.lsp

import com.bfoxnet.pocketcode.commands.LspActionContext
import com.bfoxnet.pocketcode.commands.LspCommand
import com.bfoxnet.pocketcode.commands.LspNonActionContext
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.lsp.formatDocumentSuspend
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import kotlinx.coroutines.launch

class FormatDocumentLspCommand : LspCommand() {
    override val id: String = "lsp.format_document"

    override fun getLabel(): String = strings.format_document_lsp.getString()

    override fun execute(context: LspActionContext) {
        context.editorTab.scope.launch { formatDocumentSuspend(context.editorTab) }
    }

    override fun isEnabled(context: LspNonActionContext): Boolean {
        return context.editorTab.editorState.editable
    }

    override fun isSupported(context: LspNonActionContext): Boolean {
        return context.lspConnector.isFormattingSupported()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.auto_fix)
}

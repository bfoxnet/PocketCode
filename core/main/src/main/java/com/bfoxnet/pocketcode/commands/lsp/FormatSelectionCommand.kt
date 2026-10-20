package com.bfoxnet.pocketcode.commands.lsp

import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.LspActionContext
import com.bfoxnet.pocketcode.commands.LspCommand
import com.bfoxnet.pocketcode.commands.LspNonActionContext
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.lsp.formatDocumentRange
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class FormatSelectionCommand : LspCommand() {
    override val id: String = "lsp.format_selection"

    override fun getLabel(): String = strings.format_selection.getString()

    override fun execute(context: LspActionContext) {
        formatDocumentRange(DefaultScope, context.editorTab)
    }

    override fun isEnabled(context: LspNonActionContext): Boolean {
        return context.editorTab.editorState.editable
    }

    override fun isSupported(context: LspNonActionContext): Boolean {
        return context.lspConnector.isRangeFormattingSupported()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.auto_fix)
}

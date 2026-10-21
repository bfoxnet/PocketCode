package com.bfoxnet.pocketcode.commands.lsp

import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.LspActionContext
import com.bfoxnet.pocketcode.commands.LspCommand
import com.bfoxnet.pocketcode.commands.LspNonActionContext
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.lsp.renameSymbol
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class RenameSymbolCommand : LspCommand() {
    override val id: String = "lsp.rename_symbol"

    override fun getLabel(): String = strings.rename_symbol.getString()

    override fun execute(context: LspActionContext) {
        renameSymbol(DefaultScope, context.editorTab)
    }

    override fun isSupported(context: LspNonActionContext): Boolean {
        return context.lspConnector.isRenameSymbolSupported()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.manage_search)
}

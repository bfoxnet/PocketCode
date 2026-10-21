package com.bfoxnet.pocketcode.commands.lsp

import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.LspActionContext
import com.bfoxnet.pocketcode.commands.LspCommand
import com.bfoxnet.pocketcode.commands.LspNonActionContext
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.lsp.goToDefinition
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class GoToDefinitionCommand : LspCommand() {
    override val id: String = "lsp.go_to_definition"

    override fun getLabel(): String = strings.go_to_definition.getString()

    override fun execute(context: LspActionContext) {
        goToDefinition(
            scope = DefaultScope,
            context = context.currentActivity,
            viewModel = commandContext.mainViewModel,
            editorTab = context.editorTab,
        )
    }

    override fun isSupported(context: LspNonActionContext): Boolean {
        return context.lspConnector.isGoToDefinitionSupported()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.jump_to_element)
}

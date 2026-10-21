package com.bfoxnet.pocketcode.ai.command

import com.bfoxnet.pocketcode.activities.main.MainActivity
import com.bfoxnet.pocketcode.ai.icons.SparklesTiny
import com.bfoxnet.pocketcode.ai.tab.AiTab
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class AIChatCommand : GlobalCommand() {
    override val id: String = "global.ai_chat"

    override fun getLabel(): String = strings.ai_chat_command_label.getString()

    override fun execute(context: ActionContext) {
        MainActivity.instance?.viewModel?.tabManager?.addTab(AiTab(), switchToTab = true)
    }

    override fun getIcon(): Icon = Icon.VectorIcon(SparklesTiny)

    override fun isSupported(): Boolean {
        return MainActivity.instance?.viewModel?.currentVisibleTab !is AiTab
    }
}

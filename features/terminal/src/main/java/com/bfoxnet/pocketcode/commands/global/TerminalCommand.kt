package com.bfoxnet.pocketcode.commands.global

import android.content.Intent
import android.view.KeyEvent
import com.bfoxnet.pocketcode.activities.terminal.Terminal
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.feature.FeatureRegistry
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

object TerminalCommand : GlobalCommand() {
    override val id: String = "global.terminal"

    override fun getLabel(): String = strings.terminal.getString()

    override fun execute(context: ActionContext) {
        val activity = context.currentActivity
        val intent = Intent(activity, Terminal::class.java)
        activity.startActivity(intent)
    }

    override fun isSupported(): Boolean = FeatureRegistry.isEnabled("feature_terminal")

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.terminal)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_J, ctrl = true)
}

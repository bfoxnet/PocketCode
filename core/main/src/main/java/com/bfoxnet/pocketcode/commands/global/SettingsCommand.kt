package com.bfoxnet.pocketcode.commands.global

import android.content.Intent
import android.view.KeyEvent
import com.bfoxnet.pocketcode.activities.settings.SettingsActivity
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class SettingsCommand : GlobalCommand() {
    override val id: String = "global.settings"

    override fun getLabel(): String = strings.settings.getString()

    override fun execute(context: ActionContext) {
        val activity = context.currentActivity
        activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.settings)

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_COMMA, ctrl = true)
}

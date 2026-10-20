package com.bfoxnet.pocketcode.commands.global

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings

class CommandPaletteCommand : GlobalCommand() {
    override val id: String = "global.command_palette"

    override fun getLabel(): String = strings.command_palette.getString()

    override fun execute(context: ActionContext) {
        commandContext.mainViewModel.showCommandPalette()
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.command_palette)

    override val defaultKeybinds: KeyCombination =
        KeyCombination(keyCode = KeyEvent.KEYCODE_P, ctrl = true, shift = true)
}

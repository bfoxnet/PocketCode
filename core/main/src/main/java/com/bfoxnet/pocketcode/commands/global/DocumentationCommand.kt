package com.bfoxnet.pocketcode.commands.global

import android.view.KeyEvent
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.icons.Menu_book
import com.bfoxnet.pocketcode.icons.XedIcons
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.utils.openUrl

class DocumentationCommand : GlobalCommand() {
    override val id: String = "global.documentation"

    override fun getLabel(): String = strings.docs.getString()

    override fun getIcon(): Icon = Icon.VectorIcon(XedIcons.Menu_book)

    override fun execute(context: ActionContext) {
        val url = "https://xed-editor.github.io/Xed-Docs/"
        context.currentActivity.openUrl(url)
    }

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_F1)
}

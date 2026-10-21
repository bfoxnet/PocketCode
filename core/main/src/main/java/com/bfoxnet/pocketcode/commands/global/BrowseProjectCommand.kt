package com.bfoxnet.pocketcode.commands.global

import android.view.KeyEvent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.ui.platform.AndroidUiDispatcher
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.activities.main.ui.drawerStateRef
import com.bfoxnet.pocketcode.commands.ActionContext
import com.bfoxnet.pocketcode.commands.GlobalCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import kotlinx.coroutines.launch

class BrowseProjectCommand : GlobalCommand() {
    override val id: String = "global.browse_project"

    override fun getLabel(): String = strings.browse_project.getString()

    override fun getIcon(): Icon = Icon.VectorIcon(Icons.Rounded.Menu)

    override fun execute(context: ActionContext) {
        DefaultScope.launch(AndroidUiDispatcher.Main) { drawerStateRef.get()?.open() }
    }

    override val defaultKeybinds: KeyCombination = KeyCombination(keyCode = KeyEvent.KEYCODE_B, ctrl = true)
}

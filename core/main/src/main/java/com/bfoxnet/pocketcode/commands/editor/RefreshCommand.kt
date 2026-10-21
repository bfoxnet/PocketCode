package com.bfoxnet.pocketcode.commands.editor

import android.view.KeyEvent
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.EditorActionContext
import com.bfoxnet.pocketcode.commands.EditorCommand
import com.bfoxnet.pocketcode.commands.KeyCombination
import com.bfoxnet.pocketcode.events.EditorTabEvent
import com.bfoxnet.pocketcode.events.Events
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.tabs.editor.EditorTab
import com.bfoxnet.pocketcode.utils.dialogRes
import kotlinx.coroutines.launch

class RefreshCommand : EditorCommand() {
    override val id: String = "editor.refresh"

    override fun getLabel(): String = strings.refresh.getString()

    override fun execute(context: EditorActionContext) {
        val currentTab = context.editorTab
        if (currentTab.editorState.isDirty) {
            dialogRes(
                activity = context.currentActivity,
                title = strings.attention.getString(),
                msg = strings.ask_refresh.getString(),
                okRes = strings.refresh,
                onCancel = {},
                onOk = {
                    currentTab.refresh()
                    publishEvent(currentTab)
                },
            )
        } else {
            currentTab.refresh()
            publishEvent(currentTab)
        }
    }

    private fun publishEvent(currentTab: EditorTab) {
        DefaultScope.launch { Events.publish(EditorTabEvent.Refreshed(currentTab)) }
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.refresh)

    override val defaultKeybinds: KeyCombination =
        KeyCombination(keyCode = KeyEvent.KEYCODE_R, ctrl = true, shift = true)
}

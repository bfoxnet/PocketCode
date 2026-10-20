package com.bfoxnet.pocketcode.exec

import android.app.Activity
import android.content.Intent
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.file.localDir
import com.bfoxnet.pocketcode.file.sandboxDir
import com.bfoxnet.pocketcode.file.sandboxHomeDir
import com.bfoxnet.pocketcode.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun isTerminalInstalled(): Boolean {
    val rootfs =
        sandboxDir().listFiles()?.filter {
            it.absolutePath != sandboxHomeDir().absolutePath &&
                it.absolutePath != sandboxDir().child("tmp").absolutePath
        } ?: emptyList()

    return localDir().child(".terminal_setup_ok_DO_NOT_REMOVE").exists() && rootfs.isNotEmpty()
}

suspend fun isTerminalWorking(): Boolean =
    withContext(Dispatchers.IO) {
        val process = ubuntuProcess(command = arrayOf("true"))
        return@withContext process.waitFor() == 0
    }

fun launchTerminal(activity: Activity, terminalCommand: TerminalCommand) {
    pendingCommand = terminalCommand
    try {
        val intent = Intent().setClassName(activity, "com.bfoxnet.pocketcode.activities.terminal.Terminal")
        activity.startActivity(intent)
    } catch (_: Exception) {
        toast("Terminal feature is not available in this build")
    }
}

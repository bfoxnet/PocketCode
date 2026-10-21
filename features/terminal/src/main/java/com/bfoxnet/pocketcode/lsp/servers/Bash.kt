package com.bfoxnet.pocketcode.lsp.servers

import android.content.Context
import com.bfoxnet.pocketcode.exec.NpmUtils
import com.bfoxnet.pocketcode.exec.isTerminalInstalled
import com.bfoxnet.pocketcode.file.BuiltinFileType
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.file.localBinDir
import com.bfoxnet.pocketcode.file.sandboxDir
import com.bfoxnet.pocketcode.lsp.LspConnectionConfig
import com.bfoxnet.pocketcode.lsp.ScriptedLspServer

object Bash : ScriptedLspServer() {
    override val id: String = "bash"
    override val languageName: String = "Bash"
    override val serverName = "bash-language-server"
    override val supportedExtensions = BuiltinFileType.SHELL.extensions
    override val icon = BuiltinFileType.SHELL.icon

    override val installScript = localBinDir().child("lsp/bash")
    override val installId = "Bash language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) {
            return false
        }

        return sandboxDir().child("/usr/bin/$serverName").exists()
    }

    override suspend fun hasUpdate(context: Context): Boolean {
        return NpmUtils.hasUpdate(serverName)
    }

    override fun getConnectionConfig(): LspConnectionConfig {
        return LspConnectionConfig.Process(arrayOf("/usr/bin/node", "/usr/bin/$serverName", "start"))
    }
}

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

object HTML : ScriptedLspServer() {
    override val id: String = "html"
    override val languageName: String = "HTML"
    override val serverName = "vscode-html-language-server"
    override val supportedExtensions = BuiltinFileType.HTML.extensions + BuiltinFileType.HTMX.extensions
    override val icon = BuiltinFileType.HTML.icon

    override val installScript = localBinDir().child("lsp/html")
    override val installId = "HTML language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) {
            return false
        }

        return sandboxDir().child("/usr/bin/$serverName").exists()
    }

    override suspend fun hasUpdate(context: Context): Boolean {
        return NpmUtils.hasUpdate("vscode-langservers-extracted")
    }

    override fun getConnectionConfig(): LspConnectionConfig {
        return LspConnectionConfig.Process(arrayOf("/usr/bin/node", "/usr/bin/$serverName", "--stdio"))
    }
}

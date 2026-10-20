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

object TypeScript : ScriptedLspServer() {
    override val id: String = "typescript"
    override val languageName: String = "TypeScript"
    override val serverName = "tsc"
    override val supportedExtensions =
        BuiltinFileType.JAVASCRIPT.extensions +
            BuiltinFileType.TYPESCRIPT.extensions +
            BuiltinFileType.JSX.extensions +
            BuiltinFileType.TSX.extensions
    override val icon = BuiltinFileType.TYPESCRIPT.icon

    override val installScript = localBinDir().child("lsp/typescript")
    override val installId = "TypeScript language server"

    override suspend fun isInstalled(context: Context): Boolean {
        if (!isTerminalInstalled()) {
            return false
        }

        return sandboxDir().child("/usr/bin/$serverName").exists()
    }

    override suspend fun hasUpdate(context: Context): Boolean {
        return NpmUtils.hasUpdate("typescript")
    }

    override fun getConnectionConfig(): LspConnectionConfig {
        return LspConnectionConfig.Process(arrayOf("/usr/bin/$serverName", "--lsp", "--stdio"))
    }
}

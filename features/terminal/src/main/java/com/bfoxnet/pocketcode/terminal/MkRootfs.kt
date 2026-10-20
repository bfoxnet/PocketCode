package com.bfoxnet.pocketcode.terminal

import android.content.Context
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.file.sandboxDir
import com.bfoxnet.pocketcode.file.sandboxHomeDir
import com.bfoxnet.pocketcode.utils.getTempDir
import com.bfoxnet.pocketcode.utils.isMainThread
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class NEXT_STAGE {
    NONE,
    EXTRACTION,
}

suspend fun CoroutineScope.getNextStage(context: Context): NEXT_STAGE =
    withContext(Dispatchers.IO) {
        if (isMainThread()) {
            throw RuntimeException("IO operation on the main thread")
        }

        val sandboxFile = File(getTempDir(), "sandbox.tar.gz")
        val rootfsFiles =
            sandboxDir().listFiles()?.filter {
                it.absolutePath != sandboxHomeDir().absolutePath &&
                    it.absolutePath != sandboxDir().child("tmp").absolutePath
            } ?: emptyList()

        return@withContext if (sandboxFile.exists().not() || rootfsFiles.isEmpty().not()) {
            NEXT_STAGE.NONE
        } else {
            NEXT_STAGE.EXTRACTION
        }
    }

package com.bfoxnet.pocketcode.commands.editor

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.commands.EditorFileActionContext
import com.bfoxnet.pocketcode.commands.EditorFileCommand
import com.bfoxnet.pocketcode.file.FileWrapper
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.utils.toast
import kotlinx.coroutines.launch

class ShareCommand : EditorFileCommand() {
    override val id: String = "editor.share"

    override fun getLabel(): String = strings.share.getString()

    override fun execute(context: EditorFileActionContext) {
        val activity = context.currentActivity
        val file = context.file

        DefaultScope.launch {
            if (file.getAbsolutePath().contains(activity.filesDir.parentFile!!.absolutePath)) {
                toast(strings.permission_denied)
                return@launch
            }

            val fileUri =
                if (file is FileWrapper) {
                    FileProvider.getUriForFile(activity as Context, "${activity.packageName}.fileprovider", file.file)
                } else {
                    file.toUri()
                }

            val intent =
                Intent(Intent.ACTION_SEND).apply {
                    type = activity.contentResolver.getType(fileUri) ?: "*/*"
                    setDataAndType(fileUri, activity.contentResolver.getType(fileUri) ?: "*/*")
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

            activity.startActivity(Intent.createChooser(intent, "Share file"))
        }
    }

    override fun getIcon(): Icon = Icon.ResourceIcon(drawables.send)
}

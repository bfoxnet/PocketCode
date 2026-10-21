package com.bfoxnet.pocketcode.project

import android.content.Intent
import com.bfoxnet.pocketcode.DefaultScope
import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.filetree.FileAction
import com.bfoxnet.pocketcode.filetree.FileActionContext
import com.bfoxnet.pocketcode.filetree.FileActionType
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import kotlinx.coroutines.launch

object NewProjectAction : FileAction() {
    override val icon = Icon.ResourceIcon(drawables.folder_managed)
    override val title = strings.new_project.getString()

    override suspend fun execute(context: FileActionContext) {
        DefaultScope.launch {
            val intent =
                Intent(context.context, ProjectCreatorActivity::class.java).apply { putExtra("root", context.file) }
            context.context.startActivity(intent)
        }
    }

    override suspend fun isSupported(file: FileObject, root: FileObject?): Boolean {
        return ProjectTemplateRegistry.categories.value.any { it.templates.isNotEmpty() }
    }

    override val type = FileActionType(file = false, folder = true, rootFolder = true)
}

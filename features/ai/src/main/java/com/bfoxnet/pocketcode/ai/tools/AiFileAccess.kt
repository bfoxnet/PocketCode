package com.bfoxnet.pocketcode.ai.tools

import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.file.FileWrapper
import com.bfoxnet.pocketcode.file.NetWrapper
import com.bfoxnet.pocketcode.file.UriWrapper
import com.bfoxnet.pocketcode.file.ZipFileObject

enum class AiFileKind {
    Local,
    Document,
    Remote,
    Archive,
    Unknown,
}

fun FileObject.aiKind(): AiFileKind =
    when (this) {
        is FileWrapper -> AiFileKind.Local
        is UriWrapper -> AiFileKind.Document
        is NetWrapper -> AiFileKind.Remote
        is ZipFileObject -> AiFileKind.Archive
        else -> AiFileKind.Unknown
    }

fun FileObject.nativePathOrNull(): String? = if (this is FileWrapper) getAbsolutePath() else null

internal val AI_IGNORED_DIRS = setOf(".git", ".gradle", ".idea", "build", "node_modules", ".cxx")

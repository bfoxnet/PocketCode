package com.bfoxnet.pocketcode.search.file

import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.search.index.FileMeta

/** Strategy for searching file names. */
interface FileSearchStrategy {
    suspend fun search(query: String, projectRoot: FileObject): List<FileMeta>
}

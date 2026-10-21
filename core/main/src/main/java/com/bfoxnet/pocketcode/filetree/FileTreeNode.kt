package com.bfoxnet.pocketcode.filetree

import com.bfoxnet.pocketcode.file.FileObject

data class FileTreeNode(val file: FileObject, val isFile: Boolean, val isExpandable: Boolean, val name: String)

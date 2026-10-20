package com.bfoxnet.pocketcode.runner

import android.content.Context
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.file.createDirIfNot
import com.bfoxnet.pocketcode.file.localDir
import com.bfoxnet.pocketcode.utils.application
import java.io.File

fun runnerDir(context: Context = application!!): File {
    return localDir(context).child("runners").createDirIfNot()
}

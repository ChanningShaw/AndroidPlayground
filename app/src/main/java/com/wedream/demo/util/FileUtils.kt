package com.wedream.demo.util

import android.content.Context
import java.io.File

object FileUtils {


}

fun Context.getCrashDir(): File {
    val crashDir = File(filesDir, "/crash/")
    if (!crashDir.exists()) {
        crashDir.mkdirs()
    }
    return crashDir
}
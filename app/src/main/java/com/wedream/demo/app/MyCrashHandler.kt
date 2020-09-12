package com.wedream.demo.app

import com.wedream.demo.util.LogUtils.log

class MyCrashHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        e.printStackTrace()
        log { "程序出现异常了 Thread = ${t.name}, cause = ${e.cause}" }
    }
}
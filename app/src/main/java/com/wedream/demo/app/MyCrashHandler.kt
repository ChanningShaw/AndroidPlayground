package com.wedream.demo.app

import android.os.Process
import com.wedream.demo.util.LogUtils.getStack
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.getCrashDir
import com.wedream.demo.util.save
import com.wedream.demo.util.shot

class MyCrashHandler(private val application: MyApplication) : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        val lastResumeActivity = application.getCurrentResumeActivity() ?: return
        val stack = e.getStack()
        log { "程序出现异常了，[${t.name}][${lastResumeActivity.javaClass.canonicalName}], cause = ${e.cause} ,stack = \n${stack}" }
        // 截图
        val bitmap = lastResumeActivity.shot()
        bitmap.save(application.getCrashDir())
        Process.killProcess(Process.myPid())
    }
}
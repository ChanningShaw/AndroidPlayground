package com.wedream.demo.app

import android.os.Looper
import android.os.Process
import com.wedream.demo.app.monitor.ANRError
import com.wedream.demo.app.monitor.ANRWatchDog
import com.wedream.demo.util.LogUtils.getStack
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.getCrashDir
import com.wedream.demo.util.save
import com.wedream.demo.util.shot

class CrashHandler(private val application: MyApplication) : Thread.UncaughtExceptionHandler {

    init {
        initHandler()
    }

    private fun initHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this)
        val watchdog = ANRWatchDog().apply {
            setReportMainThreadOnly()
            setIgnoreDebugger(true)
        }
        watchdog.setANRListener(object : ANRWatchDog.ANRListener{
            override fun onAppNotResponding(error: ANRError) {
                handleException(ErrorType.ANR, Looper.getMainLooper().thread, error)
            }
        })
        watchdog.start()
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        handleException(ErrorType.Exception, Looper.getMainLooper().thread, e)
    }

    private fun handleException(type: ErrorType, t: Thread, e: Throwable) {
        val lastResumeActivity = application.getCurrentResumeActivity() ?: return
        val stack = e.getStack()
        log { "$type raised，[${t.name}][${lastResumeActivity.javaClass.canonicalName}], msg = ${e.message} \n${stack}" }
        // 截图
        val bitmap = lastResumeActivity.shot()
        bitmap.save(application.getCrashDir())
        Process.killProcess(Process.myPid())
    }

    enum class ErrorType {
        ANR, Exception
    }
}
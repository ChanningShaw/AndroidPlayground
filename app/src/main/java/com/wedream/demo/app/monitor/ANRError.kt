package com.wedream.demo.app.monitor

import android.os.Looper
import java.util.*

/**
 * Error thrown by [com.github.anrwatchdog.ANRWatchDog] when an ANR is detected.
 * Contains the stack trace of the frozen UI thread.
 *
 *
 * It is important to notice that, in an ANRError, all the "Caused by" are not really the cause
 * of the exception. Each "Caused by" is the stack trace of a running thread. Note that the main
 * thread always comes first.
 */
/**
 * The minimum duration, in ms, for which the main thread has been blocked. May be more.
 */
class ANRError private constructor(t: ANRThrowable?, val duration: Long) :
    Error("Application Not Responding for at least $duration ms.", t, false, false) {

    private class ANRThrowable(
        name: String,
        _stackTrace: Array<StackTraceElement>,
        cause: Throwable? = null
    ) : Throwable(name, cause) {

        init {
            stackTrace = _stackTrace
        }

        override fun fillInStackTrace(): Throwable {
            stackTrace = arrayOf()
            return this
        }
    }

    override fun fillInStackTrace(): Throwable {
        stackTrace = cause?.stackTrace ?: arrayOf()
        return this
    }

    override fun getStackTrace(): Array<StackTraceElement> {
        return cause?.stackTrace ?: arrayOf()
    }

    companion object {
        fun newANR(duration: Long, prefix: String, logThreadsWithoutStackTrace: Boolean): ANRError {
            val mainThread = Looper.getMainLooper().thread
            val stackTraces: MutableMap<Thread, Array<StackTraceElement>> = TreeMap(
                Comparator { lhs, rhs ->
                    if (lhs === rhs) return@Comparator 0
                    if (lhs === mainThread) return@Comparator 1
                    if (rhs === mainThread) -1 else rhs.name.compareTo(lhs.name)
                })
            // 获取所有线程的状态
            for ((key, value) in Thread.getAllStackTraces()) {
                if (key === mainThread
                    || (key.name.startsWith(prefix) && (logThreadsWithoutStackTrace || value.isNotEmpty()))
                ) {
                    stackTraces[key] = value
                }
            }

            // Sometimes main is not returned in getAllStackTraces() - ensure that we list it
            if (!stackTraces.containsKey(mainThread)) {
                stackTraces[mainThread] = mainThread.stackTrace
            }

            var at: ANRThrowable? = null
            for ((key, value) in stackTraces) {
                at = ANRThrowable(getThreadTitle(key), value)
            }
            return ANRError(at, duration)
        }

        fun newMainOnly(duration: Long): ANRError {
            val mainThread: Thread = Looper.getMainLooper().thread
            val mainStackTrace = mainThread.stackTrace
            return ANRError(ANRThrowable(getThreadTitle(mainThread), mainStackTrace), duration)
        }

        private fun getThreadTitle(thread: Thread): String {
            return thread.name + " (state = " + thread.state + ")"
        }
    }
}
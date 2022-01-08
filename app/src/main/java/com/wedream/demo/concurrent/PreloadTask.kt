package com.wedream.demo.concurrent

import kotlinx.coroutines.*
import android.util.Log
import androidx.annotation.MainThread

class PreloadTask<T> private constructor(private var block: () -> T) {

    @Volatile
    private var job: Job? = null

    @Volatile
    private var result: T? = null

    companion object {
        fun <T> of(block: () -> T): PreloadTask<T> {
            return PreloadTask(block)
        }
    }

    @MainThread
    fun start(): PreloadTask<T> {
        synchronized(this) {
            if (job != null) {
                return this
            }
            job = GlobalScope.launch(Dispatchers.IO) {
                result = block.invoke()
            }
            return this
        }
    }

    @JvmOverloads
    @MainThread
    fun get(timeout : Long = 2000): T? {
        synchronized(this) {
            val job = job ?: return block.invoke()
            return if (job.isCompleted) {
                result
            } else {
                if (job.isActive) {
                    try {
                        runBlocking {
                            withTimeout(timeout) {
                                job.join()
                            }
                        }
                    } catch (e : TimeoutCancellationException) {
                        // timeout
                        return block.invoke()
                    }
                    result
                } else {
                    block.invoke()
                }
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}
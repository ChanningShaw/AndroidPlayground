package com.wedream.demo.concurrent.kotlin

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.concurrent.PreloadTask
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.curTimeString
import kotlinx.coroutines.*

class CoroutineActivity : BaseActivity() {

    companion object {
        const val TAG = "xcm"
    }

    private var mode = CoroutineStart.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
        findViewById<Button>(R.id.launch).setOnClickListener {
            val before = System.currentTimeMillis()
            Log.e(TAG, "点击事件")
            lifecycleScope.launch(Dispatchers.Unconfined) {
                // 如果用的是Unconfined，下面两句log可能不在同一线程里打印
                Log.e(TAG, "协程开始，时间: ${System.currentTimeMillis() - before}")
                delay(500)
                log { "协程完成，时间: ${System.currentTimeMillis() - before}" }
            }

            Log.e(TAG, "主线程 sleep ，时间: ${System.currentTimeMillis() - before}")
            Thread.sleep(1000)
            Log.e(TAG, "主线程运行，时间: ${System.currentTimeMillis() - before}")
        }

        findViewById<RadioGroup>(R.id.rg_start_modes).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.start_mode_default -> {
                    mode = CoroutineStart.DEFAULT
                }
                R.id.start_mode_atomic -> {
                    mode = CoroutineStart.ATOMIC
                }
                R.id.start_mode_undispatched -> {
                    mode = CoroutineStart.UNDISPATCHED
                }
                R.id.start_mode_lazy -> {
                    mode = CoroutineStart.LAZY
                }
            }
        }

        findViewById<Button>(R.id.start_mode).setOnClickListener {
            Log.e(TAG, "点击事件开始")
            val before = System.currentTimeMillis()
            val job: Job = lifecycleScope.launch(start = mode) {
                Log.e(TAG, "协程开始运行，等待时间: ${System.currentTimeMillis() - before}")
            }

            Thread.sleep(1000L)
            // 手动启动协程，如果是lazy模式，需要手动调用start才生效
            job.start()
        }

        findViewById<Button>(R.id.async).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Unconfined) {
                log { "点击事件开始" }
                val deferred = async {
                    log { "协程开始执行" }
                    delay(1000L)
                    log { "协程执行结束" }
                    return@async 0
                }
                Log.e(TAG, "等待异步协程结束")
                val result = deferred.await()
                Log.e(TAG, "异步协程结束，结果是 $result")
            }
        }

        findViewById<Button>(R.id.runBlocking).setOnClickListener {
            Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
            runBlocking {
                Log.e(TAG, "协程阻塞1s")
                // 阻塞1s
                delay(1000L)
                Log.e(TAG, "协程结束时间: ${System.currentTimeMillis()}")
            }
            Log.e(TAG, "主线程阻塞2s")
            Thread.sleep(2000L)
            Log.e(TAG, "主线程sleep结束时间: ${System.currentTimeMillis()}")
        }

        findViewById<Button>(R.id.async_task).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val before = SystemClock.elapsedRealtime()
                val task = PreloadTask.of {
                    runBlocking {
                        delay(30)
                    }
                    0
                }.start()
                val result = task.get()
                log {
                    "async task,result = $result,  cost = ${SystemClock.elapsedRealtime() - before}"
                }
            }
        }

        findViewById<Button>(R.id.suspend_1).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                Log.e(TAG, "suspend_1 start")
                val token = getToken()
                val response = getResponse(token)
                Log.e(TAG, "suspend_1 end")
            }
            log { "suspend_1" }
        }

        findViewById<Button>(R.id.suspend_2).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Unconfined) {
                Log.e(TAG, "suspend_2 start")
                // 以下两个async会并行执行
                val token = async(Dispatchers.Unconfined) {
                    return@async getToken()
                }.await()

                val response = async(Dispatchers.Unconfined) {
                    return@async getResponse(token)
                }.await()
                Log.e(TAG, "suspend_2 end")
            }
        }

        findViewById<Button>(R.id.suspend_3).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                Log.e(TAG, "协程初始化")
                val token = getToken()
                val response = getResponse(token)
                Log.e(TAG, "协程结束")
            }
            for (i in 1..10) {
                Log.e(TAG, "主线程打印第$i 次，时间:  $curTimeString")
            }
        }

        findViewById<Button>(R.id.suspend_4).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                Log.e(TAG, "协程测试 开始执行，线程：${Thread.currentThread().name}")
                val token = async(Dispatchers.IO) {
                    return@async getToken2()
                }.await()

                val response = async(Dispatchers.IO) {
                    return@async getResponse2(token)
                }.await()

                setText(response)
            }
            Log.e(TAG, "主线程协程后面代码执行，线程：${Thread.currentThread()}")
        }

        findViewById<Button>(R.id.cancel1).setOnClickListener {
            lifecycleScope.launch {
                val job = lifecycleScope.launch {
                    repeat(1000) { i ->
                        log { "job: I'm sleeping $i ..." }
                        delay(500L)
                    }
                }
                delay(1300L) // 延迟一段时间
                log { "main: I'm tired of waiting!" }
                job.cancel() // 取消该作业，取消的含义是不会在执行新的协程，但正在执行的协程不会结束
                job.join() // 等待作业执行结束
                log { "main: Now I can quit." }
            }
        }

        /**
         * 这里，因为协程运行过程中没有检测状态，实际上无法取消
         */
        findViewById<Button>(R.id.cancel2).setOnClickListener {
            lifecycleScope.launch {
                val startTime = System.currentTimeMillis()
                val job = launch(Dispatchers.Default) {
                    var nextPrintTime = startTime
                    var i = 0
                    while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU，改成isActive即可取消
                        // 每秒打印消息两次
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            println("job: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                }
                delay(1300L) // 等待一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消一个作业并且等待它结束
                println("main: Now I can quit.")
            }
        }
    }

    private suspend fun getToken(): String {
        Log.e(TAG, "getToken start")
        delay(1000)
        Log.e(TAG, "getToken end")
        return "token"
    }

    private suspend fun getResponse(token: String): String {
        Log.e(TAG, "getResponse start")
        delay(2000)
        Log.e(TAG, "getResponse end")
        return "response"
    }

    suspend fun getToken2(): String {
        Log.e(TAG, "getToken2 start")
        delay(100)
        Log.e(TAG, "getToken2 end")
        return "ask"
    }

    suspend fun getResponse2(token: String): String {
        Log.e(TAG, "getResponse2 start，线程：${Thread.currentThread().name}")
        delay(200)
        Log.e(TAG, "getResponse2 end，线程：${Thread.currentThread().name}")
        return "response"
    }

    fun setText(response: String) {
        Log.e(TAG, "setText 执行，线程：${Thread.currentThread().name}")
    }
}
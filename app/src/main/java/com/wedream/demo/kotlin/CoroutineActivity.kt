package com.wedream.demo.kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import kotlinx.coroutines.*

class CoroutineActivity : AppCompatActivity() {

    companion object {
        const val TAG = "xcm"
    }

    private var mode = CoroutineStart.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
        findViewById<Button>(R.id.launch).setOnClickListener {
            val before = System.currentTimeMillis()
            Log.e(TAG, "协程初始化开始，时间: $before")
            GlobalScope.launch(Dispatchers.Unconfined) {
                Log.e(TAG, "协程初始化完成，时间: ${System.currentTimeMillis() - before}")
                for (i in 1..3) {
                    Log.e(TAG, "协程任务1打印第$i 次，时间: ${System.currentTimeMillis() - before}")
                }
                delay(500)
                for (i in 1..3) {
                    Log.e(TAG, "协程任务2打印第$i 次，时间: ${System.currentTimeMillis() - before}")
                }
            }

            Log.e(TAG, "主线程 sleep ，时间: ${System.currentTimeMillis() - before}")
            Thread.sleep(1000)
            Log.e(TAG, "主线程运行，时间: ${System.currentTimeMillis() - before}")

            for (i in 1..3) {
                Log.e(TAG, "主线程打印第$i 次，时间: ${System.currentTimeMillis() - before}")
            }
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
            Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
            val job: Job = GlobalScope.launch(start = mode) {
                Log.e(TAG, "协程开始运行，时间: " + System.currentTimeMillis())
            }

            Thread.sleep(1000L)
            // 手动启动协程
            job.start()
        }

        findViewById<Button>(R.id.async).setOnClickListener {
            GlobalScope.launch(Dispatchers.Unconfined) {
                Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
                val deferred = GlobalScope.async {
                    Log.e(TAG, "异步协程开始执行")
                    delay(1000L)
                    return@async System.currentTimeMillis()
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
            Log.e(TAG, "主线程slepp结束时间: ${System.currentTimeMillis()}")
        }

        findViewById<Button>(R.id.suspend_1).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
                val token = getToken()
                val response = getResponse(token)
                Log.e(TAG, "协程结束时间: ${System.currentTimeMillis()}")
            }
        }

        findViewById<Button>(R.id.suspend_2).setOnClickListener {
            GlobalScope.launch(Dispatchers.Unconfined) {
                Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
                val token = GlobalScope.async(Dispatchers.Unconfined) {
                    return@async getToken()
                }.await()

                val response = GlobalScope.async(Dispatchers.Unconfined) {
                    return@async getResponse(token)
                }.await()
                Log.e(TAG, "协程结束时间: ${System.currentTimeMillis()}")
            }
        }

        findViewById<Button>(R.id.suspend_3).setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                Log.e(TAG, "协程初始化时间: ${System.currentTimeMillis()}")
                val token = getToken()
                val response = getResponse(token)
                Log.e(TAG, "协程结束时间: ${System.currentTimeMillis()}")
            }
            for (i in 1..10) {
                Log.e(TAG, "主线程打印第$i 次，时间:  ${System.currentTimeMillis()}")
            }
        }

        findViewById<Button>(R.id.suspend_4).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                Log.e(TAG, "协程测试 开始执行，线程：${Thread.currentThread().name}")
                val token = GlobalScope.async(Dispatchers.IO) {
                    return@async getToken2()
                }.await()

                val response = GlobalScope.async(Dispatchers.IO) {
                    return@async getResponse2(token)
                }.await()

                setText(response)
            }
            Log.e(TAG, "主线程协程后面代码执行，线程：${Thread.currentThread().name}")
        }
    }

    private suspend fun getToken(): String {
        delay(100)
        Log.e(TAG, "getToken 开始执行，时间:  ${System.currentTimeMillis()}")
        return "token"
    }

    private suspend fun getResponse(token: String): String {
        delay(200)
        Log.e(TAG, "getResponse 开始执行，时间:  ${System.currentTimeMillis()}")
        return "response"
    }

    suspend fun getToken2(): String {
        Log.e(TAG, "getToken2 start，线程：${Thread.currentThread().name}")
        delay(100)
        Log.e(TAG, "getToken2 end，线程：${Thread.currentThread().name}")
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
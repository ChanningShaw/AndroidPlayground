package com.wedream.demo.concurrent.kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

class FlowActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        findViewById<Button>(R.id.flow_builder)?.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                flow {
                    for (i in 1..5) {
                        delay(100)
                        emit(i)
                    }
                }.collect {
                    Log.e("xcm", "$it")
                }
            }
        }
        findViewById<Button>(R.id.flow)?.setOnClickListener {
            flowFun()
        }
        findViewById<Button>(R.id.channel_flow)?.setOnClickListener {
            channelFlowFun()
        }
        findViewById<Button>(R.id.channel)?.setOnClickListener {
            runBlocking {
                val channel = Channel<Int>()
                launch {
                    for (x in 1..5) channel.send(x)
//                    channel.close() // we're done sending
                }
                for (y in channel) Log.e("xcm", "$y")
                Log.e("xcm", "done")
            }
        }
        /**
         * 这里创建的channel是默认的Rendezvous类型, 没有buffer, send和receive必须要meet, 否则挂起.
         * 两个producer和receiver协程都运行在同一个线程上, ready to be resumed也只是加入了一个等待队列, resume要按顺序来
         */
        findViewById<Button>(R.id.channel2)?.setOnClickListener {
            runBlocking<Unit> {
                //Rendezvous channel: 0尺寸buffer, send和receive要meet on time, 否则挂起. (默认类型).
                val channel = Channel<String>()
                launch {
                    channel.send("A1")
                    channel.send("A2")
                    Log.e("xcm", "A done")
                }
                launch {
                    channel.send("B1")
                    Log.e("xcm", "B done")
                }
                launch {
                    repeat(3) {
                        val x = channel.receive()
                        Log.e("xcm", "receive $x")
                    }
                }
            }
        }

        findViewById<Button>(R.id.channel_conflated)?.setOnClickListener {
            runBlocking<Unit> {
                val channel = Channel<String>(CONFLATED)
                GlobalScope.launch {
                    //Conflated channel: 新元素会覆盖旧元素, receiver只会得到最新元素, send永不挂起.这里会一直发送
                    channel.send("A")
                    channel.send("B")
                    Log.e("xcm", "A done")
                }
                launch {
                    channel.send("C")
                    Log.e("xcm", "B done")
                }
                launch {
                    repeat(3) {
                        val x = channel.receive()
                        Log.e("xcm", "receive $x")
                    }
                }
            }
        }

        // TODO 目前是失败的，接收不到
        findViewById<Button>(R.id.broadcast_channel)?.setOnClickListener {
            val channel = BroadcastChannel<String>(10)
            channel.offer("A")
            channel.offer("B")
            Log.e("xcm", "A done")
            channel.offer("C")
            Log.e("xcm", "B done")

            GlobalScope.launch {
                val s = channel.openSubscription().receive()
                Log.e("xcm", "receive1 $s")
            }

            channel.asFlow().onEach {
                Log.e("xcm", "receive2 $it")
            }
        }

        // 这里很难理解
        findViewById<Button>(R.id.channel_filter)?.setOnClickListener {
            getPrimes(20)
        }

        findViewById<Button>(R.id.ReceiveChannel)?.setOnClickListener {
            runBlocking {
                var channel = produce {
                    for (x in 1..5) send(x * x)
                }
                channel.consumeEach { log { it } }
                log { "Done!" }
            }
        }

        findViewById<Button>(R.id.MultiSender)?.setOnClickListener {
            runBlocking {
                val producer = produceNumbers()
                // 设置五个接受者
                repeat(5) { launchProcessor(it, producer) }
                delay(950)
                producer.cancel()
            }
        }

        findViewById<Button>(R.id.MultiSender)?.setOnClickListener {
            runBlocking {
                val channel = Channel<String>()
                launch { sendString(channel, "foo", 200L) }
                launch { sendString(channel, "BAR!", 500L) }
                repeat(6) { // receive first six
                    val e = channel.receive()
                    log { e }
                }
                coroutineContext.cancelChildren() // cancel all children to let main finish
            }
        }
    }

    private fun flowFun() = runBlocking {
        val time = measureTimeMillis {
            flow {
                for (i in 1..5) {
                    delay(100)
                    emit(i)
                }
            }.collect {
                delay(100)
                println(it)
            }
        }
        Log.e("xcm", "flowFun cost $time")
    }

    private fun channelFlowFun() = lifecycleScope.launch {
        val time = measureTimeMillis {
            channelFlow {
                for (i in 1..5) {
                    delay(100)
                    send(i)
                }
            }.collect {
                delay(100)
                println(it)
            }
        }
        Log.e("xcm", "channelFun cost $time")
    }

    private fun getPrimes(count: Int) {
        runBlocking {
            var cur = numbersFrom(2)
            repeat(count) {
                // 读出第一个，其他的继续filter
                val prime = cur.receive()
                Log.e("xcm", "prime = $prime")
                // channel已经改变了
                cur = filter(cur, prime)
            }
            coroutineContext.cancelChildren() // cancel all children to let main finish
        }
    }

    private fun flow1(): Flow<String> {
        return flow {
            repeat(10) {
                emit(it.toString())
            }
        }
    }

    private fun CoroutineScope.numbersFrom(start: Int) = produce {
        var x = start
        while (true) send(x++) // infinite stream of integers from start
    }

    /**
     * 这里是层层嵌套筛选的，
     */
    private fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
        for (x in numbers) {
            Log.e("xcm", "x = $x, prime = $prime")
            if (x % prime != 0) {
                Log.e("xcm", "send $x")
                send(x)
            }
        }
    }

    private fun CoroutineScope.produceNumbers() = produce {
        var x = 1 // start from 1
        while (true) {
            send(x++) // produce next
            delay(100) // wait 0.1s
        }
    }

    private fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        for (msg in channel) {
            log { "Processor #$id received $msg" }
        }
    }

    suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(s)
        }
    }
}
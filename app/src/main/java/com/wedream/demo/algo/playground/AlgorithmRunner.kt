package com.wedream.demo.algo.playground

import com.wedream.demo.algo.action.AlgorithmAction
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

class AlgorithmRunner {

    private val channelWarp = ChannelWrap()
    private var job: Job? = null
    private var started = false
    private var running = false

    /**
     * 启动一个算法演示
     */
    fun start(
        listener: ActionListener,
        block: suspend (ChannelWrap) -> Unit
    ): Flow<AlgorithmAction> {
        val flow = launch(block)
        GlobalScope.launch(Dispatchers.Main) {
            flow.collect {
                listener.onAction(it)
            }
        }
        return flow
    }

    private fun launch(
        block: suspend (ChannelWrap) -> Unit
    ): Flow<AlgorithmAction> {
        job = GlobalScope.launch(Dispatchers.IO) {
            block(channelWarp)
        }
        started = true
        return channelWarp.getChannel().receiveAsFlow()
    }

    fun cancel() {
        if (started) {
            job?.cancel()
            started = false
        }
    }

    fun togglePause() {
        if (started) {
            running = !running
            channelWarp.setPause(running)
        }
    }

    fun isRunning(): Boolean {
        return started && running
    }

    class ChannelWrap {
        private val channel = Channel<AlgorithmAction>()

        @Volatile
        private var pause = false
        suspend fun sendAction(action: AlgorithmAction) {
            pauseIfNeed()
            channel.send(action)
            delay(action.delayTime)
        }

        fun getChannel(): Channel<AlgorithmAction> {
            return channel
        }

        fun setPause(pause: Boolean) {
            this.pause = pause
        }

        private fun pauseIfNeed() {
            if (pause) {
                // 如果暂停，把线程挂起
                while (pause) {
                    Thread.sleep(100)
                }
            }
        }
    }
}
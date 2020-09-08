package com.wedream.demo.sort

import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.print
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AlgorithmRunner {

    private val channelWarp = ChannelWrap()
    private var job: Job? = null
    private var started = false
    private var running = false

    companion object {
        const val DELAY_TIME = 800L
    }

    fun startSort(arr: Array<Int>, algo: SortAlgorithm.Type): Flow<AlgorithmAction> {
        job = GlobalScope.launch(Dispatchers.IO) {
            SortAlgorithm.sort(arr, channelWarp, algo)
            arr.print()
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
        private var pause = false
        suspend fun sendAction(action: AlgorithmAction) {
            pauseIfNeed()
            channel.send(action)
            delay(DELAY_TIME)
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
package com.wedream.demo.sort

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AlgorithmRunner {

    private val channelWarp = ChannelWrap()

    companion object {
        const val DELAY_TIME = 800L
    }

    fun startSort(arr: Array<Int>, algo: SortAlgorithm.Type): Flow<AlgorithmAction> {
        val a = GlobalScope.launch(Dispatchers.IO) {
            SortAlgorithm.sort(arr, channelWarp, algo)
            SortAlgorithm.print(arr)
        }
        return channelWarp.getChannel().receiveAsFlow()
    }

    class ChannelWrap {
        private val channel = Channel<AlgorithmAction>()
        suspend fun sendAction(action: AlgorithmAction) {
            channel.send(action)
            delay(DELAY_TIME)
        }

        fun getChannel(): Channel<AlgorithmAction> {
            return channel
        }
    }
}
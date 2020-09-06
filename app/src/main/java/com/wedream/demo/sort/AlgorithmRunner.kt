package com.wedream.demo.sort

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AlgorithmRunner {

    private val channelWarp = ChannelWrap()

    fun startSort(arr: Array<Int>, algo: SortAlgorithm.Type): Flow<AlgorithmAction> {
        GlobalScope.launch(Dispatchers.IO) {
            SortAlgorithm.sort(arr, channelWarp, algo)
            SortAlgorithm.print(arr)
        }
        return channelWarp.getChannel().receiveAsFlow()
    }

    class ChannelWrap {
        private val channel = Channel<AlgorithmAction>()
        suspend fun sendAction(action: AlgorithmAction) {
            channel.send(action)
            delay(800)
        }

        fun getChannel(): Channel<AlgorithmAction> {
            return channel
        }
    }
}
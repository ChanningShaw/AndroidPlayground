package com.wedream.demo.sort

import android.os.HandlerThread

class AlgorithmRunner {
    private var listener: SortListener = object: SortListener{
        override fun onSwap(p1: Int, p2: Int) {
        }

        override fun onMove(from: Int, to: Int) {
        }

        override fun onFinish() {
        }

        override fun onMessage(msg: String) {
        }
    }

    fun startSort(arr: Array<Int>, algo: SortAlgorithm.Type) {
        var t = HandlerThread("algorithm_runner")
        t.start()
        Thread {
            SortAlgorithm.sort(arr, listener, algo)
            SortAlgorithm.print(arr)
        }.start()
    }

    interface SortListener {
        fun onSwap(p1: Int, p2: Int)
        fun onMove(from: Int, to: Int)
        fun onFinish()
        fun onMessage(msg: String)
    }
}
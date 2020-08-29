package com.wedream.demo.sort

import android.util.Log

object SortAlgorithm {

    const val SLEEP_TIME = 700L

    interface SortListener {
        fun onSwap(i1: Int, i2: Int)
        fun onMove(from: Int, to: Int)
        fun onFinish()
    }

    fun sort(data: Array<Int>, listener: SortListener, algorithm: SortActivity.SortAlgorithmType) {
        when (algorithm) {
            SortActivity.SortAlgorithmType.Bubble -> {
                bubbleSort(data, listener)
            }
            SortActivity.SortAlgorithmType.Select -> {
                selectSort(data, listener)
            }
            SortActivity.SortAlgorithmType.Insert -> {
                insertSort(data, listener)
            }
        }
    }

    fun bubbleSort(data: Array<Int>, listener: SortListener) {
        outer@ for (i in data.indices) {
            var hasSwap = false
            for (j in 0 until data.size - i - 1) {
                if (data[j] > data[j + 1]) {
                    listener.onSwap(j, j + 1)
                    Thread.sleep(SLEEP_TIME)
                    swap(j, j + 1, data)
                    hasSwap = true
                }
            }
            if (!hasSwap) {
                listener.onFinish()
                break@outer
            }
        }
        listener.onFinish()
    }

    /**
     * 选择排序(Selection-sort)是一种简单直观的排序算法。
     * 它的工作原理：首先在未排序序列中找到最小（大）元素，
     * 存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，
     * 然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
     */
    fun selectSort(data: Array<Int>, listener: SortListener) {
        for (i in data.indices) {
            var minIndex = i
            for (j in i + 1 until data.size) {
                if (data[j] < data[minIndex]) {
                    minIndex = j
                }
            }
            listener.onSwap(i, minIndex)
            Thread.sleep(SLEEP_TIME)
            swap(i, minIndex, data)
        }
        listener.onFinish()
    }

    fun insertSort(data: Array<Int>, listener: SortListener) {
        var current = 0
        for (i in data.indices) {
            var preIndex = i - 1;
            current = data[i]
            listener.onMove(i, -1)
            Thread.sleep(SLEEP_TIME)
            while (preIndex >= 0 && data[preIndex] > current) {
                listener.onMove(preIndex, preIndex + 1)
                Thread.sleep(SLEEP_TIME)
                data[preIndex + 1] = data[preIndex]
                preIndex--
            }
            listener.onMove(-1, preIndex + 1)
            Thread.sleep(SLEEP_TIME)
            data[preIndex + 1] = current;
        }
        listener.onFinish()
    }

    fun swap(i: Int, j: Int, data: Array<Int>) {
        val temp = data[i]
        data[i] = data[j]
        data[j] = temp
    }

    fun print(data: Array<Int>) {
        data.forEach {
            Log.e("xcm", "$it")
        }
    }
}
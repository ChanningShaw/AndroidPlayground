package com.wedream.demo.sort

import android.util.Log

object SortAlgorithm {

    const val SLEEP_TIME = 700L

    enum class Type {
        Bubble, Select, Insert, Shell, Merge
    }

    suspend fun sort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap, algorithm: Type) {
        when (algorithm) {
            Type.Bubble -> {
                bubbleSort(data, channel)
            }
            Type.Select -> {
                selectSort(data, channel)
            }
            Type.Insert -> {
                insertSort(data, channel)
            }
            Type.Shell -> {
                shellSort(data, channel)
            }
            Type.Merge -> {
                val temp = Array(data.size) { 0 }
                mergeSort(data, temp, 0, data.size - 1, channel)
            }
        }
    }

    suspend fun bubbleSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        outer@ for (i in data.indices) {
            var hasSwap = false
            for (j in 0 until data.size - i - 1) {
                if (data[j] > data[j + 1]) {
                    channel.sendAction(AlgorithmAction.SwapAction(j, j + 1))
                    swap(j, j + 1, data)
                    hasSwap = true
                }
            }
            if (!hasSwap) {
                channel.sendAction(AlgorithmAction.FinishAction)
                break@outer
            }
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    /**
     * 选择排序(Selection-sort)是一种简单直观的排序算法。
     * 它的工作原理：首先在未排序序列中找到最小（大）元素，
     * 存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，
     * 然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
     */
    suspend fun selectSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        for (i in data.indices) {
            var minIndex = i
            for (j in i + 1 until data.size) {
                if (data[j] < data[minIndex]) {
                    minIndex = j
                }
            }
            channel.sendAction(AlgorithmAction.SwapAction(i, minIndex))
            swap(i, minIndex, data)
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    suspend fun insertSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        var current: Int
        for (i in data.indices) {
            var preIndex = i - 1;
            current = data[i]
            channel.sendAction(AlgorithmAction.CopyAction(i, -1))
            while (preIndex >= 0 && data[preIndex] > current) {
                channel.sendAction(AlgorithmAction.CopyAction(preIndex, preIndex + 1))
                data[preIndex + 1] = data[preIndex]
                preIndex--
            }
            channel.sendAction(AlgorithmAction.CopyAction(-1, preIndex + 1))
            data[preIndex + 1] = current;
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    /**
     * 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
     * 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率。
     * 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位。
     */
    suspend fun shellSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        var gap = data.size / 2
        while (gap > 0) {
            channel.sendAction(AlgorithmAction.MessageAction("gap = $gap"))
            for (i in gap until data.size) {
                insertI(data, gap, i, channel)
            }
            gap /= 2
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    private suspend fun insertI(
        data: Array<Int>,
        gap: Int,
        i: Int,
        channel: AlgorithmRunner.ChannelWrap
    ) {
        val inserted = data[i]
        channel.sendAction(AlgorithmAction.CopyAction(i, -1))
        var j = i - gap
        while (j >= 0 && inserted < data[j]) {
            channel.sendAction(AlgorithmAction.CopyAction(j, j + gap))
            data[j + gap] = data[j]
            j -= gap
        }
        channel.sendAction(AlgorithmAction.CopyAction(-1, j + gap))
        data[j + gap] = inserted
    }

    suspend fun mergeSort(
        arr: Array<Int>,
        result: Array<Int>,
        start: Int,
        end: Int,
        channel: AlgorithmRunner.ChannelWrap
    ) {
        if (start >= end) return
        val len = end - start
        val mid = (len shr 1) + start
        var start1 = start
        var start2 = mid + 1
        mergeSort(arr, result, start1, mid, channel)
        mergeSort(arr, result, start2, end, channel)
        var k = start
        while (start1 <= mid && start2 <= end) {
            result[k++] = if (arr[start1] < arr[start2]) {
                channel.sendAction(AlgorithmAction.CopyAction(start1, k))
                arr[start1++]
            } else {
                channel.sendAction(AlgorithmAction.CopyAction(start2, k))
                arr[start2++]
            }
        }
        while (start1 <= mid) {
            channel.sendAction(AlgorithmAction.CopyAction(start1, k))
            result[k++] = arr[start1++]
        }
        while (start2 <= end) {
            channel.sendAction(AlgorithmAction.CopyAction(start2, k))
            result[k++] = arr[start2++]
        }
        k = start
        while (k <= end) {
            arr[k] = result[k]
            k++
        }
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
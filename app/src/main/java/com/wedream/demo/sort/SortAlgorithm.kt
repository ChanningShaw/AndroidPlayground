package com.wedream.demo.sort

import android.util.Log

object SortAlgorithm {

    const val SLEEP_TIME = 700L

    enum class Type {
        Bubble, Select, Insert, Shell, Merge
    }

    fun sort(data: Array<Int>, listener: AlgorithmRunner.SortListener, algorithm: Type) {
        when (algorithm) {
           Type.Bubble -> {
                bubbleSort(data, listener)
            }
            Type.Select -> {
                selectSort(data, listener)
            }
            Type.Insert -> {
                insertSort(data, listener)
            }
            Type.Shell -> {
                shellSort(data, listener)
            }
            Type.Merge -> {
                val temp = Array(data.size) { 0 }
                mergeSort(data, temp, 0, data.size - 1, listener)
            }
        }
    }

    fun bubbleSort(data: Array<Int>, listener: AlgorithmRunner.SortListener) {
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
    fun selectSort(data: Array<Int>, listener: AlgorithmRunner.SortListener) {
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

    fun insertSort(data: Array<Int>, listener: AlgorithmRunner.SortListener) {
        var current: Int
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

    /**
     * 希尔排序是基于插入排序的以下两点性质而提出改进方法的：
     * 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率。
     * 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位。
     */
    fun shellSort(data: Array<Int>, listener: AlgorithmRunner.SortListener) {
        var gap = data.size / 2
        while (gap > 0) {
            listener.onMessage("gap = $gap")
            for (i in gap until data.size) {
                insertI(data, gap, i, listener)
            }
            gap /= 2
        }
        listener.onFinish()
    }

    private fun insertI(data: Array<Int>, gap: Int, i: Int, listener: AlgorithmRunner.SortListener) {
        val inserted = data[i]
        listener.onMove(i, -1)
        Thread.sleep(SLEEP_TIME)
        var j = i - gap
        while (j >= 0 && inserted < data[j]) {
            listener.onMove(j, j + gap)
            Thread.sleep(SLEEP_TIME)
            data[j + gap] = data[j]
            j -= gap
        }
        listener.onMove(-1, j + gap)
        Thread.sleep(SLEEP_TIME)
        data[j + gap] = inserted
    }

    fun mergeSort(arr: Array<Int>, result: Array<Int>, start: Int, end: Int, listener: AlgorithmRunner.SortListener) {
        if (start >= end) return
        val len = end - start
        val mid = (len shr 1) + start
        var start1 = start
        var start2 = mid + 1
        mergeSort(arr, result, start1, mid, listener)
        mergeSort(arr, result, start2, end, listener)
        var k = start
        while (start1 <= mid && start2 <= end) {
            result[k++] = if (arr[start1] < arr[start2]) {
                listener.onMove(start1, k)
                Thread.sleep(SLEEP_TIME)
                arr[start1++]
            } else {
                listener.onMove(start2, k)
                Thread.sleep(SLEEP_TIME)
                arr[start2++]
            }
        }
        while (start1 <= mid) {
            listener.onMove(start1, k)
            Thread.sleep(SLEEP_TIME)
            result[k++] = arr[start1++]
        }
        while (start2 <= end) {
            listener.onMove(start2, k)
            Thread.sleep(SLEEP_TIME)
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
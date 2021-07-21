package com.wedream.demo.algo.playground

import com.wedream.demo.algo.AlgorithmRunner
import com.wedream.demo.algo.action.*


object SortPG {

    enum class Type {
        Bubble, Select, Insert, Shell, Merge, Quick
    }

    suspend fun sort(
        data: Array<Int>,
        channel: AlgorithmRunner.ChannelWrap,
        algorithm: Type
    ) {
        when (algorithm) {
            Type.Bubble -> {
                bubbleSort(
                    data,
                    channel
                )
            }
            Type.Select -> {
                selectSort(
                    data,
                    channel
                )
            }
            Type.Insert -> {
                insertSort(
                    data,
                    channel
                )
            }
            Type.Shell -> {
                shellSort(data, channel)
            }
            Type.Merge -> {
                val temp = Array(data.size) { 0 }
                mergeSort(
                    data,
                    temp,
                    0,
                    data.size - 1,
                    channel
                )
            }
            Type.Quick -> {
                quickSort(
                    data,
                    0,
                    data.size - 1,
                    channel
                )
            }
        }
    }

    suspend fun bubbleSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        outer@ for (i in data.indices) {
            var hasSwap = false
            for (j in 0 until data.size - i - 1) {
                if (data[j] > data[j + 1]) {
                    channel.sendAction(
                        SwapAction(
                            j, j + 1
                        )
                    )
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
            channel.sendAction(SwapAction(i, minIndex))
            swap(i, minIndex, data)
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    suspend fun insertSort(data: Array<Int>, channel: AlgorithmRunner.ChannelWrap) {
        var current: Int
        for (i in data.indices) {
            var preIndex = i - 1
            current = data[i]
            channel.sendAction(ExportCopyAction(i, 0, arrayOf(current)))
            while (preIndex >= 0 && data[preIndex] > current) {
                channel.sendAction(CopyAction(preIndex, preIndex + 1))
                data[preIndex + 1] = data[preIndex]
                preIndex--
            }
            channel.sendAction(ImportCopyAction(0, preIndex + 1, arrayOf(current)))
            data[preIndex + 1] = current
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
                insertI(
                    data,
                    gap,
                    i,
                    channel
                )
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
        channel.sendAction(ExportCopyAction(i, 0, arrayOf(inserted)))
        var j = i - gap
        while (j >= 0 && inserted < data[j]) {
            channel.sendAction(CopyAction(j, j + gap))
            data[j + gap] = data[j]
            j -= gap
        }
        channel.sendAction(ImportCopyAction(0, j + gap, arrayOf(inserted)))
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
        mergeSort(
            arr,
            result,
            start1,
            mid,
            channel
        )
        mergeSort(
            arr,
            result,
            start2,
            end,
            channel
        )
        channel.sendAction(AlgorithmAction.MessageAction("start = $start, end = $end"))
        var k = start
        while (start1 <= mid && start2 <= end) {
            result[k++] = if (arr[start1] < arr[start2]) {
                channel.sendAction(ExportCopyAction(start1, k, result))
                arr[start1++]
            } else {
                channel.sendAction(ExportCopyAction(start2, k, result))
                arr[start2++]
            }
        }
        while (start1 <= mid) {
            channel.sendAction(ExportCopyAction(start1, k, result))
            result[k++] = arr[start1++]
        }
        while (start2 <= end) {
            channel.sendAction(ExportCopyAction(start2, k, result))
            result[k++] = arr[start2++]
        }
        k = start
        channel.sendAction(AlgorithmAction.MessageAction("start = $start, end = $end，回填数据"))
        while (k <= end) {
            channel.sendAction(ImportCopyAction(k, k, result))
            arr[k] = result[k]
            k++
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    fun swap(i: Int, j: Int, data: Array<Int>) {
        val temp = data[i]
        data[i] = data[j]
        data[j] = temp
    }

    suspend fun quickSort(
        arr: Array<Int>,
        left: Int,
        right: Int,
        channel: AlgorithmRunner.ChannelWrap
    ) {
        if (left < right) {
            channel.sendAction(AlgorithmAction.MessageAction("left = $left, right = $right"))
            //获取中轴元素所处的位置
            val mid = partition(
                arr,
                left,
                right,
                channel
            )
            //进行分割
            quickSort(
                arr,
                left,
                mid - 1,
                channel
            )
            quickSort(
                arr,
                mid + 1,
                right,
                channel
            )
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }

    private suspend fun partition(
        arr: Array<Int>,
        left: Int,
        right: Int,
        channel: AlgorithmRunner.ChannelWrap
    ): Int {
        //选取中轴元素
        val pivot = arr[left]
        channel.sendAction(PivotAction(left))
        var i = left + 1
        var j = right
        channel.sendAction(AlgorithmAction.MessageAction("left = $left, right = $right, pivot = $pivot"))
        while (true) {
            // 向右找到第一个小于等于 pivot 的元素位置
            while (i <= j && arr[i] <= pivot) i++
            // 向左找到第一个大于等于 pivot 的元素位置
            while (i <= j && arr[j] >= pivot) j--
            if (i >= j) break
            //交换两个元素的位置，使得左边的元素不大于pivot,右边的不小于pivot
            channel.sendAction(SwapAction(i, j))
            swap(i, j, arr)
        }
        channel.sendAction(SwapAction(j, left))
        arr[left] = arr[j]
        // 使中轴元素处于有序的位置
        arr[j] = pivot
        return j
    }
}
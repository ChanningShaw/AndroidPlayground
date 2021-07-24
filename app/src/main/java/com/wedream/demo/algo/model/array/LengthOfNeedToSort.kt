package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class LengthOfNeedToSort : AlgorithmModel() {

    override var name = "需要排序的最短子数组长度"

    override var title = "给定一个无序整形数组arr，求出需要排序的最短子数组长度"

    override var tips = "分两次遍历，第一次从右到左，找第一个比最小值大的下标，" +
            "因为右边所有比最小值大的数，排序的时候都是需要换到后面去的。" +
            "第二次，从左到右遍历，找最后一个比最大值小的下标，" +
            "因为左边所有比最大值小的数，排序的时候都是需要换到前面去的。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 3, 2, 2, 2)
        val result = lengthOfNeedToSort(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun lengthOfNeedToSort(arr: IntArray): Int {
            if (arr.size < 2) {
                return 0
            }
            // 从右到左遍历，找第一个比最小值大的值
            var noMinIndex = -1
            var min = arr.last()
            for (i in arr.lastIndex downTo 0) {
                if (arr[i] > min) {
                    noMinIndex = i
                } else {
                    min = arr[i]
                }
            }
            if (noMinIndex == -1) {
                return 0
            }
            // 从左到右遍历，找最后一个比最大值小的值
            var noMaxIndex = -1
            var max = arr[0]
            for (i in arr.indices) {
                if (arr[i] < max) {
                    noMaxIndex = i
                } else {
                    max = arr[i]
                }
            }
            return noMaxIndex - noMinIndex + 1
        }
    }
}
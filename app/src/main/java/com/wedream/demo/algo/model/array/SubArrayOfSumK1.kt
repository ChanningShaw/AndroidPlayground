package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class SubArrayOfSumK1 : AlgorithmModel() {

    override var name = "累加和为给定值的最长子数组长度1"

    override var title = "给定一个数组arr，该数组无序，但每个值均为正数，在给定一个数k。" +
            "求arr的所有子数组中所有元素相加和为k的最长子数组的长度"

    override var tips = "注意以下两个特点：\n" +
            "1、无序\n" +
            "2:、都是正数\n" +
            "因为都是正数的，所以增加子数组的长度肯定会使累计和增加，" +
            "而减少子数组的长度肯定会使累计和减小。" +
            "因此可以用过两个指针start和end从头开始遍历，sum表示arr[start..end]的累积和，如果sum < k，" +
            "那就right++扩大数组范围，如果sum > k，那就left++减小数组范围。如果sum == k," +
            "记录此时的长度。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(5, 6, 1, 2, 3, 4, 3, 4, 3, 2, 1, 10, 5, 4, 1)
        val k = 10
        val result = maxSubArrayLengthOfSumK1(arr, k)
        return ExecuteResult(arr.string() + ",$k", result.string())
    }

    companion object {
        fun maxSubArrayLengthOfSumK1(arr: IntArray, k: Int): Int {
            if (arr.isEmpty() || k <= 0) {
                return 0
            }
            var start = 0
            var end = 0
            var len = 0
            var sum = arr[0]
            while (end <= arr.lastIndex) {
                when {
                    sum < k -> {
                        end++
                        if (end > arr.lastIndex) {
                            break
                        }
                        sum += arr[end]
                    }
                    sum == k -> {
                        len = max(len, end - start + 1)
                        sum -= arr[start++]
                    }
                    else -> {
                        sum -= arr[start++]
                    }
                }
            }
            return len
        }
    }
}
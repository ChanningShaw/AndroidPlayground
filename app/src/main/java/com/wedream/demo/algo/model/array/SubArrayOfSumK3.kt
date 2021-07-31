package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class SubArrayOfSumK3 : AlgorithmModel() {

    override var name = "累加和小于或者等于定值的最长子数组长度"

    override var title = "给定一个数组arr，该数组无序，其中的值可正可负可为0，在给定一个数k。" +
            "求arr的所有子数组中所有元素相加和小于或者等于K的最长子数组的长度"

    override var tips = "对于数组arr[i..n..j]，其累积和为sum，可以把其分为两段，" +
            "后面一段和<=K，那么前一段其累积和要>=sum-k，后面一段要最长，所有前面一段要最短。" +
            "所谓最短，就是累积和最早出现>=sum-k的数值的位置，假设为n，那么n..j即为所求区间。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(-1, -2, 0, 0, 0, 1, 2, 3, -2, -1, 3)
        val k = 0
        val result = maxSubArrayLengthOfSumK3(arr, k)
        return ExecuteResult(arr.string() + ",$k", result.string())
    }

    companion object {
        fun maxSubArrayLengthOfSumK3(arr: IntArray, k: Int): Int {
            if (arr.isEmpty()) {
                return 0
            }
            val h = IntArray(arr.size + 1)
            var sum = 0
            h[0] = sum
            for (i in arr.indices) {
                sum += arr[i]
                h[i + 1] = max(sum, h[i])
            }
            sum = 0
            var len = 0
            var pre = 0
            var result = 0
            for (i in arr.indices) {
                sum += arr[i]
                pre = getLessIndex(h, sum - k)
                len = if (pre == -1) {
                    0
                } else {
                    i - pre + 1
                }
                result = max(result, len)
            }
            return result
        }

        /**
         * 二分查找找到第一个大于等于num的下标
         */
        private fun getLessIndex(arr: IntArray, num: Int): Int {
            var low = 0
            var high = arr.lastIndex
            var mid: Int
            var result = -1
            while (low <= high) {
                mid = (low + high) / 2
                if (mid >= num) {
                    result = mid
                    high = mid - 1
                } else {
                    low = mid + 1
                }
            }
            return result
        }
    }
}
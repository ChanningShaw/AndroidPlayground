package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class LengthOfIntegrableSubArray : AlgorithmModel() {

    override var name = "最长可整合子数组长度"

    override var title = "如果一个数组在排序之后，每相邻每个数之间的差都为1，那么该数组为可整合数组。" +
            "给定一个整型数组arr，返回其中最大的可整合子数组长度"

    override var tips = "快速判断整合数组的方法：一个数组中如果没有重复元素，并且最大值减最小值再加1结果等于元素个数，" +
            "那么这个数组就是可整合数组。遍历所有子数组，用该方法判断即可。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(5, 3, 4, 6, 2, 8, 9)
        val result = maxLengthOfIntegrable(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun maxLengthOfIntegrable(arr: IntArray): Int {
            if (arr.isEmpty()) {
                return 0
            }
            var maxLength = 0
            val set = hashSetOf<Int>()
            for (i in arr.indices) {
                set.clear()
                var max = Int.MIN_VALUE
                var min = Int.MAX_VALUE
                // 遍历所有子数组 0..i..j..N
                for (j in i..arr.lastIndex) {
                    if (set.contains(arr[j])) {
                        break
                    }
                    set.add(arr[j])
                    max = max(max, arr[j])
                    min = min(min, arr[j])
                    if (max - min == j - i) {
                        maxLength = max(maxLength, j - i + 1)
                    }
                }
            }
            return maxLength
        }
    }
}
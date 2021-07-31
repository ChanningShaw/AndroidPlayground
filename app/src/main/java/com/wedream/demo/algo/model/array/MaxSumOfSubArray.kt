package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class MaxSumOfSubArray : AlgorithmModel() {

    override var name = "子数组的最大累积和问题"

    override var title = "给定一个数组arr，返回子数组的最大累加和"

    override var tips = "从左到右求当前的最大累积和curSum，如果curSum已经小于0，说明前面的部分已经成为了累赘，将其抛弃，" +
            "重新开始累计即可"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, -5, 3, 4)
        val input = arr.string()
        val result = adjustOddOrEven(arr)
        return ExecuteResult(input, result.string())
    }

    companion object {
        fun adjustOddOrEven(arr: IntArray): Int {
            var curSum = 0
            var maxSum = Int.MIN_VALUE
            for (i in arr) {
                curSum += i
                maxSum = max(maxSum, curSum)
                curSum = max(0, curSum)
            }
            return maxSum
        }
    }
}
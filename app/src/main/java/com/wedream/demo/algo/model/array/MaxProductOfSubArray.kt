package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class MaxProductOfSubArray : AlgorithmModel() {

    override var name = "数组中子数组的最大乘积"

    override var title = "给定一个double型的数组arr，其中元素可正可负可为0，" +
            "返回子数组的最大累乘积"

    override var tips = "有一种数组遍历方式：以某位置为结尾来遍历（通常我们以某位置为开头来遍历），" +
            "这是一种在数组中很重要的遍历方式，请务必牢记。" +
            "在这种遍历方式的情况下，整个数组的最大累乘积 P=max{以0为结尾的所有子数组的最大累乘积,..," +
            "为n-1以0为结尾的所有子数组最大累乘积}。那么以i为结尾的所有子数组的最大累乘积sum[i]怎么求呢？" +
            "假设以arr[i-1]为结尾最大累乘积为max，最小累乘积为min，sum[i]那么分为三种情况：\n" +
            "1. min * arr[i]\n" +
            "2. max * arr[i]\n" +
            "3. arr[i]"

    override fun execute(option: Option?): ExecuteResult {
        val arr = doubleArrayOf(100.0, 0.1, 100.0)
        val result = maxProductOfSubArray(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun maxProductOfSubArray(arr: DoubleArray): Double {
            if (arr.isEmpty()) {
                return 0.0
            }
            var max = arr[0]
            var min = arr[0]
            var result = arr[0]
            for (i in 1..arr.lastIndex) {
                max = max(max(max * arr[i], min * arr[i]), arr[i])
                min = min(min(max * arr[i], min * arr[i]), arr[i])
                result = max(result, max)
            }
            return result
        }
    }
}
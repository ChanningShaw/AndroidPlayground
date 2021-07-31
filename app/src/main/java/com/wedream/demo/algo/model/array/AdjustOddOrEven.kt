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

class AdjustOddOrEven : AlgorithmModel() {

    override var name = "奇数下标都是奇数或者偶数下标都是偶数"

    override var title = "给定一个长度不小于2的整型数组arr，实现一个调整函数，" +
            "使得arr要么奇数位置都是奇数，要么偶数位置都是偶数"

    override var tips = "利用最后一个元素当做temp，如果该元素为偶数，和当前偶数下标交换，偶数下标移至下一个，" +
            "否则和当前奇数下标交换，奇数下标移至下一个"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 5, 3, 4)
        val input = arr.string()
        adjustOddOrEven(arr)
        return ExecuteResult(input, arr.string())
    }

    companion object {
        fun adjustOddOrEven(arr: IntArray) {
            if (arr.size < 2) {
                return
            }
            var even = 0
            var odd = 1
            val end = arr.lastIndex
            while (even <= end && odd <= end) {
                if (arr[end] and 1 == 0) {
                    swap(arr, end, even)
                    even += 2
                } else {
                    swap(arr, end, odd)
                    odd += 2
                }
            }
        }
    }
}
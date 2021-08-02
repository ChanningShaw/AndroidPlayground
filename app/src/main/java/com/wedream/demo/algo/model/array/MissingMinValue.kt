package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.concurrent.fixedRateTimer
import kotlin.math.max
import kotlin.math.min

class MissingMinValue : AlgorithmModel() {

    override var name = "数组中未出现的最先正整数"

    override var title = "给定一个无序整型数组arr，返回数组中未出现的最小正整数"

    override var tips = "假设原来arr长度为n，那么原来数组中'可能'包含的正整数的范围只能是[1,n]，" +
            "用遍历l和r把数组分成3部分：已经包含，可能包含的和已经被排除的。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(0, 1, 2, 1, 1, 1, 0, 0, 0, 0, 2)
        val input = arr.string()
        val output = minValue(arr)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun minValue(arr: IntArray): Int {
            var l = 0
            var r = arr.size
            while (l < r) {
                if (arr[l] == l + 1) {
                    l++
                } else if (arr[l] <= l || arr[l] > r
                    || arr[arr[l] - 1] == arr[l] // arr[l] - 1 是arr[l]应该出现的问题
                ) {
                    arr[l] = arr[--r]
                } else {
                    swap(arr, l, arr[l] - 1)
                }
            }
            return l + 1
        }
    }
}
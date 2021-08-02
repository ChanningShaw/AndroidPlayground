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

class MaxGap : AlgorithmModel() {

    override var name = "数组排序之后相邻数的最大差值"

    override var title = "给定一个整形数组arr，返回排序后相邻两数的最大差值"

    override var tips = "用桶排序的思想，假设原数组有N个数，设置N+1个桶，桶的大小为(max-min)/N，" +
            "每个数落在的桶编号为(num-min)*len/(max-min)，因为N个数，N+1个桶，所以必有空桶。" +
            "所以排序后最大的相邻差值肯定是某个空桶旁边的两个数的差值，差值为后一个桶的最小值减前一个桶的最大值。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(0, 3, 6, 9)
        val input = arr.string()
        val output = maxGap(arr)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun maxGap(arr: IntArray): Int {
            if (arr.size < 2) {
                return 0
            }
            val len = arr.size
            var min = Int.MAX_VALUE
            var max = Int.MIN_VALUE
            for (i in arr) {
                min = min(min, i)
                max = max(min, i)
            }
            if (min == max) {
                return 0
            }
            val hasNum = BooleanArray(len + 1)
            val maxs = IntArray(len + 1)
            val mins = IntArray(len + 1)
            for (i in arr.indices) {
                val bid = (arr[i] - min) * len / (max - min)
                mins[bid] = if (hasNum[bid]) {
                    min(mins[bid], arr[i])
                } else {
                    arr[i]
                }
                maxs[bid] = if (hasNum[bid]) {
                    max(maxs[bid], arr[i])
                } else {
                    arr[i]
                }
                hasNum[bid] = true
            }
            var res = 0
            var lastMax = maxs[0]
            for (i in 1..len) {
                if (hasNum[i]) {
                    res = max(res, mins[i] - lastMax)
                    lastMax = maxs[i]
                }
            }
            return res
        }
    }
}
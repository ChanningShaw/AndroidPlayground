package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class SubArrayOfSumK2 : AlgorithmModel() {

    override var name = "累加和为给定值的最长子数组长度2"

    override var title = "给定一个数组arr，该数组无序，其中的值可正可负可为0，在给定一个数k。" +
            "求arr的所有子数组中所有元素相加和为k的最长子数组的长度"

    override var tips = "这里的数组元素是可正可负可为0的，增加数组的长度并不一定会使累计和增加，" +
            "所以不能再按照1中的思路来解决。" +
            ""

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(-1, -2, 0, 0, 0, 1, 2, 3, -2, -1, 3)
        val k = 0
        val result = maxSubArrayLengthOfSumK2(arr, k)
        return ExecuteResult(arr.string() + ",$k", result.string())
    }

    companion object {
        fun maxSubArrayLengthOfSumK2(arr: IntArray, k: Int): Int {
            if (arr.isEmpty()) {
                return 0
            }
            val map = hashMapOf<Int, Int>() // 保存arr[0..i]累计和最早出现的位置
            map[0] = -1
            var len = 0
            var sum = 0
            for (i in arr.indices) {
                sum += arr[i]
                if (map.containsKey(sum - k)) {
                    len = max(len, i - map[sum - k]!!)
                }
                if (!map.containsKey(sum)) {
                    map[sum] = i
                }
            }
            return len
        }
    }
}
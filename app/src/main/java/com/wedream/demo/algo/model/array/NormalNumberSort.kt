package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class NormalNumberSort : AlgorithmModel() {

    override var name = "自然数数组的排序"

    override var title = "给定一个长度为N的数组的整型数组arr，其中有N个互不相等的自然数1..N，" +
            "请实现arr的排序算法，但不要把1..N通过直接赋值的方法填上去"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 5, 3, 4)
        val input = arr.string()
        normalNumberSort1(arr)
        return ExecuteResult(input, arr.string())
    }

    companion object {
        fun normalNumberSort1(arr: IntArray) {
            var temp = 0
            for (i in arr.indices) {
                while (arr[i] != i + 1) {
                    temp = arr[i]
                    arr[i] = arr[temp - 1]
                    arr[temp - 1] = temp
                }
            }
        }
    }
}
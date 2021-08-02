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

class ArrayPartition : AlgorithmModel() {

    override var name = "数组划分"

    override var title = "原问题：调整有序数组，使左边没有重复数字且升序\n" +
            "进阶问题：给定一个数组，其中只可能含有0,1,2三个值，请实现arr的排序"

    override var tips = "原问题，设立一个指针left表示左边的区域，另一个指针i遍历数组，如果arr[left]!=arr[i]，" +
            "交换arr[left+1]和arr[i]即可\n" +
            "进阶问题，设立两个指针left和right，left初始值为0，right初始值为n-1，另一个指针i遍历数组，预到为0的放入左区，" +
            "遇到为2的放入右区，剩下的中间的自然全部为1，直到i==right为止"


    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "原问题"),
            Option(1, "进阶问题"),
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        return when (option?.id) {
            1 -> {
                val arr = intArrayOf(0, 1, 2, 1, 1, 1, 0, 0, 0, 0, 2)
                val input = arr.string()
                threePartition(arr)
                ExecuteResult(input, arr.string())
            }
            else -> {
                val arr = intArrayOf(0, 1, 1, 1, 2, 2, 2, 2, 3, 4)
                val input = arr.string()
                leftUnique(arr)
                ExecuteResult(input, arr.string())
            }
        }
    }

    companion object {
        fun leftUnique(arr: IntArray) {
            if (arr.isEmpty() || arr.size < 2) {
                return
            }
            var left = 0
            var i = 1
            while (i <= arr.lastIndex) {
                if (arr[left] != arr[i]) {
                    swap(arr, ++left, i)
                }
                i++
            }
        }

        /**
         * 把数组划分0,1,2三块
         */
        fun threePartition(arr: IntArray) {
            if (arr.isEmpty() || arr.size < 2) {
                return
            }
            var left = -1
            var right = arr.size
            var i = 0
            while (i < right) {
                when {
                    arr[i] == 0 -> {
                        swap(arr, ++left, i++)
                    }
                    arr[i] == 2 -> {
                        swap(arr, --right, i)
                    }
                    else -> {
                        i++
                    }
                }
            }
        }
    }
}
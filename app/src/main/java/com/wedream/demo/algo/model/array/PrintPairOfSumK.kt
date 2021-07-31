package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class PrintPairOfSumK : AlgorithmModel() {

    override var name = "不重复打印排序数组中和为指定值的所有二元组和三元组"

    override var title = "给定排序整型数组arr和整数k，不重复打印arr中所有相加和为k的不降序二元组和三元组"

    override var tips = "由于是排序数组，可以采用前后双指针往中间逼近的方法。" +
            "避免重复的方法是左边的元素和上一个元素不重复即可。" +
            "打印三元组时，先选定第一个元素，保证其不重复，然后在剩余的数组中按照二元组的方法打印即可"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "打印二元组"),
            Option(1, "打印三元组")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        return when (option?.id) {
            1 -> {
                val arr = intArrayOf(1, 2, 4, 5, 6, 7, 7, 8, 8, 9)
                val k = 11
                val result = printTripleOfSumK(arr, k)
                ExecuteResult(arr.string() + ",$k", result.string())
            }
            else -> {
                val arr = intArrayOf(1, 2, 4, 5, 6, 7, 7, 8, 8, 9)
                val k = 10
                val result = printPairOfSumK(arr, k)
                ExecuteResult(arr.string() + ",$k", result.string())
            }
        }

    }

    companion object {
        fun printPairOfSumK(arr: IntArray, k: Int): String {
            if (arr.size < 2) {
                return ""
            }
            val builder = StringBuilder()
            var left = 0
            var right = arr.lastIndex
            while (left < right) {
                when {
                    arr[left] + arr[right] < k -> {
                        left++
                    }
                    arr[left] + arr[right] > k -> {
                        right--
                    }
                    else -> {
                        if (left == 0 || arr[left - 1] != arr[left]) {
                            builder.append("(${arr[left]}, ${arr[right]})\n")
                        }
                        left++
                        right--
                    }
                }
            }
            return builder.toString()
        }

        fun printTripleOfSumK(arr: IntArray, k: Int): String {
            if (arr.size < 3) {
                return ""
            }
            val builder = StringBuilder()
            for (i in 0..arr.lastIndex - 2) {
                if (i == 0 || arr[i] != arr[i - 1]) {
                    printRest(builder, arr, i, k - arr[i])
                }
            }
            return builder.toString()
        }

        private fun printRest(
            builder: StringBuilder,
            arr: IntArray,
            i: Int,
            k: Int
        ) {
            var left = i + 1
            var right = arr.lastIndex
            while (left < right) {
                when {
                    arr[left] + arr[right] < k -> {
                        left++
                    }
                    arr[left] + arr[right] > k -> {
                        right--
                    }
                    else -> {
                        if (left == i + 1 || arr[left - 1] != arr[left]) {
                            builder.append("(${arr[i]}, ${arr[left]}, ${arr[right]})\n")
                        }
                        left++
                        right--
                    }
                }
            }
        }
    }
}
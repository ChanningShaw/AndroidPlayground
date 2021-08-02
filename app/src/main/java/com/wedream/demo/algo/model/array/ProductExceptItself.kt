package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.concurrent.fixedRateTimer
import kotlin.math.max
import kotlin.math.min

class ProductExceptItself : AlgorithmModel() {

    override var name = "不包含自己的累乘数组"

    override var title = "给定一个整型数组arr，返回不包含本位置值的累乘数组"

    override var tips = "方法一：使用除法，先算出所有累乘积，在逐个除以本位置的值。\n" +
            "方法二：使用左右累乘积。一遍算出左边的累乘积，在一遍算出右边的累乘积，那么某位置的累乘积等于左边的" +
            "累乘积*右边的累乘积"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 3, 4, 0)
        val result = productExceptItself2(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun productExceptItself1(arr: IntArray): IntArray {
            if (arr.isEmpty() || arr.size < 2) {
                return intArrayOf()
            }
            var count = 0
            var all = 1
            for (i in arr) {
                if (i == 0) {
                    count++
                } else {
                    all *= i
                }
            }
            val result = IntArray(arr.size)
            if (count == 0) {
                // 没有0
                for (i in arr.indices) {
                    result[i] = all / arr[i]
                }
            } else if (count == 1) {
                // 只有一个0
                for (i in arr.indices) {
                    if (arr[i] == 0) {
                        result[i] = all
                        break
                    }
                }
            }
            return result
        }

        /**
         * 不用除法，但是多做了一次乘除法
         */
        fun productExceptItself2(arr: IntArray): IntArray {
            val result = IntArray(arr.size)
            result[0] = arr[0]
            for (i in 1..arr.lastIndex) {
                // 从左到右求累乘积
                result[i] = result[i - 1] * arr[i]
            }
            var rightProduct = 1
            for (i in arr.lastIndex downTo 1) {
                result[i] = result[i - 1] * rightProduct
                rightProduct *= arr[i]
            }
            result[0] = rightProduct
            return result
        }
    }
}
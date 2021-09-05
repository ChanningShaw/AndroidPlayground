package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.pow

class MoneyCombos : AlgorithmModel() {
    override var name = "找钱问题"

    override var title = "给定一个数组arr，里面每一个值表示面额数，每一种面额数都可以使用若干张。" +
            "现在要凑齐出总额sum，请问有多少种方法。"

    override var tips = "暴力递归转动态规划"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(1, 2, 5, 20, 50, 100)
        val sum = 555
        val output = dp2(input, 555)
        return ExecuteResult(
            input.string() + ",$sum", output.toString()
        )
    }

    companion object {
        private fun recurse(arr: IntArray, index: Int, rest: Int): Int {
            if (index == arr.size) {
                return if (rest == 0) 1 else 0
            }
            var ways = 0
            var count = 0 // 每一种面值可以选的张数
            while (rest - count * arr[index] >= 0) {
                ways += recurse(arr, index + 1, rest - count * arr[index])
                count++
            }
            return ways
        }

        private fun dp1(arr: IntArray, aim: Int): Int {
            if (arr.isEmpty()) {
                return 0
            }
            /**
             * dp[index][rest] 表示arr[index..arr.size-1]范围中，还有剩下rest面额，有多少种选法
             */
            val dp = Array(arr.size + 1) {
                IntArray(aim + 1)
            }
            dp[arr.size][0] = 1 // 最后成功的条件
            for (index in arr.size - 1 downTo 0) {
                for (rest in 0..aim) {
                    var ways = 0
                    var count = 0 // 每一种面值可以选的张数
                    while (rest - count * arr[index] >= 0) {
                        ways += dp[index + 1][rest - count * arr[index]]
                        count++
                    }
                    dp[index][rest] = ways
                }
            }
            return dp[0][aim]
        }

        private fun dp2(arr: IntArray, aim: Int): Int {
            if (arr.isEmpty()) {
                return 0
            }
            /**
             * dp[index][rest] 表示arr[index..arr.size-1]范围中，还有剩下rest面额，有多少种选法
             */
            val dp = Array(arr.size + 1) {
                IntArray(aim + 1)
            }
            dp[arr.size][0] = 1 // 最后成功的条件
            for (index in arr.size - 1 downTo 0) {
                for (rest in 0..aim) {
                    var ways = dp[index + 1][rest]
                    // 斜率优化
                    if (rest - arr[index] >= 0) {
                        ways += dp[index][rest - arr[index]] // 自己本行减少一个面值的格子
                    }
                    dp[index][rest] = ways
                }
            }
            return dp[0][aim]
        }
    }
}
package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string
import kotlin.math.min

class MinCoins : AlgorithmModel() {

    override var name = "最小的硬币数"

    override var title = "给定一个数组arr，里面都是正数，表示硬币的面值。在给定一个数k，表示要达到的面额总值。" +
            "现在要从arr中选出若干个硬币出来，来凑够面额总数k，返回最小的凑够面额总数k的硬币数。"

    override var tips = "动态规划"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(1, 2, 3, 4, 5, 7, 10)
        val k = 15
        val output = minCoins3(input, k)
        return ExecuteResult(input.string(), output.string())
    }

    companion object {
        fun minCoins1(coins: IntArray, k: Int): Int {
            return process1(coins, 0, k)
        }

        fun minCoins2(coins: IntArray, k: Int): Int {
            val dp = Array(coins.size + 1) {
                IntArray(k + 1) { -2 }
            }
            return process2(coins, 0, k, dp)
        }

        fun minCoins3(coins: IntArray, k: Int): Int {
            val dp = Array(coins.size + 1) { // 生成动态规划数组
                IntArray(k + 1)
            }

            for (index in 0..coins.size) { // 边界值设定
                dp[index][0] = 0
            }
            for (rest in 1..k) { // 边界值设定
                dp[coins.size][rest] = -1
            }

            for (index in coins.size - 1 downTo 0) { // 从下往上求解，最后一行不需要求解了。
                for (rest in 1..k) { // 从左往右求解，最左一列不需要求解了。
                    var take = -1 // -1表示当前选择不可行
                    if (rest - coins[index] >= 0) {
                        take = dp[index + 1][rest - coins[index]] // 选择当前硬币的情况下获得之前的硬币数
                    }
                    val notTake = dp[index + 1][rest] // 不选择当前硬币的情况下获得之前的硬币数
                    if (take == -1 && notTake == -1) {
                        dp[index][rest] = -1
                    } else if (take == -1) {
                        dp[index][rest] = notTake
                    } else if (notTake == -1) {
                        dp[index][rest] = 1 + take
                    } else {
                        dp[index][rest] = min(take + 1, notTake)
                    }
                }
            }
            return dp[0][k]
        }

        /**
         * @return 返回从index开始选，满足rest所需要的最小硬币数
         */
        private fun process1(coins: IntArray, index: Int, rest: Int): Int {
            when {
                rest < 0 -> {
                    return -1
                }
                rest == 0 -> {
                    return 0
                }
                else -> {
                    // rest > 0
                    if (index == coins.size) {
                        return -1
                    }
                    val take = process1(coins, index + 1, rest - coins[index])
                    val notTake = process1(coins, index + 1, rest)
                    return if (take == -1 && notTake == -1) {
                        -1
                    } else if (take == -1) {
                        notTake
                    } else if (notTake == -1) {
                        1 + take
                    } else {
                        min(take + 1, notTake)
                    }
                }
            }
        }

        /**
         * @return 返回从index开始选，满足rest所需要的最小硬币数
         */
        private fun process2(coins: IntArray, index: Int, rest: Int, dp: Array<IntArray>): Int {
            when {
                rest < 0 -> {
                    return -1
                }
                dp[index][rest] != -2 -> {
                    return dp[index][rest]
                }
                rest == 0 -> {
                    dp[index][rest] = 0
                }
                else -> {
                    // rest > 0
                    if (index == coins.size) {
                        dp[index][rest] = -1
                    }
                    val take = process1(coins, index + 1, rest - coins[index])
                    val notTake = process1(coins, index + 1, rest)
                    if (take == -1 && notTake == -1) {
                        dp[index][rest] = -1
                    } else if (take == -1) {
                        dp[index][rest] = notTake
                    } else if (notTake == -1) {
                        dp[index][rest] = 1 + take
                    } else {
                        dp[index][rest] = min(take + 1, notTake)
                    }
                }
            }
            return dp[index][rest]
        }
    }
}
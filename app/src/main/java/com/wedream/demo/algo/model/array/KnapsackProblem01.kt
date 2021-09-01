package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max

class KnapsackProblem01 : AlgorithmModel() {
    override var name = "01背包问题"

    override var title = "用穷举法解01背包问题"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val weights = intArrayOf(5, 4, 3, 2, 1)
        val values = intArrayOf(10, 5, 3, 7, 3)
        val bag = 10
        val n = getMaxValue(weights, values, bag)
        return ExecuteResult(
            "weights = ${weights.string()},values=${values.string()},bag=$bag",
            n.toString()
        )
    }

    companion object {
        fun getMaxValue(weights: IntArray, values: IntArray, bag: Int): Int {
            return process(weights, values, bag, 0, "", 0, 0).totalValue
        }

        private fun process(
            weights: IntArray,
            values: IntArray,
            bag: Int,
            i: Int, // 现在挑选到第几个了
            picks: String,
            totalValue: Int,
            alreadyWeights: Int // 已经有的重量
        ): PickResult {
            if (i == weights.size) { // 已经没有可以选的了，当前的价值是0
                return PickResult(totalValue, picks)
            }
            var pickResult = PickResult(totalValue, picks)
            if (alreadyWeights + weights[i] <= bag) {
                pickResult = process(
                    weights,
                    values,
                    bag,
                    i + 1,
                    "$picks,$i",
                    totalValue + values[i],
                    alreadyWeights + weights[i]
                ) //选择了当前物品
            }
            val notPickResult =
                process(weights, values, bag, i + 1, picks, totalValue, alreadyWeights)
            return if (pickResult.totalValue > notPickResult.totalValue) {
                pickResult
            } else {
                notPickResult
            }
        }
    }

    class PickResult(val totalValue: Int, val picks: String)
}
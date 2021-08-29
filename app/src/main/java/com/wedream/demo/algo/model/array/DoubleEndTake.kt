package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*
import kotlin.math.max
import kotlin.math.min

class DoubleEndTake : AlgorithmModel() {
    override var name = "数组取数问题"

    override var title = "给定一个数组arr，玩家A和玩家B轮流从arr中取数，但只能从最左或者最右的位置取。" +
            "最终取到的所有数累计最大的玩家获胜。假设两位玩家都是绝顶聪明的，不会失误，求最后的获胜分数。"

    override var tips = "分治思想"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(1, 2, 100, 4)
        val output = takeNum(input)
        return ExecuteResult(input.string(), output.toString())
    }

    companion object {
        fun takeNum(arr: IntArray): Int {
            return max(firstTake(arr, 0, arr.lastIndex), secondTake(arr, 0, arr.lastIndex))
        }

        private fun firstTake(arr: IntArray, l: Int, r: Int): Int {
            if (l == r) {
                return arr[l]
            }
            return max(arr[l] + secondTake(arr, l + 1, r), arr[r] + secondTake(arr, l, r - 1))
        }

        private fun secondTake(arr: IntArray, l: Int, r: Int): Int {
            if (l == r) {
                return 0
            }
            return min(firstTake(arr, l + 1, r), firstTake(arr, l, r - 1))
        }
    }
}
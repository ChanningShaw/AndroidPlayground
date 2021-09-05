package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class DoubleEndTake : AlgorithmModel() {
    override var name = "数组取数问题"

    override var title = "给定一个数组arr，玩家A和玩家B轮流从arr中取数，但只能从最左或者最右的位置取。" +
            "最终取到的所有数累计最大的玩家获胜。假设两位玩家都是绝顶聪明的，不会失误，求最后的获胜分数。"

    override var tips = "暴力递归转动态规划"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(1, 2, 100, 4)
        val output = takeNum(input)
        return ExecuteResult(input.string(), output.toString())
    }

    companion object {
        fun takeNum(arr: IntArray): Int {
            return dp(arr)
        }

        private fun dp(arr: IntArray): Int {
            if (arr.isEmpty()) {
                return 0
            }
            // 以下2个矩阵i表示区间的左边，j表示区间的右边 ，i <= j
            val f = Array(arr.size) { IntArray(arr.size) } // 先手矩阵
            val s = Array(arr.size) { IntArray(arr.size) } // 后手矩阵
            for (i in f.indices) { // 初始化先手矩阵，对角线
                f[i][i] = arr[i]
            }
            var col = 1
            while (col < arr.size) {
                var i = 0
                var j = col
                while (i < arr.size && j < arr.size) { // 沿着对角线求解
                    f[i][j] = max(arr[i] + s[i + 1][j], arr[j] + s[i][j - 1])
                    s[i][j] = min(f[i + 1][j], f[i][j - 1])
                    i++
                    j++
                }
                col++
            }
            return max(f[0][arr.lastIndex], s[0][arr.lastIndex])
        }

        private fun recurse(arr: IntArray): Int {
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
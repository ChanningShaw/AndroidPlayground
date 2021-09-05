package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class HorseJump : AlgorithmModel() {
    override var name = "象棋马跳的方法数"

    override var title = "中国象棋棋盘的大小是10行9列，现在有一个马要从坐标(0,0)出发，跳到(x,y)坐标，必须跳k次的前提下，" +
            "返回总共的方法数"

    override var tips = "暴力递归转动态规划"

    override fun execute(option: Option?): ExecuteResult {
        val x = 2
        val y = 2
        val step = 4
        val output = dp(x, y, step)
        return ExecuteResult("x = $x, y = $y, step = $step", output.toString())
    }

    companion object {
        /**
         * @param x 横向距离目标还有x
         * @param y 纵向距离目标还有y
         * @param step 当前剩余的步数
         *
         * 暴力递归要正推理解，动态规划是反推理解。
         */
        private fun recurse(x: Int, y: Int, step: Int): Int {
            if (x < 0 || x > 8 || y < 0 || y > 9) { // 越界检测
                return 0
            }
            if (step == 0) {
                return if (x == 0 && y == 0) 1 else 0 // 如果没有步数可走了，而且走到了目标位置，返回1 否则返回0
            }
            return recurse(x - 2, y - 1, step - 1) +
                    recurse(x - 1, y - 2, step - 1) +
                    recurse(x + 1, y - 2, step - 1) +
                    recurse(x + 2, y - 1, step - 1) +
                    recurse(x + 2, y + 1, step - 1) +
                    recurse(x + 1, y + 2, step - 1) +
                    recurse(x - 1, y + 2, step - 1) +
                    recurse(x - 2, y + 1, step - 1)
        }

        private fun dp(x: Int, y: Int, step: Int): Int {
            // dp[x][y][step] 表示 在step步的时候能到达x，y
            val dp = Array(9) { Array(10) { IntArray(step + 1) } }
            dp[0][0][0] = 1
            for (h in 1..step) {
                for (i in 0..8) {
                    for (j in 0..9) {
                        dp[i][j][h] += getValue(dp, i - 2, j - 1, h - 1)
                        dp[i][j][h] += getValue(dp, i - 1, j - 2, h - 1)
                        dp[i][j][h] += getValue(dp, i + 1, j - 2, h - 1)
                        dp[i][j][h] += getValue(dp, i + 2, j - 1, h - 1)
                        dp[i][j][h] += getValue(dp, i + 2, j + 1, h - 1)
                        dp[i][j][h] += getValue(dp, i + 1, j + 2, h - 1)
                        dp[i][j][h] += getValue(dp, i - 1, j + 2, h - 1)
                        dp[i][j][h] += getValue(dp, i - 2, j + 1, h - 1)
                    }
                }
            }
            return dp[x][y][step]
        }

        private fun getValue(dp: Array<Array<IntArray>>, i: Int, j: Int, h: Int): Int {
            if (i < 0 || i > 8 || j < 0 || j > 9) {
                return 0
            }
            return dp[i][j][h]
        }
    }
}
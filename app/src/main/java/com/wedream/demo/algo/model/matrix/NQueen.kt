package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.abs

class NQueen : AlgorithmModel() {

    override var name = "N后问题"

    override var title = "N后问题。给定一个N*N的棋盘，在棋盘上摆放N个皇后，要求每个皇后都不同行、不同列、不同斜线。"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val n = 8
        val result = nQueueCount2(8)
        return ExecuteResult(n.toString(), result.string())
    }

    companion object {
        fun nQueueCount1(n: Int): Int {
            if (n < 1) {
                return 0
            }
            val record = IntArray(n) // record[i]表示第i的皇后放在第record[i]列
            return process(0, record, n)
        }

        private fun process(i: Int, record: IntArray, n: Int): Int {
            if (i == n) {
                return 1
            }
            var res = 0
            for (j in 0 until n) { // 逐列尝试
                if (isValid(record, i, j)) {
                    record[i] = j // 第i行的皇后放在第j列
                    res += process(i + 1, record, n)
                }
            }
            return res
        }

        /**
         * 第i行能否放在第j列
         */
        private fun isValid(record: IntArray, i: Int, j: Int): Boolean {
            for (k in 0 until i) { // 遍历之前的行
                if (j == record[k] || abs(k - i) == abs(record[k] - j)/*判断是否共斜线，斜率是否相等，即横纵坐标的差值是否相等*/) {
                    return false
                }
            }
            return true
        }

        fun nQueueCount2(n: Int): Int {
            if (n < 1 || n > 32) {
                return 0
            }
            val allPos = if (n == 32) -1 else (1 shl n) - 1 // 后n位都是1
            return process2(allPos, 0, 0, 0)
        }

        /**
         * colLim，leftDiaLim，rightDiaLim 1的位表示不能放，0表示可以放
         */
        private fun process2(
            allPos: Int,
            colLim: Int, // 列的先知
            leftDiaLim: Int, // 左对角线的限制
            rightDiaLim: Int // 右对角线的限制
        ): Int {
            if (colLim == allPos) {
                return 1 // 已经填满了n列，成功，返回1
            }
            var availPos = allPos and ((colLim or leftDiaLim or rightDiaLim).inv()) // (colLim or leftDiaLim or rightDiaLim) 整体的限制
            var res = 0
            while (availPos != 0) {
                val mostRightOne = availPos and (availPos.inv() + 1)
                availPos -= mostRightOne // 更新剩下的可以位置
                res += process2(
                    allPos,
                    colLim or mostRightOne, // 更新列限制
                    (leftDiaLim or mostRightOne) shl 1, // 更新左对角线限制
                    (rightDiaLim or mostRightOne) ushr 1 // 更新右对角线的限制
                )
            }
            return res
        }
    }
}
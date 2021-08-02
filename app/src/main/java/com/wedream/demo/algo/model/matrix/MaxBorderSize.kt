package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.MaxHeap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class MaxBorderSize : AlgorithmModel() {

    override var name = "矩阵中边界都是1的最大正方形大小"

    override var title = "给定一个N*N的方阵，在这个方阵中，只有0和1两种值，返回边框全是1的最大的子方阵的大小"

    override var tips = "用预处理方法。先算出矩阵中每一个的下边和右边有多少个连续的i，形成两个矩阵right和down。" +
            ""

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(0, 1, 1, 1, 1),
            intArrayOf(1, 1, 0, 0, 1),
            intArrayOf(0, 1, 1, 1, 1),
            intArrayOf(0, 1, 1, 1, 1),
            intArrayOf(0, 1, 0, 0, 1),
        )
        val output = getMaxBorderSize(matrix)
        return ExecuteResult(matrix.string(), output.string())
    }

    companion object {
        fun getMaxBorderSize(matrix: Array<IntArray>): Int {
            if (matrix.isEmpty() || matrix[0].isEmpty()) {
                return 0
            }
            val rightM =
                Array(matrix.size) { IntArray(matrix[0].size) } //rightM[i][j]表示matrix[i][j]从自己到右边有多少个连续的1
            val downM =
                Array(matrix.size) { IntArray(matrix[0].size) } //downM[i][j]表示matrix[i][j]从自己到下边有多少个连续的1
            preProcess(matrix, rightM, downM)
            for (size in min(matrix.size, matrix[0].size) downTo 1) {
                if (hasBorderOfSize(size, rightM, downM)) {
                    return size
                }
            }
            return 0
        }

        /**
         * 是否存在边框全是1，大小为size的方阵
         */
        private fun hasBorderOfSize(
            size: Int,
            right: Array<IntArray>,
            down: Array<IntArray>
        ): Boolean {
            val r = right.lastIndex
            val c = right[0].lastIndex
            for (i in 0..r) {
                for (j in 0..c) {
                    if (right[i][j] >= size
                        && down[i][j] >= size
                        && down[i][j + size - 1] >= size
                        && right[i + size - 1][j] >= size
                    ) {
                        return true
                    }
                }
            }
            return false
        }

        private fun preProcess(m: Array<IntArray>, right: Array<IntArray>, down: Array<IntArray>) {
            val r = m.lastIndex
            val c = m[0].lastIndex
            // 右下角的点
            if (m[r][c] == 1) {
                right[r][c] = 1
                down[r][c] = 1
            }
            // 最后一列
            for (i in r - 1 downTo 0) {
                if (m[i][c] == 1) {
                    right[i][c] = 1
                    down[i][c] = down[i + 1][c] + 1
                }
            }
            // 最后一行
            for (j in c - 1 downTo 0) {
                if (m[r][j] == 1) {
                    right[r][j] = right[r][j + 1] + 1
                    down[r][j] = 1
                }
            }
            // 其他点
            for (i in r - 1 downTo 0) {
                for (j in c - 1 downTo 0) {
                    if (m[i][j] == 1) {
                        right[i][j] = right[i][j + 1] + 1
                        down[i][j] = down[i + 1][j] + 1
                    }
                }
            }
        }
    }
}
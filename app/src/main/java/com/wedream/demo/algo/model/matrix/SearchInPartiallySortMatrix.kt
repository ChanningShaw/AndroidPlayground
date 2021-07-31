package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class SearchInPartiallySortMatrix : AlgorithmModel() {

    override var name = "在部分排序的矩阵中寻找数"

    override var title = "给定一个N * M的矩阵m和一个数k，矩阵每一行和每一列都是有序的，" +
            "实现一个函数，判断k是否在m中。"

    override var tips = "在这个矩阵中，每一个'L'都是有序的。"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(0, 1, 2, 5),
            intArrayOf(2, 3, 4, 7),
            intArrayOf(4, 4, 4, 8),
            intArrayOf(5, 7, 7, 9)
        )
        val k = 6
        val result = partiallySortMatrixContains(matrix, k)
        return ExecuteResult(matrix.string() + ", \n$k", result.string())
    }

    companion object {
        fun partiallySortMatrixContains(matrix: Array<IntArray>, k: Int): Boolean {
            if (matrix.isEmpty() || matrix[0].isEmpty()) {
                return false
            }
            var row = matrix.lastIndex
            var col = 0
            while (row >= 0 && col <= matrix[0].lastIndex) {
                when {
                    k == matrix[row][col] -> {
                        return true
                    }
                    k < matrix[row][col] -> {
                        row--
                    }
                    else -> {
                        col++
                    }
                }
            }
            return false
        }
    }
}
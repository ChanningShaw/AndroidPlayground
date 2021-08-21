package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class IslandCount : AlgorithmModel() {

    override var name = "矩阵中岛的数量"

    override var title = "给定一个二维数组matrix，其中只有0和1两种值，每个位置与其上下左右相邻。如果一个堆1可以连成一片，" +
            "这片区域叫作一个岛。返回matrix中岛的数量。"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 0, 0, 1, 1),
            intArrayOf(1, 0, 0, 0, 0),
            intArrayOf(1, 0, 0, 1, 1),
            intArrayOf(1, 0, 0, 1, 1),
        )
        val input = matrix.string()
        val output = getIslandCount(matrix)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun getIslandCount(matrix: Array<IntArray>): Int {
            if (matrix.isEmpty() || matrix[0].isEmpty()) {
                return 0
            }
            var res = 0
            for (i in matrix.indices) {
                for (j in matrix[i].indices) {
                    if (matrix[i][j] == 1) {
                        res++
                        infect(matrix, i, j)
                    }
                }
            }
            return res
        }

        fun infect(matrix: Array<IntArray>, row: Int, col: Int) {
            if (row < 0 || row > matrix.lastIndex || col < 0 || col > matrix[0].lastIndex) {
                return
            }
            if (matrix[row][col] != 1) {
                return
            }
            matrix[row][col] = 2
            infect(matrix, row, col - 1)
            infect(matrix, row - 1, col)
            infect(matrix, row, col + 1)
            infect(matrix, row + 1, col)
        }
    }
}
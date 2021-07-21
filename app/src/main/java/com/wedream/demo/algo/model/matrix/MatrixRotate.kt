package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class MatrixRotate : AlgorithmModel() {

    override var name = "将方阵顺时针转到90度"

    override var title = "给定一个N * N的方阵matrix，将其顺时针转动90度"

    override var tips = "分圈处理，每个圈用[tr, tc, dr, dc]四元组表示，每个圈共需要处理dc - tc组元素，" +
            "一个有dc - tc - 1个圈要处理"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(4, 5, 6),
            intArrayOf(7, 8, 9)
        )
        val input = matrix.string()
        circlePrint(matrix)
        return ExecuteResult(input, matrix.string())
    }

    companion object {
        fun circlePrint(matrix: Array<IntArray>) {
            if (matrix.isEmpty()) {
                return
            }
            var tr = 0
            var tc = 0
            var dr = matrix.lastIndex
            var dc = matrix[0].lastIndex
            check(dr == dc)
            while (tc < dc) {
                rotate(matrix, tr++, tc++, dr--, dc--)
            }
        }

        private fun rotate(
            matrix: Array<IntArray>,
            tr: Int, tc: Int,
            dr: Int, dc: Int,
        ) {
            var temp: Int
            for (i in 0 until dc - tc) {
                temp = matrix[tr][tc + i]
                matrix[tr][tc + i] = matrix[dr - i][tc]
                matrix[dr - i][tc] = matrix[dr][dc - i]
                matrix[dr][dc - i] = matrix[tr + i][dc]
                matrix[tr + i][dc] = temp
            }
        }
    }
}
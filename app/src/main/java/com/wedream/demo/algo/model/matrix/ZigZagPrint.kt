package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class ZigZagPrint : AlgorithmModel() {

    override var name = "之型打印矩阵"

    override var title = "给定一个矩阵matrix，按照“之”型将其打印矩阵"

    override var tips = "分圈处理，每个圈用[tr, tc, dr, dc]四元组表示，每个圈共需要处理dc - tc组元素，" +
            "一个有dc - tc - 1个圈要处理"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(5, 6, 7, 8),
            intArrayOf(9, 10, 11, 12)
        )
        val input = matrix.string()
        val output = zigZagPrint(matrix)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun zigZagPrint(matrix: Array<IntArray>) : String {
            if (matrix.isEmpty()) {
                return ""
            }
            val builder = StringBuilder()
            var tr = 0
            var tc = 0
            var dr = 0
            var dc = 0
            val endR = matrix.lastIndex
            val endC = matrix[0].lastIndex
            var bottomToUp = true
            while (dc <= endC) {
                print(matrix, tr, tc, dr, dc, bottomToUp) {
                    builder.append("$it ")
                }
                // 这里要注意顺序，将小于的判断放在后面，以防止同时修改行和列
                if (tc == endC) tr++
                if (tc < endC) tc++
                if (dr == endR) dc++
                if (dr < endR) dr++
                bottomToUp = !bottomToUp
            }
            return builder.toString()
        }

        private fun print(
            matrix: Array<IntArray>,
            tr: Int, tc: Int,
            dr: Int, dc: Int,
            bottomToUp: Boolean,
            block: (Int) -> Unit
        ) {
            if (bottomToUp) {
                var r = dr
                var c = dc
                while (r >= tr) {
                    block.invoke(matrix[r--][c++])
                }
            } else {
                var r = tr
                var c = tc
                while (r <= dr) {
                    block.invoke(matrix[r++][c--])
                }
            }
        }
    }
}
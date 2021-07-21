package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class CirclePrint : AlgorithmModel() {

    override var name = "环形打印矩阵"

    override var title = "给定一个整形的矩阵matrix，请按照顺时针的方向，按圈打印"

    override var tips = "分圈处理，每个圈用[tr, tc, dr, dc]四元组，则遍历的顺序是从tc -> dc, tr -> dr, dc -> tc, dr -> tr"

    override fun execute(option: Option?): ExecuteResult {
        val input = arrayOf(
            intArrayOf(1 ,2 ,3 ,4 ,5),
            intArrayOf(6 ,7 ,8 ,9 ,10),
            intArrayOf(11 ,12 ,13 ,14 ,15)
        )
        val output = circlePrint(input)
        return ExecuteResult(input.string(), output.string())
    }

    companion object {
        fun circlePrint(matrix: Array<IntArray>) : String {
            if (matrix.isEmpty()) {
                return ""
            }
            val builder = StringBuilder()
            var tr = 0
            var tc = 0
            var dr = matrix.lastIndex
            var dc = matrix[0].lastIndex
            while (tr <= dr && tc <= dc) {
                traversal(matrix, tr++, tc++, dr--, dc--) {
                    builder.append("$it ")
                }
            }
            return builder.toString()
        }

        private fun traversal(
            matrix: Array<IntArray>,
            tr: Int, tc: Int,
            dr: Int, dc: Int,
            block: (Int) -> Unit
        ) {
            when {
                tr == dr -> {
                    for (i in dr..dc) {
                        block.invoke(matrix[tr][i])
                    }
                }
                tc == dc -> {
                    for (i in tr..dr) {
                        block.invoke(matrix[i][tc])
                    }
                }
                else -> {
                    var cur = tc
                    while (cur < dc) {
                        block.invoke(matrix[tr][cur++])
                    }
                    cur = tr
                    while (cur < dr) {
                        block.invoke(matrix[cur++][dc])
                    }
                    cur = dc
                    while (cur > tc) {
                        block.invoke(matrix[dr][cur--])
                    }
                    cur = dr
                    while (cur > tr) {
                        block.invoke(matrix[cur--][tc])
                    }
                }
            }
        }
    }
}
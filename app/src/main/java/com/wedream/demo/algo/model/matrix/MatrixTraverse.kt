package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.Pos
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashSet

class MatrixTraverse : AlgorithmModel() {

    override var name = "矩阵遍历"

    override var title = "从给定位置开始，广度优先和深度优先遍历矩阵"

    override var tips = "广度优先，需要一个队列；深度优先需要栈，不过可以借助递归来实现。"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "深度优先遍历"),
            Option(1, "广度优先遍历")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 2, 3, 4, 5),
            intArrayOf(6, 7, 8, 9, 10),
            intArrayOf(11, 12, 13, 14, 15),
            intArrayOf(16, 17, 18, 19, 20),
        )
        when (option?.id) {
            1 -> {
                val row = 2
                val col = 2
                val builder = StringBuilder()
                breadthFirstTraverse(matrix, row, col) {
                    builder.append("$it,")
                }
                return ExecuteResult("${matrix.string()}, $row, $col", builder.toString())
            }
            else -> {
                val row = 2
                val col = 2
                val builder = StringBuilder()
                depthFirstTraverse(matrix, row, col) {
                    builder.append("$it,")
                }
                return ExecuteResult("${matrix.string()}, $row, $col", builder.toString())
            }
        }

    }

    companion object {
        fun breadthFirstTraverse(
            matrix: Array<IntArray>,
            row: Int,
            col: Int,
            block: (Int) -> Unit
        ) {
            if (matrix.isEmpty() || matrix[0].isEmpty()) {
                return
            }
            if (row < 0 || row > matrix.lastIndex || col < 0 || col > matrix[0].lastIndex) {
                return
            }
            val queue = LinkedList<Pos>()
            queue.add(Pos(row, col))
            val set = hashSetOf<Pos>()
            set.add(Pos(row, col))
            while (queue.isNotEmpty()) {
                val pos = queue.poll()!!
                block.invoke(matrix[pos.r][pos.c])
                breadthTraverse(queue, set, matrix.lastIndex, matrix[0].lastIndex, pos.r, pos.c - 1)
                breadthTraverse(queue, set, matrix.lastIndex, matrix[0].lastIndex, pos.r - 1, pos.c)
                breadthTraverse(queue, set, matrix.lastIndex, matrix[0].lastIndex, pos.r, pos.c + 1)
                breadthTraverse(queue, set, matrix.lastIndex, matrix[0].lastIndex, pos.r + 1, pos.c)
            }
        }

        fun depthFirstTraverse(
            matrix: Array<IntArray>,
            row: Int,
            col: Int,
            block: (Int) -> Unit
        ) {
            if (matrix.isEmpty() || matrix[0].isEmpty()) {
                return
            }
            val set = hashSetOf<Pos>()
            depthTraverse(matrix, set, row, col, block)
        }

        private fun depthTraverse(
            matrix: Array<IntArray>,
            set: HashSet<Pos>,
            row: Int,
            col: Int,
            block: (Int) -> Unit
        ) {
            if (row < 0 || row > matrix.lastIndex || col < 0 || col > matrix[0].lastIndex) {
                return
            }
            val p = Pos(row, col)
            if (!set.contains(p)){
                set.add(p)
                block.invoke(matrix[row][col])
                depthTraverse(matrix, set, row, col - 1, block)
                depthTraverse(matrix, set, row -1, col, block)
                depthTraverse(matrix, set, row, col + 1, block)
                depthTraverse(matrix, set, row + 1, col, block)
            }
        }

        private fun breadthTraverse(
            queue: LinkedList<Pos>,
            set: HashSet<Pos>,
            maxRow: Int,
            maxCol: Int,
            row: Int,
            col: Int
        ) {
            if (row in 0..maxRow && col in 0..maxCol) {
                val p = Pos(row, col)
                if (!set.contains(p)) {
                    queue.add(p)
                    set.add(p)
                }
            }
        }
    }
}
package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.Pos
import com.wedream.demo.util.string
import java.util.*

class ShortestPathInMatrix : AlgorithmModel() {

    override var name = "求最短路径"

    override var title = "用一个整型矩阵matrix表示一个网络，1代表有路，2代表无路，每一个位置只要不越界，" +
            "都有上下左右4个方向，求从左上角到右下角到右下角的最短路径的长度"

    override var tips = "使用宽度遍历。用队列存存放已经遍历过的点。"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 0, 1, 0, 1),
            intArrayOf(1, 0, 1, 0, 1),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 1, 1, 0, 1),
        )
        val output = shortestPath(matrix)
        return ExecuteResult(matrix.string(), output.string())
    }

    companion object {
        fun shortestPath(m: Array<IntArray>): Int {
            if (m.isEmpty()
                || m[0].isEmpty()
                || m[0][0] != 1
                || m[m.lastIndex][m[0].lastIndex] != 1
            ) {
                return 0
            }
            val map = Array(m.size) { IntArray(m[0].size) } // 记录从左上角到[i,j]位置的最短路径值
            map[0][0] = 1
            val queue = LinkedList<Pos>()
            queue.add(Pos(0, 0))
            while (queue.isNotEmpty()) {
                val p = queue.removeLast()
                val cur = map[p.r][p.c]
                if (p.r == m.lastIndex && p.c == m[0].lastIndex) {
                    return cur
                }
                walkTo(cur, p.r - 1, p.c, m, map, queue)
                walkTo(cur, p.r + 1, p.c, m, map, queue)
                walkTo(cur, p.r, p.c - 1, m, map, queue)
                walkTo(cur, p.r, p.c + 1, m, map, queue)
            }
            return 0
        }

        private fun walkTo(
            pre: Int,
            toR: Int,
            toC: Int,
            m: Array<IntArray>,
            map: Array<IntArray>,
            queue: LinkedList<Pos>
        ) {
            if (toR < 0 || toC < 0 || toR > m.lastIndex || toC > m[0].lastIndex // 越界
                || m[toR][toC] != 1 // 此路不通
                || map[toR][toC] != 0 // 已经走过了
            ) {
                return
            }
            map[toR][toC] = pre + 1
            queue.add(Pos(toR, toC))
        }
    }
}
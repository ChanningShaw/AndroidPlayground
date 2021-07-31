package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.MaxHeap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max

class TopKInMatrix : AlgorithmModel() {

    override var name = "找出N个数组整体最大的TopK"

    override var title = "有N个长度不一的数组，所有数组都是升序的，请从大到小输出这N个数组的整体最大的前K个数"

    override var tips = "借鉴子数组的最大累加和的问题，按行来遍历所有子矩阵，" +
            "每个子矩阵的和就是每一行和的和，逐行按列求累加最大值即可"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(1, 2, 4, 5),
            intArrayOf(6, 7),
            intArrayOf(-1, 3, 9)
        )
        val input = matrix.string()
        val k = 5
        val builder = StringBuilder()
        topKInMatrix(matrix, k) {
            builder.append("$it, ")
        }
        return ExecuteResult("$input,$k", builder.string())
    }

    companion object {
        fun topKInMatrix(m: Array<IntArray>, k: Int, block: (Int) -> Unit) {
            if (m.isEmpty()) {
                return
            }
            val heap = MaxHeap<HeapNode>(m.size)
            for (i in 0..m.lastIndex) {
                val index = m[i].lastIndex
                heap.push(HeapNode(m[i][index], i, index))
            }
            var i = 0
            while (i < k) {
                if (heap.size() == 0) {
                    break
                }
                val top = heap.peek()
                i++
                block.invoke(top.value)
                if (top.index > 0) {
                    heap.replace(HeapNode(m[top.row][top.index - 1], top.row, top.index - 1))
                } else {
                    heap.pop()
                }
            }
        }
    }

    private class HeapNode(val value: Int, val row: Int, val index: Int) : Comparable<HeapNode> {
        override fun compareTo(other: HeapNode): Int {
            return value.compareTo(other.value)
        }

        override fun toString(): String {
            return "$value"
        }
    }
}
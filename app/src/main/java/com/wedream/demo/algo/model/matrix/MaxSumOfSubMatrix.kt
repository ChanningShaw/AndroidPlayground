package com.wedream.demo.algo.model.matrix

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.max

class MaxSumOfSubMatrix : AlgorithmModel() {

    override var name = "子矩阵的最大累积和问题"

    override var title = "给定一个矩阵m，其中的值可正可负可为0，返回子矩阵的最大累加和"

    override var tips = "借鉴子数组的最大累加和的问题，按行来遍历所有子矩阵，" +
            "每个子矩阵的和就是每一行和的和，逐行按列求累加最大值即可"

    override fun execute(option: Option?): ExecuteResult {
        val matrix = arrayOf(
            intArrayOf(-1, 1, -1),
            intArrayOf(-1, 2, -1),
            intArrayOf(-1, -1, 1)
        )
        val input = matrix.string()
        val result = maxSumOfSubMatrix(matrix)
        return ExecuteResult(input, result.string())
    }

    companion object {
        fun maxSumOfSubMatrix(m: Array<IntArray>): Int {
            if (m.isEmpty() || m[0].isEmpty()) {
                return 0
            }
            var maxSum = 0
            for (start in 0..m.lastIndex) { // 子矩阵的范围
                val colSum = IntArray(m[start].size) // 逐行累计数组
                for (row in start..m.lastIndex) { // 当前累加到哪一行
                    var cur = 0 // 当子矩阵的累积和
                    for (i in 0..m[row].lastIndex) {
                        colSum[i] += m[row][i]
                        maxSum = max(maxSum, cur)
                        cur += colSum[i]
                        cur = max(0, cur)
                    }
                }
            }
            return maxSum
        }
    }
}
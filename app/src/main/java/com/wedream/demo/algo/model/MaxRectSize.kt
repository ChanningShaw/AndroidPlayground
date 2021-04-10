package com.wedream.demo.algo.model

import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max

class MaxRectSize : AlgorithmModel() {

    override var name = "最大子矩阵的大小"
    override var title = "给定一个整形矩阵map，其中的值只有0和1两种\n，" +
            "求其中全是1的所有矩形区域中的最大的矩形的面积"
    override var tips = "同样使用单调栈，将二维问题转化为一个数组处理：" +
            "第一行开始，尝试寻找以该行开始的最大全为1的矩形。\n" +
            "从该行开始，将每一列连续的1的数量记录下来，形成height[]数组\n," +
            "然后用栈顶到栈底从大到小的栈寻找height数组上每一个数的扩展范围n(数值不小于它的连续区域)，\n" +
            "最大子矩阵的范围就是height * n"


    override fun execute(): Pair<String, String> {
        val input = arrayOf(
            intArrayOf(1, 0, 1, 1),
            intArrayOf(1, 1, 1, 1),
            intArrayOf(1, 1, 1, 0)
        )
        return Pair(input.string(), maxRectSize(input).toString())
    }

    companion object {
        fun maxRectSize(map: Array<IntArray>): Int {
            if (map.isEmpty() || map[0].isEmpty()) {
                return 0
            }
            var maxArea = 0
            val height = IntArray(map[0].size)
            for (i in map.indices) {
                for (j in map[0].indices) {
                    height[j] = if (map[i][j] == 0) 0 else height[j] + 1
                }
                // 尝试以寻找以第i为底的最大子矩阵
                maxArea = max(maxArea, maxRectFromBottom(height))
            }
            return maxArea
        }

        private fun maxRectFromBottom(arr: IntArray): Int {
            if (arr.isEmpty()) {
                return 0
            }
            var maxArea = 0
            val maxStack = Stack<Int>()
            for (i in arr.indices) {
                while (maxStack.isNotEmpty() && arr[i] < arr[maxStack.peek()]) {
                    val right = maxStack.pop()
                    val left = if (maxStack.isEmpty()) 0 else maxStack.peek()
                    val curArea = (i - left) * arr[right]
                    maxArea = max(maxArea, curArea)
                }
                maxStack.push(i)
            }
            while (maxStack.isNotEmpty()) {
                val right = maxStack.pop()
                val left = if (maxStack.isEmpty()) 0 else maxStack.peek()
                val curArea = (arr.size - left) * arr[right]
                maxArea = max(maxArea, curArea)
            }
            return maxArea
        }
    }
}
package com.wedream.demo.algo.model

import com.wedream.demo.util.string
import java.util.*

class MonotonicStack : AlgorithmModel() {

    override var name = "单调栈结构"

    override var title = "给定一个不含重复值的数组arr，找到每一个位置i左边和右边离i位置最近且比arr[i]小的位置\n" +
            "返回所有的位置信息"

    override var tips = "使用单调栈的结构。\n" +
            "维护一个从栈顶到栈底递减的栈，每次往栈压入位置i\n" +
            "如果i对应的值比栈顶小，那么弹出栈顶，左边比栈顶其小的位置就是栈顶的下一个位置（单调递减）\n" +
            "右边比栈顶小的位置就是当前入栈位置i"

    override fun execute(): Pair<String, String> {
        val arr = intArrayOf(3, 4, 1, 5, 6, 2, 7)
        val result = getNearLess(arr)
        return Pair(arr.string(), result.string())
    }

    private fun getNearLess(arr: IntArray): Array<IntArray> {
        if (arr.isEmpty()) {
            return emptyArray()
        }
        val res = Array(arr.size) { IntArray(2) }
        val stack = Stack<Int>()
        for (i in arr.indices) {
            while (stack.isNotEmpty() && arr[i] < arr[stack.peek()]) {
                val index = stack.pop()
                val leftValue = if (stack.isEmpty()) -1 else stack.peek()
                res[index][0] = leftValue
                res[index][1] = i
            }
            stack.push(i)
        }
        while (stack.isNotEmpty()) {
            val index = stack.pop()
            val leftValue = if (stack.isEmpty()) -1 else stack.peek()
            res[index][0] = leftValue
            res[index][1] = -1
        }
        return res
    }
}
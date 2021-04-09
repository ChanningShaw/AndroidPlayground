package com.wedream.demo.algo.model

import com.wedream.demo.util.string
import java.util.*

class NextGreaterNumber : AlgorithmModel() {

    override var name = "下一个更大的数"

    override var title = "给你一个数组，返回一个等长的数组，\n" +
            "对应索引存储着下一个更大元素，如果没有更大的元素，就存 -1"

    override var tips = "使用单调栈的结构。\n" +
            "维护一个从栈顶到栈底递增的栈，每次往栈压入位置i\n" +
            "如果i对应的值比栈顶大，那么弹出栈顶, i对应的值就是栈顶位置对应的更大的数"

    override fun execute(): Pair<String, String> {
        val arr = intArrayOf(3, 4, 1, 5, 6, 2, 7)
        val result = getNextGreater(arr)
        return Pair(arr.string(), result.string())
    }

    private fun getNextGreater(arr: IntArray): IntArray {
        if (arr.isEmpty()) {
            return intArrayOf()
        }
        val res = IntArray(arr.size)
        val stack = Stack<Int>()
        for (i in arr.indices) {
            while (stack.isNotEmpty() && arr[i] > arr[stack.peek()]) {
                val index = stack.pop()
                res[index] = arr[i]
            }
            stack.push(i)
        }
        while (stack.isNotEmpty()) {
            val index = stack.pop()
            res[index] = -1
        }
        return res
    }
}
package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class RevertStackOnlyByRecur : AlgorithmModel() {
    override var name = "仅仅通过递归来逆序一个栈"

    override var title = "仅仅通过递归来逆序一个栈，不能使用任何其他的数据结构"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val stack = Stack<Int>()
        stack.push(1)
        stack.push(2)
        stack.push(3)
        val input = stack.toString()
        reverseStack(stack)
        val output = stack.toString()
        return ExecuteResult(input, output)
    }

    companion object {
        fun reverseStack(stack: Stack<Int>) {
            if (stack.isEmpty()) {
                return
            }
            val bottom = removeBottom(stack)
            reverseStack(stack)
            stack.push(bottom)
        }

        private fun removeBottom(stack: Stack<Int>): Int {
            if (stack.size == 1) {
                return stack.pop()
            }
            val top = stack.pop()
            val result = removeBottom(stack)
            stack.push(top)
            return result
        }
    }
}
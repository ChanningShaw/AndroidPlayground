package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class ExpressionSolution : AlgorithmModel() {

    override var name = "公式字符串求值"

    override var title = "给定一个字符串str，str表示一个公式，这个公式保证是正确的。公式里可能有整数、加减乘除符号和左右括号。" +
            "返回公式的计算结果。"

    override var tips = "递归求解，每对括号都是一个递归过程。递归的结果是当前值和求解到的下标"

    override fun execute(option: Option?): ExecuteResult {
        val input = "-300 + 1000 * 5 - (100 - 200)"
        val output = solve(input)
        return ExecuteResult(input.string(), output.string())
    }

    companion object {
        fun solve(exp: String): Int {
            return value(exp.toCharArray(), 0).value
        }

        /**
         * 返回值 第0位表示当前的值，第1位表示当前的下标
         */
        private fun value(chas: CharArray, index: Int): Result {
            val queue = LinkedList<String>()
            var pre = 0 // 当前的操作数
            var i = index
            while (i < chas.size && chas[i] != ')') {
                when {
                    chas[i] == ' ' -> {
                        i++
                    }
                    chas[i] in '0'..'9' -> {
                        // 数字求值
                        pre = pre * 10 + (chas[i++] - '0')
                    }
                    chas[i] == '(' -> {
                        // 左括号，求解括号里面的内容
                        val result = value(chas, i + 1)
                        pre = result.value
                        i = result.index + 1
                    }
                    else -> {
                        // 操作符，把当前值入队，并且继续入队操作符
                        addNum(queue, pre)
                        queue.addLast(chas[i++].toString())
                        pre = 0
                    }
                }
            }
            addNum(queue, pre)
            return Result(getNum(queue), i)
        }

        private fun addNum(deq: Deque<String>, num: Int) {
            var lastOp = deq.peekLast()
            if (lastOp == "*" || lastOp == "/") {
                // 如果是乘除，先算出其值
                lastOp = deq.pollLast()
                val lastNum = deq.pollLast().toInt()
                if (lastOp == "*") {
                    deq.addLast((lastNum * num).toString())
                } else {
                    deq.addLast((lastNum / num).toString())
                }
            } else {
                deq.addLast(num.toString())
            }
        }

        /**
         * 求解加减表达式
         */
        private fun getNum(deq: Deque<String>): Int {
            var res = 0
            var add = true
            while (deq.isNotEmpty()) {
                when (val cur = deq.pollFirst()) {
                    "+" -> {
                        add = true
                    }
                    "-" -> {
                        add = false
                    }
                    else -> {
                        val num = cur.toInt()
                        res += if (add) num else -num
                    }
                }
            }
            return res
        }

        private class Result(val value: Int, val index: Int)
    }
}
package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max

class BracketsValidity : AlgorithmModel() {

    override var name = "括号字符串的有效性和最长有效长度"

    override var title = "原问题：给定一个字符串str，判断是不是整体有效和括号字符串。" +
            "补充问题：给定一个括号字符串str，返回最长的有效括号字符串。"

    override var tips = "原问题：字符只能是'('或者')'，遍历过程中任何时候')'不能比'('多，遍历完'('和')'应该一样多。\n" +
            "补充问题：用动态规划。假设length[i]是str[0..i]中必须str[i]结尾的字符串最长有效长度。" +
            "那么如果str[i] == ')'，str[i - length[i-1] - 1]为'('，length[i] = length[i-1] + 2 + length[i - length[i-1] - 2]"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "原问题"),
            Option(1, "补充问题")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        return when (option?.id) {
            1 -> {
                val input = "()(())))"
                val output = maxValidLength(input)
                ExecuteResult(input.string(), output.string())
            }
            else -> {
                val input = "(((())))"
                val output = isBracketsValid(input)
                ExecuteResult(input.string(), output.string())
            }
        }
    }

    companion object {
        fun isBracketsValid(str: String): Boolean {
            if (str.isEmpty()) {
                return false
            }
            val chas = str.toCharArray()
            var leftCount = 0
            var rightCount = 0
            for (c in chas) {
                if (c != '(' && c != ')') {
                    return false
                }
                if (c == '(') {
                    leftCount++
                } else if (c == ')') {
                    rightCount++
                }
                if (rightCount > leftCount) {
                    return false
                }
            }
            return leftCount == rightCount
        }

        fun maxValidLength(str: String) : Int {
            if (str.isEmpty()) {
                return 0
            }
            val chas = str.toCharArray()
            val length = IntArray(chas.size)
            var res = 0
            for (i in 1..chas.lastIndex) {
                if (chas[i] == ')') {
                    val pre = i - length[i - 1] - 1
                    if (pre >= 0 && chas[pre] == '(') {
                        length[i] = length[i - 1] + 2
                        if (pre > 0) {
                            length[i] += length[pre - 1]
                        }
                        res = max(res, length[i])
                    }
                }
            }
            return res
        }
    }
}
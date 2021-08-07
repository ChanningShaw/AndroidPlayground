package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder

class ReplaceSpace : AlgorithmModel() {

    override var name = "字符串的替换和调整"

    override var title = "给定一个字符数组chas[]，chas右半边全是空字符，左半区不含有空字符。" +
    "现在向将左半区所有的空格字符串替换成'%20'，假设chas右半区足够大，可满足替换所需要的空间。请完成替换函数。\n" +
    "补充问题：给定一个字符类型的数组chas，其中只含有数字字符和'*'字符，现在想把所有的'*'字符挪到chas左边，" +
    "数字字符挪到chas的右边，并且保持数字的相对位置不变，请完成调整函数。"

    override var tips = "使用逆序复制。"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "原问题"),
            Option(1, "补充问题")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        when (option?.id) {
            1 -> {
                val chas = arrayOf(
                    '*', '1', '2', '3', '*', '*'
                )
                val input = chas.string()
                moveStar(chas)
                val output = chas.string()
                return ExecuteResult(input, output)
            }
            else -> {
                val strs = arrayOf(
                    'a', ' ', 'b', 'c', 'c', ' ', ' ',
                    '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'
                )
                val input = strs.string()
                replaceSpace(strs)
                val output = strs.string()
                return ExecuteResult(input, output)
            }
        }
    }

    companion object {
        fun replaceSpace(chas: Array<Char>) {
            if (chas.isEmpty()) {
                return
            }
            var sCount = 0 //空格的数量
            var len = 0 // 左半区的大小（没有空字符的半区）
            while (len < chas.size && chas[len] != '\u0000') {
                if (chas[len] == ' ') {
                    sCount++
                }
                len++
            }
            var j = len + sCount * 2 - 1
            for (i in len - 1 downTo 0) {
                if (chas[i] != ' ') {
                    // 不为空格，直接挪到后面
                    chas[j--] = chas[i]
                } else {
                    chas[j--] = '0'
                    chas[j--] = '2'
                    chas[j--] = '%'
                }
            }
        }
        fun moveStar(chas: Array<Char>) {
            if (chas.isEmpty()) {
                return
            }
            var j = chas.lastIndex // 要复制到的位置
            for (i in chas.lastIndex downTo 0) {
                if (chas[i] != '*') {
                    chas[j--] = chas[i]
                }
            }
            while (j >= 0) {
                chas[j--] = '*'
            }
        }
    }
}
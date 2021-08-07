package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder

class ReverseByWord : AlgorithmModel() {

    override var name = "以单词为单位，翻转字符串"

    override var title = "原问题：给定一个字符类型的数组chas，请在单词间做逆序调整。只要做到逆序调整单词即可，" +
            "对空格的位置没有特别要求。\n" +
            "补充问题：给定一个字符数组chas和一个整数size，要求把大小为size的左半区整体移到右半区，右半区移到左半区。"

    override var tips = "原问题：先整体翻转，然后再逐个单词翻转即可。\n" +
            "补充问题：先整体翻转，然后翻转前面[0..size-n-1]部分，在翻转后面[size-n..size-1]部分"

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
                    '1', '2', '3', '4', '5', '6', '7', 'a', 'b', 'c', 'd'
                )
                val input = chas.string()
                val k = 7
                reversePart(chas, k)
                val output = chas.string()
                return ExecuteResult("$input,$k", output)
            }
            else -> {
                val chas = arrayOf(
                    'd', 'o', 'g', ' ', 'l', 'o', 'v', 'e', 's', ' ', 'p', 'i', 'g',
                )
                val input = chas.string()
                reverseByWord(chas)
                val output = chas.string()
                return ExecuteResult(input, output)
            }
        }
    }

    companion object {
        fun reverseByWord(chas: Array<Char>) {
            if (chas.size < 2) {
                return
            }
            reverse(chas, 0, chas.lastIndex)
            var l = -1
            var r = -1
            for (i in 0..chas.lastIndex) {
                if (chas[i] != ' ') {
                    if (i == 0 || chas[i - 1] == ' ') l = i
                    if (i == chas.lastIndex || chas[i + 1] == ' ') r = i
                    if (l != -1 && r != -1) {
                        // 找到了一个单词，旋转
                        reverse(chas, l, r)
                        l = -1
                        r = -1
                    }
                }
            }
        }

        fun reversePart(chas: Array<Char>, k: Int) {
            if (chas.size < 2 || k <= 0 || k >= chas.size) {
                return
            }
            reverse(chas, 0, chas.lastIndex)
            reverse(chas, 0, chas.lastIndex - k)
            reverse(chas, chas.lastIndex - k + 1, chas.lastIndex)
        }

        fun reverse(chas: Array<Char>, left: Int, right: Int) {
            var l = left
            var r = right
            while (l < r) {
                swap(chas, l++, r--)
            }
        }
    }
}
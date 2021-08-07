package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder

class StringStatistics : AlgorithmModel() {

    override var name = "字符串的统计字符串"

    override var title = "给定一个字符串str，返回str的统计字符串。例如，'aaabbadddffc'" +
            "返回'a_2_b_2_a_1_d_3_f_2_c_1'。\n" +
            "补充问题，给定一个统计字符串cstr，在给定一个整数index，返回cstr所代表的原始字符串上的第index个字符。"

    override var tips = ""

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "原问题"),
            Option(1, "补充问题"),
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        return when (option?.id) {
            1 -> {
                val input = "a_3_b_4_c_1"
                val output = getCharOfIndex(input, 5)
                ExecuteResult(input, output.string())
            }
            else -> {
                val str = " aaabbadddffc"
                val output = getStatisticsString(str)
                ExecuteResult(str, output.string())
            }
        }
    }

    companion object {
        fun getStatisticsString(str: String): String {
            if (str.isEmpty()) {
                return ""
            }
            val chs = str.toCharArray()
            val builder = StringBuilder()
            builder.append(chs[0])
            var cur = chs[0]
            var count = 1
            for (i in 1..chs.lastIndex) {
                if (chs[i] == cur) {
                    count++
                } else {
                    cur = chs[i]
                    // 遍历到了新的字符
                    builder.append("_${count}_$cur")
                    count = 1
                }
            }
            builder.append("_$count")
            return builder.toString()
        }

        fun getCharOfIndex(statStr: String, index: Int): Char? {
            if (statStr.isEmpty() || index < 0) {
                return null
            }
            val chs = statStr.toCharArray()
            var charMode = true
            var curC = ' '
            var count = 0
            var sum = 0
            for (c in chs) {
                when {
                    c == '_' -> {
                        charMode = !charMode
                    }
                    charMode -> {
                        // 字符模式
                        sum += count // 累计上一个字符的出现次数
                        if (sum > index) {
                            return curC
                        }
                        count = 0
                        curC = c
                    }
                    else -> {
                        // 计数模式
                        count = count * 10 + (c - '0')
                    }
                }
            }
            sum += count
            return if (sum > index) curC else null
        }
    }
}
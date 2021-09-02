package com.wedream.demo.algo.model.classics

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class KMP : AlgorithmModel() {
    override var name = "查找子串位置（KMP）算法"

    override var title = "给定两个字符串str和match，长度分别为N和M，实现一个算法，如果字符串str中含有子串match，" +
            "则返回match在str中的位置，否则返回-1"

    override var tips = "核心思想：充分利用已经匹配成功的部分。" +
            "构建数组nextArr，nextArr[i]表示以match[0]开头的前缀串" +
            "和以match[i-1]为结尾的后缀串的最大匹配长度是多少。"

    override fun execute(option: Option?): ExecuteResult {
        val s = "1231231241231235"
        val m = "1231241231235"
        val output = getIndexOf(s, m)
        return ExecuteResult("$s, $m", output.toString())
    }

    companion object {
        fun getIndexOf(s: String, m: String): Int {
            if (s.length < m.length || m.isEmpty()) {
                return -1
            }
            val ss = s.toCharArray()
            val ms = m.toCharArray()
            var si = 0
            var mi = 0
            val next = getNextArray(ms)
            while (si < ss.size && mi < ms.size) {
                when {
                    ss[si] == ms[mi] -> { // 相等，一起往后移
                        si++
                        mi++
                    }
                    next[mi] == -1 -> { // m串已经回调到0位置了，s串只能往后移一个位置
                        si++
                    }
                    else -> {
                        mi = next[mi] // m串往前跳
                    }
                }
            }
            return if (mi == ms.size) si - mi else -1 // mi越界表示m串已经匹配完毕，即找到了
        }

        /**
         * 求出match数组中nextArr的位置
         */
        private fun getNextArray(ms: CharArray): IntArray {
            if (ms.size == 1) {
                return intArrayOf(-1)
            }
            val next = IntArray(ms.size)
            next[0] = -1
            next[1] = 0
            var pos = 2
            var cn = 0 // 上一个位置的匹配长度
            while (pos < next.size) {
                when {
                    ms[pos - 1] == ms[cn] -> {
                        next[pos++] = ++cn
                    }
                    cn > 0 -> {
                        // 往前跳
                        cn = next[cn]
                    }
                    else -> { // 没发前跳了，前缀为0
                        next[pos++] = 0
                    }
                }
            }
            return next
        }
    }
}
package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max

class LongestUniqueSubString : AlgorithmModel() {

    override var name = "最长无重复子串"

    override var title = "给定一个字符串str，返回str的最长无重复子串的长度"

    override var tips = "老套路，以必须以str[i]为结尾的方式来遍历str。用一个int[256]作为map来记录字符最近出现的位置。" +
            "根据以str[i-1]的结尾的最长不重复子串的长度和str[i]上一次出现的位置，即可求出当前位置的最长不重复子串的长度"


    override fun execute(option: Option?): ExecuteResult {
        val input = "adbddccabcddd"
        val output = maxUnique(input)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun maxUnique(str: String): Int {
            if (str.isEmpty()) {
                return 0
            }
            val chas = str.toCharArray()
            val map = IntArray(256) { -1 }
            var pre = -1
            var len = 0 // 累计最长长度
            for (i in chas.indices) {
                pre = max(pre, map[chas[i].code])
                val cur = i - pre // 当前的最长长度
                len = max(len, cur)
                map[chas[i].code] = i
            }
            return len
        }
    }
}
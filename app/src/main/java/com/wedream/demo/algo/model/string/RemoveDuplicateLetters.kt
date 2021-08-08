package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder

class RemoveDuplicateLetters : AlgorithmModel() {

    override var name = "删除多余的字符得到字典序最小的字符串"

    override var title = "给定一个全是小写字母的字符串str，删除多余的字符，使得每种字符值保留一个，" +
            "并让最终结果的字符串字典序最小。"

    override var tips = "统计字符出现的个数，从头遍历str，将遍历到的字符出现次数减1，如果有字符出现的次数为0了，" +
            "说明必须得挑选该字符了，不然就错过了。因为是最小字典序，在挑选它之前，如果前面有比它小的，必须得比它先挑选。"

    override fun execute(option: Option?): ExecuteResult {
        val str = "dbcacbca"
        val input = str.string()
        val output = removeDuplicateLetters(str)
        return ExecuteResult(input, output)
    }

    companion object {
        fun removeDuplicateLetters(str: String) : String {
            val chas = str.toCharArray()
            val map = IntArray(26)
            // 统计各单词出现的次数
            for (c in chas) {
                map[c - 'a']++
            }
            // 结果
            val res = CharArray(26)
            var index = 0
            var l = 0
            var r = 0
            while (r <= chas.lastIndex) {
                // -1表示不在考虑, 大于零表示后面还会出现
                if (map[str[r] - 'a'] == -1 || --map[str[r] - 'a'] > 0) {
                    r++
                } else {
                    var pick = -1
                    for (i in l..r) {
                        if (map[str[i] - 'a'] != -1 && (pick == -1 || str[i] < str[pick])) {
                            // 找最小的字符
                            pick = i
                        }
                    }
                    // 放入挑选结果中
                    res[index++] = str[pick]
                    //
                    for (i in pick + 1..r) {
                        if (map[str[i] - 'a'] != -1) {
                            map[str[i] - 'a']++
                        }
                    }
                    map[str[pick] - 'a'] = -1
                    l = pick + 1
                    r = l
                }
            }
            return String(res, 0, index)
        }
    }
}
package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class IsDeformation : AlgorithmModel() {

    override var name = "判断两个字符串是否互为变形词"

    override var title = "给定两个字符串str1和str2，如果str1和str2中出现过的字符及其数量都是一样的，" +
            "那么str1和str2就互为变形词，请实现函数判断两个字符串是否互为变形词"

    override var tips = "如果两个字符串长度不相等，直接返回false。" +
            "建立哈希表，首先遍历str1将对应字符的出现次数增加，在遍历str2将对应的字符串次数减少。" +
            "str2字符次数减少的时候如果变成负数，那么直接返回false。如果遍历完还没有一个字符变为负数，则返回true。" +
            "由于是字符串，哈希表可以用长度为256的整型数组arr来代替，arr[i]表示第i个字符的出现次数"

    override fun execute(option: Option?): ExecuteResult {
        val str1 = "1234"
        val str2 = "43215"
        val output = isDeformation(str1, str2)
        return ExecuteResult("$str1, $str2", output.string())
    }

    companion object {
        fun isDeformation(str1: String, str2: String): Boolean {
            if (str1.length != str2.length) {
                return false
            }
            val map = IntArray(256)
            val chas1 = str1.toCharArray()
            for (c in chas1) {
                map[c.code]++
            }
            val chas2 = str2.toCharArray()
            for (c in chas2) {
                if (--map[c.code] < 0) {
                    return false
                }
            }
            return true
        }
    }
}
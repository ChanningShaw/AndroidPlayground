package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max

class ConnectStrings : AlgorithmModel() {

    override var name = "拼接字符串"

    override var title = "给定一个字符串类型的数组，请找到一个拼接顺序，使得拼接后的字符串是所有拼接可能中字典顺序最小的。" +
            "并返回拼接后的字符串"

    override var tips = "将字符数组按照拼接后的排序，即字符串a和b，拼接成ab和ba来比较。算法不难，证明难。"


    override fun execute(option: Option?): ExecuteResult {
        val strs = arrayOf("ab", "ac", "b", "ba", "aa")
        val input = strs.string()
        val output = connectString(strs)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun connectString(strs: Array<String>): String {
            strs.sortWith { a, b ->
                (a + b).compareTo(b + a)
            }
            val builder = StringBuilder()
            for (s in strs) {
                builder.append(s)
            }
            return builder.toString()
        }
    }
}
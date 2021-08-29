package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class AllSubSequences : AlgorithmModel() {

    override var name = "打印字符串的所有子序列"

    override var title = "打印字符串的所有子序列，包括空串"

    override var tips = "每个位置按照要或者不要进行递归"

    override fun execute(option: Option?): ExecuteResult {
        val str = "abcd"
        val output = allSubSequences(str.toCharArray())
        return ExecuteResult(str, output.string())
    }

    companion object {
        fun allSubSequences(chas: CharArray): String {
            val builder = StringBuilder()
            process(chas, 0, builder)
            return builder.toString()
        }

        private fun process(chas: CharArray, i: Int, builder: StringBuilder) {
            if (i == chas.size) {
                builder.append(String(chas) + ",")
                return
            }
            process(chas, i + 1, builder)
            val temp = chas[i]
            chas[i] = '\u0000' // 将i位置删除
            process(chas, i + 1, builder)
            chas[i] = temp //将i位置还原
        }
    }
}
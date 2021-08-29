package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string

class AllPermutations : AlgorithmModel() {

    override var name = "打印字符串的全排列"

    override var title = "打印字符串的所有子序列，包括空串"

    override var tips = "不去重的数量：N!"

    override fun execute(option: Option?): ExecuteResult {
        val str = "abcc"
        val output = allPermutations(str.toCharArray())
        return ExecuteResult(str, output.string())
    }

    companion object {
        fun allPermutations(chas: CharArray): String {
            val builder = StringBuilder()
            process(chas, 0, builder)
            return builder.toString()
        }

        private fun process(chas: CharArray, i: Int, builder: StringBuilder) {
            if (i == chas.size) {
                builder.append(String(chas) + ",")
                return
            }
            val visit = BooleanArray(26)
            for (j in i..chas.lastIndex) {
                if (!visit[chas[j] - 'a']) { // 当前位置如果没试过才试
                    visit[chas[j] - 'a'] = true
                    swap(chas, i, j)
                    process(chas, i + 1, builder)
                    swap(chas, i, j)
                }
            }
        }
    }
}
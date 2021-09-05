package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class NumberStrToCharacterStr : AlgorithmModel() {
    override var name = "将数字字符串转成字母字符串"

    override var title = "将数字字符串转成字母字符串。规定1对应A，2对应B，...26对应Z，那么111可以转换成AAA，" +
            "也可以转换成AK，也可以转成KA。给定一个只有数字的字符串str，" +
            "返回有多少种转换结果"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val input = "111"
        val n = transferCount(input)
        return ExecuteResult(input, n.toString())
    }

    companion object {
        fun transferCount(str: String): Int {
            val chas = str.toCharArray()
            return transfer(chas, 0)
        }

        /**
         * todo 改成动态规划
         */
        private fun transfer(chas: CharArray, i: Int): Int {
            if (i == chas.size) {
                return 1
            }
            if (chas[i] == '0') { //前面的结果导致现在的位置必须以0位开头，整体无效
                return 0
            }
            if (chas[i] == '1') {
                var res = transfer(chas, i + 1) // 自己作为一种
                if (i + 1 < chas.size) {
                    res += transfer(chas, i + 2)
                }
                return res
            }
            if (chas[i] == '2') {
                var res = transfer(chas, i + 1) // 自己作为一种
                if (i + 1 < chas.size && chas[i + 1] >= '0' && chas[i + 1] <= '6') {
                    res += transfer(chas, i + 2)
                }
                return res
            }
            return transfer(chas, i +1) //其他的只能自己作为一种
        }
    }
}
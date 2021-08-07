package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string

class NumberConvert : AlgorithmModel() {

    override var name = "将整型字符串转成整数值"

    override var title = "给定一个字符串str，如果str符合日常的书写的整数形式，并且属于32位整数的范围，" +
            "返回str所代表的的整数值，否则返回0"

    override var tips = "首先判断字符串合不合法，然后从高位到地位逐位转换。因为负数的表示范围更大(-2147483648)，" +
            "所以计算过程中以负数表示，最后再根据正负取反"

    override fun execute(option: Option?): ExecuteResult {
        val str = "-2147483648"
        val output = convertToInt(str)
        return ExecuteResult(str, output.string())
    }

    companion object {
        fun convertToInt(str: String): Int {
            if (str.isEmpty()) {
                return 0
            }
            val chars = str.toCharArray()
            if (!isValid(chars)) {
                return 0
            }
            val positive = chars[0] != '-'
            val minQ = Int.MIN_VALUE / 10 // -214748364
            val minR = Int.MIN_VALUE % 10 // -8
            val start = if (positive) 0 else 1
            var num = 0
            for (i in start..chars.lastIndex) {
                val cur = '0' - chars[i] //注意这里是负的
                if ((num < minQ) || (num == minQ && cur < minR)) {
                    return 0
                }
                num = num * 10 + cur
            }
            if (positive && num == Int.MIN_VALUE) {
                return 0
            }
            return if (positive) -num else num
        }

        private fun isValid(chars: CharArray): Boolean {
            if (chars[0] == '-' && (chars.size == 1 || chars[1] == '0')) {
                return false
            }
            if (chars[0] != '-' && (chars[0] < '0' || chars[0] > '9')) {
                return false
            }
            if (chars[0] == '0' && chars.size > 1) {
                return false
            }
            for (i in 1..chars.lastIndex) {
                if (chars[i] < '0' || chars[i] > '9') {
                    return false
                }
            }
            return true
        }
    }
}
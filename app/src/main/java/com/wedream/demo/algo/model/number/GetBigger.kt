package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class GetBigger : AlgorithmModel() {

    override var name = "不做任何比较找到两个数的较大值"

    override var title = "给定两个32位的整数a和b，返回a和b中的较大值，不做任何比较"

    override var tips = "判断a-b的符号情况，要注意溢出"

    override fun execute(option: Option?): ExecuteResult {
        val a = -1
        val b = -2
        val input = "$a, $b"
        val output = getBigger2(a, b)
        return ExecuteResult(input, output.toString())
    }

    companion object {
        fun getBigger(a: Int, b: Int): Int {
            val diff = a - b
            val sa = sign(diff) // sa为1或者0
            val sb = flip(sa) // sb为0或者1
            return sa * a + sb * b
        }

        fun getBigger2(a: Int, b: Int): Int {
            val c = a - b
            val sc = sign(c) // sc为1或者0
            val sa = sign(a) // sa为0或者1
            val sb = sign(b) // sb为0或者1
            val diffSab = sa xor sb // a和b的符号不一样为1，一样为0
            val sameSab = flip(diffSab) // a和b的符号一样为1，不一样为0
            val returnA = diffSab * sa + sameSab * sc // a和b符号不同且a的符号为1 或者 a和b符号相同且sc的符号为1，乘表示且，加表示或
            val returnB = flip(returnA)
            return returnA * a + returnB * b
        }

        /**
         * 求n的符号，正数或者0返回1，负数返回0
         */
        fun sign(n: Int): Int {
            return flip((n shr 31) and 1)
        }

        fun flip(n: Int): Int {
            return n xor 1
        }
    }
}
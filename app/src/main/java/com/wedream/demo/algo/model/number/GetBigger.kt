package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class GetBigger : AlgorithmModel() {

    override var name = "不做任何比较找到两个数的较大值"

    override var title = "给定两个32位的整数a和b，返回a和b中的较大值，不做任何比较"

    override var tips = "判断a-b的符号情况"

    override fun execute(option: Option?): ExecuteResult {
        val a = -1
        val b = -2
        val input = "$a, $b"
        val output = getBigger(a, b)
        return ExecuteResult(input, output.toString())
    }

    companion object {
        fun getBigger(a: Int, b: Int): Int {
            val diff = a - b
            val sa = sign(diff) // sa为1或者0
            val sb = flip(sa) // sb为0或者1
            return sa * a + sb * b
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
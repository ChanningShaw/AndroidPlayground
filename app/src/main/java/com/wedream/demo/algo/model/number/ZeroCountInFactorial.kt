package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class ZeroCountInFactorial : AlgorithmModel() {

    override var name = "阶乘中末尾0的个数"

    override var title = "给定一个非负整数N，返回N!结果末尾为0的数量"

    override var tips = "找1-n中5的因子的个数即可，比如5有一个5，25有2个5，125有3个5"

    override fun execute(option: Option?): ExecuteResult {
        val input = Int.MAX_VALUE
        val output = zeroCount2(input)
        return ExecuteResult(input.toString(), output.toString())
    }

    companion object {
        fun zeroCount1(n: Int): Int {
            if (n < 0) {
                return 0
            }
            var res = 0
            var cur = 0 // 当前的数
            for (i in 5..n step 5) {
                cur = i
                while (cur % 5 == 0) {
                    res++
                    cur /= 5
                }
            }
            return res
        }
        fun zeroCount2(n: Int): Int {
            if (n < 0) {
                return 0
            }
            var res = 0
            var num = n
            while (num != 0) {
                res += num / 5 // 每5个自然数有就会有一个5，每25个自然数有就会有一个25
                num /= 5
            }
            return res
        }
    }
}
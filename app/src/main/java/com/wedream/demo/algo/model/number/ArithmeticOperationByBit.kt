package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class ArithmeticOperationByBit : AlgorithmModel() {

    override var name = "用位运算实现加减乘除"

    override var title = ""

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val a = -10
        val b = 5
        val sum = div(a, b)
        return ExecuteResult("$a, $b", sum.toString())
    }

    companion object {
        fun add1(a: Int, b: Int): Int {
            var op1 = a
            var carry = b
            while (carry != 0) {
                val sum = op1 xor carry // 异或相当于无进位相加
                carry = (op1 and carry) shl 1 // 进位
                op1 = sum
            }
            return op1
        }

        fun add2(a: Int, b: Int): Int {
            val sum = a xor b // 异或相当于无进位相加
            val carry = (a and b) shl 1 // 进位
            return if (carry == 0) {
                sum
            } else {
                add2(sum, carry)
            }
        }

        /**
         * 减去一个数，等于加上这个数的相反数
         */
        fun minus(a: Int, b: Int): Int {
            return add1(a, negNum(b))
        }

        fun mul(a: Int, b: Int): Int {
            var res = 0
            var op1 = a
            var op2 = b
            while (op2 != 0) {
                if ((op2 and 1) != 0) {
                    res = add1(res, op1)
                }
                op2 = op2 ushr 1 // 乘数右移
                op1 = op1 shl 1 // 被乘数左移
            }
            return res
        }

        fun div(a: Int, b: Int): Int {
            var x = if (a < 0)  negNum(a) else a // 如果是负数，先变成相反数
            val y = if (b < 0)  negNum(b) else b
            var res = 0
            for (i in 31 downTo 0) {
                if ((x shr i) >= y) {
                    res += (1 shl i)
                    x = minus(x, y shl i)
                }
            }
            return if ((a < 0) xor (b < 0)) negNum(res) else res // 如果a和b的符号不相同，需要再次取反
        }

        /**
         * 求一个数的相反数，取反加1
         */
        fun negNum(n: Int): Int {
            return add1(n.inv(), 1)
        }
    }
}
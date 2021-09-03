package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class OneCount : AlgorithmModel() {

    override var name = "整数二进制数表达中1个数量"

    override var title = "给定一个32位整数n，可正可负可为0，返回该整数二进制数表达中1个数量"

    override var tips = "方法一：无符号右移，每次取出第1位来判断是不是1."

    override fun execute(option: Option?): ExecuteResult {
        val n = -1
        return ExecuteResult(n.toString(), countOne4(n).toString())
    }

    companion object {
        fun countOne1(n: Int): Int {
            var res = 0
            var n = n
            while (n != 0) {
                res += (n and 1) // 与1就是取出第1位
                n = n ushr 1
            }
            return res
        }

        fun countOne2(n: Int): Int {
            var res = 0
            var n = n
            while (n != 0) {
                n = n and (n - 1) // 干掉最右边的1
                res++
            }
            return res
        }

        fun countOne3(n: Int): Int {
            var res = 0
            var n = n
            while (n != 0) {
                n -= n and (n.inv() + 1) // 干掉最右边的1
                res++
            }
            return res
        }

        fun countOne4(n: Int): Int {
            var n = n
            n = (n and 0x55555555) + ((n ushr 1) and 0x55555555)
            n = (n and 0x33333333) + ((n ushr 2) and 0x33333333)
            n = (n and 0x0f0f0f0f) + ((n ushr 4) and 0x0f0f0f0f)
            n = (n and 0x00ff00ff) + ((n ushr 8) and 0x00ff00ff)
            n = (n and 0x0000ffff) + ((n ushr 16) and 0x0000ffff)
            return n
        }
    }
}
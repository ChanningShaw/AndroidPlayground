package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class OnceNum : AlgorithmModel() {

    override var name = "在其他数都出现K次的数组中找到只出现一次的数"

    override var title = "给定一个整型数组arr和一个大于1的整数k，已知arr中只有1个数出现了1次，其他数都出现了k次，" +
            "请返回只出现了一次的数"

    override var tips = "k个相同的k进制数无进位相加结果为0。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(100, 100, 100, 66, 66, 66, 10, 10, 10, 1)
        val k = 3
        val output = getOnceNum(arr, k)
        return ExecuteResult("${arr.string()}, $k", output.toString())
    }

    companion object {
        fun getOnceNum(arr: IntArray, k: Int): Int {
            val e0 = IntArray(32)
            for (i in arr) {
                addKSysNum(e0, i, k)
            }
            return transKSysNumToNum(e0, k) // 转换回十进制
        }

        fun addKSysNum(e0: IntArray, value: Int, k: Int) {
            val curKSysNum = transNumToKSysNum(value, k)
            for (i in e0.indices) {
                e0[i] = (e0[i] + curKSysNum[i]) % k // 无进位相加
            }
        }

        /**
         * 将十进制数转换成k进制数
         */
        fun transNumToKSysNum(value: Int, k: Int) : IntArray {
            val res = IntArray(32)
            var i = 0
            var value = value
            while (value != 0) {
                res[i++] = value % k
                value /= k
            }
            return res
        }

        /**
         * 将k进制数转换成十进制数
         *
         * e0数组 低位 ---------> 高位
         */
        fun transKSysNumToNum(e0: IntArray, k: Int): Int {
            var res = 0
            for (i in e0.lastIndex downTo 0) {
                res = res * k + e0[i]
            }
            return res
        }
    }
}
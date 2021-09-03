package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class IsPowerOf : AlgorithmModel() {

    override var name = "判断一个数是否是2或者4的某个次幂"

    override var title = "判断一个数是否是2或者4的某个次幂"

    override var tips = "如果n是2的某个次幂，2只有一个1。如果n是4的某个次幂，那么它首先是2的某个次幂，然后它的1只能出现在偶数位。"

    override fun execute(option: Option?): ExecuteResult {
        val n = -1
        return ExecuteResult(n.toString(), isPowerOf2(n).toString())
    }

    companion object {
        fun isPowerOf2(n: Int): Boolean {
            return n and (n - 1) == 0
        }
        fun isPowerOf4(n: Int): Boolean {
            return isPowerOf2(n) && (n and 0x55555555) != 0
        }
    }
}
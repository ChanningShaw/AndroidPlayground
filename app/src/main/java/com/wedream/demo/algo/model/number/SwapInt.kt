package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class SwapInt : AlgorithmModel() {

    override var name = "不用额外遍历交换两个整数的值"

    override var title = "不用额外遍历交换两个整数的值"

    override var tips = "使用异或运算，异或运算可以记录两个整数所有不同的位"

    override fun execute(option: Option?): ExecuteResult {
        var a = 0
        var b = 1
        val input = "$a, $b"
        a = a xor b
        b = a xor b
        a = a xor b
        val output = "$a, $b"
        return ExecuteResult(input, output)
    }
}
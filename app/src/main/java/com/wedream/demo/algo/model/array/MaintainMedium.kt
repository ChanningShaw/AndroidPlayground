package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class MaintainMedium : AlgorithmModel() {

    override var name = "随时获得中位数"

    override var title = "给定一个数据流，可以随时获得中位数"

    override var tips = "对于输入的数字流，如果我们总是可以维持集合较大部分和较小部分，并且让这两部分的数字个数相差不超过1，" +
            "那么我们可以随时获得中位数。" +
            "中位数就是较大部分的最小值或者最小部分的最大值。所以我们可以使用小顶堆和大顶堆的组合来实现。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 2, 2, 2, 2)
        val result = maintainMedium(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun maintainMedium(arr: IntArray): IntArray {
            return intArrayOf()
        }
    }
}
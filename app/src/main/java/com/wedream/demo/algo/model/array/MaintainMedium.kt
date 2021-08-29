package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class MaintainMedium : AlgorithmModel() {

    override var name = "随时获得中位数"

    override var title = "给定一个数据流，可以随时获得中位数"

    override var tips = "对于输入的数字流，如果我们总是可以维持集合较大部分和较小部分，并且让这两部分的数字个数相差不超过1，" +
            "那么我们可以随时获得中位数。" +
            "中位数就是较大部分的最小值或者最小部分的最大值。所以我们可以使用小顶堆和大顶堆的组合来实现。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 3, 6, 2, 8, 1)
        val result = maintainMedium(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun maintainMedium(arr: IntArray): IntArray {
            if (arr.isEmpty()) {
                return intArrayOf()
            }
            val result = IntArray(arr.size)
            val smallPart = PriorityQueue<Int> { i1, i2 -> // 大顶堆
                i2 - i1
            }
            val bigPart = PriorityQueue<Int> { i1, i2 -> // 小顶堆
                i1 - i2
            }
            for (i in arr.indices) {
                if (smallPart.isEmpty() || i < smallPart.peek()) {
                    smallPart.offer(arr[i])
                    if (smallPart.size - bigPart.size > 1) {
                        bigPart.offer(smallPart.poll())
                    }
                } else {
                    bigPart.offer(arr[i])
                    if (bigPart.size - smallPart.size > 1) {
                        smallPart.offer(bigPart.poll())
                    }
                }
                // 生成中位数
                when {
                    bigPart.size > smallPart.size -> {
                        result[i] = bigPart.peek()
                    }
                    smallPart.size > bigPart.size -> {
                        result[i] = smallPart.peek()
                    }
                    else -> {
                        result[i] = (smallPart.peek() + bigPart.peek()) / 2
                    }
                }
            }
            return result
        }
    }
}
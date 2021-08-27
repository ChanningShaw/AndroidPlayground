package com.wedream.demo.algo.model.classics

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class Huffman : AlgorithmModel() {
    override var name = "分金条的最小花费"

    override var title = "给定一个正整数数组arr，arr的累加和表示金条的长度，arr的每个数表示金条要分成的长度。" +
            "规定长度的k的金条分成2块所需要的费用是k，返回把金条分出arr中的每个数字需要的最小花费"

    override var tips = "哈夫曼编码，并采用逆推的方法。中心思想是每次尽量把金条分成相对均匀的两部分，这样每一份相对比较小些。" +
            "把所有数字放入最小堆，每次弹出两个，并将其和再次压入。直到剩下的一个数字，便是总体的花费。"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(3, 9, 5, 2, 4, 4)
        val output = getMinSplitCost(input)
        return ExecuteResult(input.string(), output.toString())
    }

    companion object {
        fun getMinSplitCost(arr: IntArray): Int {
            if (arr.size < 2) {
                return 0
            }
            val minHeap = PriorityQueue<Int>()
            for (i in arr) {
                
                minHeap.add(i)
            }
            while (minHeap.size > 1) {
                val sum = minHeap.poll() + minHeap.poll()
                minHeap.add(sum)
            }
            return minHeap.peek()
        }
    }
}
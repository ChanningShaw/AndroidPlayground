package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class MaxProfit : AlgorithmModel() {

    override var name = "做项目的最大收益问题"

    override var title = "给定两个正整数w和k表示现在拥有的出事资金和可以做的项目数，在给定两个数组costs和profits，" +
            "表示第i的项目的成本和利润，只有手头的资金大于等于项目的成本才能做该项目，项目只能串行地做，请返回最后可以获得的最大资金"

    override var tips = "成本越低越好，利润越大越好。建立一个成本小顶堆，用于存放所有的项目。" +
            "每次从中获取所有能够启动的项目，并加入到利润大顶堆中，然后每次从利润大顶堆中获取一个项目来启动。" +
            "直到已经做了k个项目或者利润大顶堆为空为止。大顶堆和小顶堆可以用PriorityQueue来实现。"

    override fun execute(option: Option?): ExecuteResult {
        val costs = intArrayOf(5, 4, 1, 2)
        val profits = intArrayOf(3, 5, 3, 2)
        val w = 3
        val k = 2
        val input = "costs = ${costs.string()}\nprofits = ${profits.string()}\n w = $w, k = $k"
        val output = maxProfits(3, 2, costs, profits)
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun maxProfits(w: Int, k: Int, costs: IntArray, profits: IntArray): Int {
            if (w < 1 || k < 0 || costs.isEmpty() || costs.size != profits.size) {
                return w
            }
            val costMinHeap = PriorityQueue<Program> { o1, o2 ->
                o1.cost.compareTo(o2.cost)
            }
            val profitMaxHeap = PriorityQueue<Program> { o1, o2 ->
                o2.profit.compareTo(o1.profit)
            }
            for (i in costs.indices) {
                costMinHeap.add(Program(costs[i], profits[i]))
            }
            var cw = w
            for (i in 1..k) {
                // 把所有可以做的项目都添加到利润最大堆里
                while (costMinHeap.isNotEmpty() && costMinHeap.peek().cost <= cw) {
                    profitMaxHeap.add(costMinHeap.poll())
                }
                if (profitMaxHeap.isEmpty()) {
                    // 如果为空，说明没有项目可以做了，返回
                    return cw
                }
                // 做当前利润做大的项目
                cw += profitMaxHeap.poll().profit
            }
            return cw
        }
    }

    private class Program(val cost: Int, val profit: Int)
}
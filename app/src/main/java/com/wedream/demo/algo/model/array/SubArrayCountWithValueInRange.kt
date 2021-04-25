package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class SubArrayCountWithValueInRange : AlgorithmModel() {

    override var name = "最大值减去最小值小于或者等于num的子数组数量"

    override var title = "给定数组 arr 和整数 num，共返回有多少个子数组满足如下情况：\n" +
            "\n" +
            "max(arr[i...j] - min(arr[i...j]) <= num\n" +
            "max(arr[i...j])表示子数组arr[i...j]中的最大值\n" +
            "min[arr[i...j])表示子数组arr[i...j]中的最小值\n"

    override var tips = "通过题目可以分析得到以下两个结论：\n" +
            "1）如果子数组arr[i..j]满足条件，即max(arr[i..j])-min(arr[i..j])<=num，" +
            "那么arr[k..l](i<=k<=l<=j)肯定都满足条件，即若一个数组满足条件，它的所有子数组肯定满足条件。\n" +
            "2）如果子数组arr[i..j]不满足条件，即max(arr[i..j])-min(arr[i..j])>num，" +
            "那么arr[k..l](k<=i<=j<=l)肯定不满足条件，即若一个数组不满足条件，所有包含它的数组肯定都不满足条件。\n\n" +

            "使用双端队列维护窗口内的最大值，时间复杂度为O(n)，\n" +
            "受此启发，我们可以维护两个双端队列来 实时更新 滑动窗口的最大值和最小值。\n" +
            "如果全局最大值和全全局最小值都满足条件，那么滑动窗口中的任意子数组也是满足的。\n" +
            "在计算子数组数量时，每次只计算以滑动窗口的左边界为起点的子数组数量有多少，实际就是滑动窗口的长度。\n"

    override fun execute(option: Option?): Pair<String, String> {
        val input = intArrayOf(6, 2, 5, 3, 4, 1)
        val result = execute(input, 3).toString()
        return Pair(input.string() + ", 3", result)
    }

    companion object {
        fun execute(arr: IntArray, num: Int): Int {
            if (arr.isEmpty() || num < 0) {
                return 0
            }
            val qMin = LinkedList<Int>()
            val qMax = LinkedList<Int>()
            var i = 0
            var j = 0
            var res = 0
            while (i < arr.size) {
                while (j < arr.size) {
                    if (qMin.isEmpty() || qMin.peekLast() != j) {
                        while (qMin.isNotEmpty() && arr[j] <= arr[qMin.peekLast()]) {
                            qMin.pollLast()
                        }
                        qMin.addLast(j)
                        while (qMax.isNotEmpty() && arr[j] >= arr[qMax.peekLast()]) {
                            qMax.pollLast()
                        }
                        qMax.addLast(j)
                    }
                    if (arr[qMax.first] - arr[qMin.first] > num) {
                        break
                    }
                    j++
                }
                res += j - i
                if (qMin.peekFirst() == i) {
                    qMin.pollFirst()
                }
                if (qMax.peekFirst() == i) {
                    qMax.pollFirst()
                }
                i++
            }
            return res
        }
    }
}
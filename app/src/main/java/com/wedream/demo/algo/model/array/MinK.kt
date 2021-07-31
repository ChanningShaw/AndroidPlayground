package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.MaxHeap
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max

class MinK : AlgorithmModel() {

    override var name = "找到无序数组中最小的k个数"

    override var title = "给定一个无序整形数组arr，找到其中最小的k个数，" +
            "要求实现O(Nlogk)和O(N)时间复杂度"

    override var tips = "一、O(Nlogk)：堆排序\n" +
            "二、使用BFPRT算法求出第k小的元素，然后求出最小的k个数"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(5, 4, 3, 2, 1)
        val k = 4
        val result = minK(arr, k)
        return ExecuteResult(arr.string() + ", $k", result.string())
    }

    companion object {
        fun minK(arr: IntArray, k: Int): IntArray {
            if (k < 0 || k > arr.size) {
                return arr
            }
            val maxHeap = MaxHeap<Int>(k)
            for (i in 0 until k) {
                maxHeap.push(arr[i])
            }
            for (i in k until arr.size) {
                if (arr[i] < maxHeap.peek()) {
                    maxHeap.replace(arr[i])
                }
            }
            return maxHeap.toIntArray()
        }

        fun minKByBFPRT(arr: IntArray, k: Int): IntArray {
            if (k < 0 || k > arr.size) {
                return arr
            }
            val kth = KthMin.getMinKthByBFPRT(arr, k)
            val result = IntArray(k)
            var index = 0
            for (i in arr) {
                if (i < kth) {
                    result[index++] = i
                }
            }
            for (i in index until k) {
                result[i] = kth
            }
            return result
        }
    }
}
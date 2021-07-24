package com.wedream.demo.algo.model.array

import android.util.Range
import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.ArrayUtils.insertSort
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import kotlin.math.min

class KthMin : AlgorithmModel() {

    override var name = "找到无序数组中第k小的数"

    override var title = "给定一个无序整形数组arr，找到arr中第k小的数，" +
            "要求实现O(N)"

    override var tips = "使用BFPRT算法，用中位数的中位数来做partition，求k-1位置的值。" +
            "一种局部排序的思想，整体排序的复杂度是O(NLogN)，如果是局部排序就可以实现O(N)。" +
            "使用partition可以不停的缩小k-1位置所在的范围，最终递归求出"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 2, 2, 2, 2)
        val k = 4
        val result = getMinKthByBFPRT(arr, k)
        return ExecuteResult(arr.string() + ", $k", result.string())
    }

    companion object {
        /**
         * BFPRT算法求数组arr中第k小的元素
         */
        fun getMinKthByBFPRT(arr: IntArray, k: Int): Int {
            val copy = ArrayUtils.copyArray(arr)
            return select(copy, 0, copy.lastIndex, k - 1)
        }

        /**
         * 求arr如果排成升序后，i位置的元素
         */
        fun select(arr: IntArray, start: Int, end: Int, i: Int): Int {
            if (start == end) {
                return arr[start]
            }
            // 使用中位数作为划分值
            val pivot = mediaOfMedians(arr, start, end)
            val pivotRange = partition(arr, start, end, pivot)
            return when {
                i in pivotRange -> {
                    arr[i]
                }
                i < pivotRange.lower -> {
                    select(arr, start, pivotRange.lower - 1, i)
                }
                else -> {
                    select(arr, pivotRange.lower + 1, end, i)
                }
            }
        }

        /**
         * partition，将数组分为小于pivot，等于pivot和大于pivot的3部分
         */
        private fun partition(arr: IntArray, start: Int, end: Int, pivotValue: Int): Range<Int> {
            var small = start - 1
            var cur = start
            var big = end + 1
            while (cur != big) {
                when {
                    arr[cur] < pivotValue -> {
                        swap(arr, ++small, cur++)
                    }
                    arr[cur] > pivotValue -> {
                        swap(arr, cur, --big)
                    }
                    else -> {
                        cur++
                    }
                }
            }
            return Range(small + 1, big - 1)
        }

        private fun mediaOfMedians(arr: IntArray, start: Int, end: Int): Int {
            val count = end - start + 1
            val extra = if (count % 5 == 0) 0 else 1
            val medianArr = IntArray(count / 5 + extra)
            for (i in medianArr.indices) {
                val startI = start + i * 5
                val endI = min(startI + 4, end)
                medianArr[i] = getMedian(arr, startI, endI)
            }
            return select(medianArr, 0, medianArr.lastIndex, medianArr.size / 2)
        }

        private fun getMedian(arr: IntArray, start: Int, end: Int): Int {
            insertSort(arr, start, end)
            val sum = start + end
            // 取后面的中位数
            val mid = (sum / 2) + sum % 2
            return arr[mid]
        }
    }
}
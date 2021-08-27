package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class SmallSum : AlgorithmModel() {

    override var name = "求数组的小和"

    override var title = "数组中一个数的小和为这个数左边小于或者等于该数的所有元素之和。" +
            "数组的小和就是所有元素的小和之和。给定数组arr，求其小和"

    override var tips = "如果arr[k]右边有N个数比它大，那么它的小和是arr[k]*N。" +
            "使用归并排序，因为小和涉及到基于位置的比较关系。" +
            "在合并的过程中，左侧被选择时，可以算出它的小和=它的值*右侧剩余的长度。" +
            "但是有一个问题要注意，merge相等元素的时候，要先拷贝右边的元素"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1,3,5,2,4,6)
        val result = getSmallSum(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun getSmallSum(arr: IntArray): Int {
            if (arr.isEmpty()) {
                return 0
            }
            return divideAndMerge(arr, 0, arr.lastIndex)
        }
        private fun divideAndMerge(arr: IntArray, left: Int, right: Int): Int {
            if (left >= right) {
                return 0
            }
            val mid = (left + right) / 2
            return (divideAndMerge(arr, left, mid)
                    + divideAndMerge(arr, mid + 1, right)
                    + merge(arr, left, mid, right))
        }

        private fun merge(arr: IntArray, left: Int, mid: Int, right: Int):Int {
            val tmpArr = IntArray(right - left + 1) // 合并排序需要额外的空间
            var i = left
            var j = mid + 1
            var sum = 0
            var k = 0
            while (i <= mid && j <= right) {
                if (arr[i] < arr[j]) {
                    sum += arr[i] * (right - j + 1)
                    tmpArr[k++] = arr[i++]
                }   else {
                    tmpArr[k++] = arr[j++]
                }
            }
            while (i <= mid) {
                tmpArr[k++] = arr[i++]
            }
            while (j <= right) {
                tmpArr[k++] = arr[j++]
            }
            // 回填数据
            System.arraycopy(tmpArr, 0, arr, left, tmpArr.size)
            return sum
        }
    }
}
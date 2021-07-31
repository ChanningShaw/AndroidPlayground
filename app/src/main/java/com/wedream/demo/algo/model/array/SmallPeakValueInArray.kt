package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string

class SmallPeakValueInArray : AlgorithmModel() {

    override var name = "在数组中找到一个局部最小的位置"

    override var title = "首先定义局部最小值概念：arr长度为1是，arr[0]是局部最小的。如果arr的长度大于2，" +
            "如果第一个元素小于第二个元素，那么第一个元素是局部最小值；同样，如果如果倒数第二个元素小于第一个元素，那么倒数第一个元素是局部最小值；" +
            "对于其他任意元素，如果其同时小于左右相邻的两个元素，那么其是局部最小的。" +
            "给定一个数组arr，已知arr中任意两个相邻的数都不相等，实现一个函数，返回arr中任意一个局部最小值即可。"

    override var tips = "使用二分查找。二分查找并不是数组有序才能使用，只要能确定二分的某一侧肯定存在要找的内容，" +
            "就可以使用二分查找"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(5, 1, 3, 4)
        val result = findSmallPeakIndex(arr)
        return ExecuteResult(arr.string(), result.string())
    }

    companion object {
        fun findSmallPeakIndex(arr: IntArray): Int {
            if (arr.isEmpty()) {
                return -1
            }
            if (arr.size == 1 || arr[0] < arr[1]) {
                return 0
            }
            if (arr[arr.lastIndex] < arr[arr.lastIndex - 1]) {
                return arr.lastIndex
            }
            var left = 1
            var right = arr.lastIndex - 1
            var mid: Int
            while (left < right) {
                mid = (left + right) / 2
                when {
                    arr[mid] > arr[mid - 1] -> {
                        // 递增区间
                        right = mid - 1
                    }
                    arr[mid] > arr[mid + 1] -> {
                        // 递减区间
                        left = mid + 1
                    }
                    else -> {
                        return mid
                    }
                }
            }
            return left // 注意这里的返回值
        }
    }
}
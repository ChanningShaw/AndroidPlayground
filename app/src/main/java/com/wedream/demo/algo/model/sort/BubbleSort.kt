package com.wedream.demo.algo.model.sort

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder

class BubbleSort : AlgorithmModel() {

    override var name = "冒泡排序"

    override var title = "实现冒泡排序"

    override var tips = "每次交换相邻的两个数。\n" +
            "最好：n\n" +
            "平均：n^2\n" +
            "最坏：n^2\n" +
            "稳定\n" +
            "空间：1\n" +
            "优化点1：如果某一趟冒泡排序已经完全有序，可以提前结束，这种优化结果不大。\n" +
            "优化点2：如果已经局部有序，可以记录最后一次交换的位置，扩大有序范围"

    override fun execute(option: Option?): ExecuteResult {
        val arr = arrayOf(5, 6, 3, 4, 1, 2)
        val input = arr.string()
        bubbleSort(arr)
        val output = arr.string()
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun <T : Comparable<T>> bubbleSort(arr: Array<T>) {
            if (arr.isEmpty()) {
                return
            }
            var i = arr.lastIndex
            while (i > 0) { // 未排序的范围
                var lastIndex = 1 // 1当数组完全有序时，可以跳出循环
                for (j in 0 until i) {
                    if (arr[j] > arr[j + 1]) {
                        swap(arr, j, j + 1)
                    }
                    lastIndex = j + 1
                }
                i = lastIndex
                i--
            }
        }
    }
}
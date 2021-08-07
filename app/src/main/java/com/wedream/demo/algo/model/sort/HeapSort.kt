package com.wedream.demo.algo.model.sort

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.string
import java.lang.StringBuilder

class HeapSort : AlgorithmModel() {

    override var name = "堆排序"

    override var title = "实现堆排序"

    override var tips = "默认升序排序。首先建堆，建堆过程其实是大顶堆，要保证最大的元素在对顶。每次新增的元素在末尾，然后自底向上调整。" +
            "等所有元素都入堆以后，开始堆化。" +
            "堆化就是一下子把对顶元素挪到最后，然后剩余部分继续自顶向下调整成大顶堆，然后继续堆化。"

    override fun execute(option: Option?): ExecuteResult {
        val arr = arrayOf(5, 6, 3, 4, 1, 2)
        val input = arr.string()
        heapSort(arr)
        val output = arr.string()
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun <T : Comparable<T>> heapSort(arr: Array<T>) {
            if (arr.isEmpty()) {
                return
            }
            for (i in arr.indices) {
                heapInsert(arr, i)
            }
            for (i in arr.lastIndex downTo 1) {
                ArrayUtils.swap(arr, 0, i) // 把最大的一下子挪到最后
                heapify(arr, 0, i) // 挪完了以后继续调整，调整成最大堆，堆顶又是最大的元素
            }
        }

        private fun <T : Comparable<T>> heapInsert(arr: Array<T>, index: Int) {
            var parent = 0
            var i = index
            // 从底向上调整，把最大的元素调整到堆顶
            while (i != 0) {
                parent = (i - 1) / 2
                if (arr[parent] < arr[i]) {
                    ArrayUtils.swap(arr, parent, i)
                    i = parent
                } else {
                    break
                }
            }
        }

        private fun <T : Comparable<T>> heapify(arr: Array<T>, index: Int, size: Int) {
            var i = index
            var left = i * 2 + 1
            var right = i * 2 + 2
            var largest = i
            while (left < size) {
                if (arr[left] > arr[i]) {
                    largest = left
                }
                if (right < size && arr[right] > arr[largest]) {
                    largest = right
                }
                if (largest != i) {
                    ArrayUtils.swap(arr, largest, i)
                } else {
                    break
                }
                i = largest
                left = i * 2 + 1
                right = i * 2 + 2
            }
        }
    }
}
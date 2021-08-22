package com.wedream.demo.algo.model.sort

class HeapSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "堆排序"

    override var title = "实现堆排序"

    override var tips = "首先是原地建大顶堆，对每一个节点，以其作为根节点自顶向下调整，调整完整个数组就是大顶堆" +
            "然后开始排序，每次一下子把堆顶元素(0号元素)挪到最后，然后剩余部分以0为根节点继续自顶向下调整成大顶堆，然后继续排序。"

    private var heapSize = 0

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.NLogN,
            Complexity.NLogN,
            Complexity.NLogN,
            Complexity.Const,
            false
        )
    }

    override fun onSort() {
        heapSize = array.size
        for (i in heapSize shr 2 - 1 downTo 0) { // 最后一个节点的父节点开始
            shiftDown(i)
        }
        while (heapSize > 1) {
            swap(0, --heapSize) // 把最大的一下子挪到最后
            shiftDown(0) // 挪完了以后继续调整，调整成最大堆，堆顶又是最大的元素
        }
    }

    private fun shiftDown(index: Int) {
        var i = index
        var left = i * 2 + 1
        var right = left + 1
        var largest = i
        while (left < heapSize) { // 如果左孩子还在堆区
            if (cmp(left, i) > 0) {
                largest = left
            }
            if (right < heapSize && cmp(right, largest) > 0) { // 要检查一下右孩子是否还在堆区
                largest = right
            }
            if (largest != i) {
                swap(largest, i)
            } else {
                break
            }
            i = largest
            left = i * 2 + 1
            right = left + 1
        }
    }

    companion object {
        fun <T : Comparable<T>> heapSort(arr: Array<T>) {
            HeapSort<T>().sort(arr)
        }
    }
}
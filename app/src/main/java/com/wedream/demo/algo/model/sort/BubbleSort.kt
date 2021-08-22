package com.wedream.demo.algo.model.sort

class BubbleSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "冒泡排序"

    override var title = "实现冒泡排序"

    override var tips = "每次交换相邻的两个数。\n" +
            "优化点1：如果某一趟冒泡排序已经完全有序，可以提前结束，这种优化结果不大。\n" +
            "优化点2：如果已经局部有序，可以记录最后一次交换的位置，扩大有序范围"


    companion object {
        fun <T : Comparable<T>> bubbleSort(arr: Array<T>) {
            BubbleSort<T>().sort(arr)
        }
    }

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.N2,
            Complexity.N,
            Complexity.N2,
            Complexity.Const,
            true
        )
    }

    override fun onSort() {
        var i = array.lastIndex
        while (i > 0) { // 未排序的范围
            var lastIndex = 1 // 1当数组完全有序时，可以跳出循环
            for (j in 0 until i) {
                if (cmp(j, j + 1) > 0) {
                    swap(j, j + 1)
                }
                lastIndex = j + 1
            }
            i = lastIndex
            i--
        }
    }
}
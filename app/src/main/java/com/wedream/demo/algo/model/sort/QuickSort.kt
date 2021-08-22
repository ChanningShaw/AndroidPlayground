package com.wedream.demo.algo.model.sort

class QuickSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "快速排序"

    override var title = "实现快速排序"

    override var tips = "快速排序可以认为是冒泡排序的改进版，它可以一下子将一个元素从一边直接交换到另一边，而不需要逐个交换。" +
            "寻找一个锚点pivot，将排序区间调整成左边小于pivot，右边大于pivot。递归进行改过程，直到排序区间为1"

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.NLogN,
            Complexity.NLogN,
            Complexity.N2,
            Complexity.LogN,
            false
        )
    }

    override fun onSort() {
        sort(0, array.lastIndex)
    }

    private fun sort(l: Int, r: Int) {
        if (l >= r) return
        val pivot = partition(l, r)
        sort(l, pivot - 1)
        sort(pivot + 1, r)
    }

    private fun partition(left: Int, right: Int): Int {
        swap(left, left + (Math.random() * (right - left)).toInt())
        val pivot = array[left]
        var l = left
        var r = right
        while (l < r) {
            while (l < r) {
                if (cmp(array[r], pivot) > 0) {
                    r--
                } else {
                    array[l++] = array[r]
                    break
                }
            }
            while (l < r) {
                if (cmp(array[l], pivot) < 0) {
                    l++
                } else {
                    array[r--] = array[l]
                    break
                }
            }
        }
        array[r] = pivot
        return r
    }

    companion object {
        fun <T : Comparable<T>> quickSort(arr: Array<T>) {
            QuickSort<T>().sort(arr)
        }
    }
}
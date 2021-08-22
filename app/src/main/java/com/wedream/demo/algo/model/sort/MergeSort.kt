package com.wedream.demo.algo.model.sort

class MergeSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "归并排序"

    override var title = "实现归并排序"

    override var tips = "归并排序可以认为是插入排序的改进版，因为插入排序在逆序对比较少的时候，性能很高。合并过程中用的就是插入排序。\n" +
            "只需要大小为一半的辅助数组，然后把左边的部分复制过去，最后辅助数组和右边的部分一起往原数组合并即可。"

    private lateinit var leftArray: Array<T?>

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.NLogN,
            Complexity.NLogN,
            Complexity.NLogN,
            Complexity.N,
            true
        )
    }

    override fun onSort() {
        leftArray = Array<Comparable<T>?>(array.size shr 1) { null } as Array<T?>
        sort(0, array.lastIndex)
    }

    private fun sort(l: Int, r: Int) {
        if (l >= r) return
        val mid = (l + r) shr 1
        sort(l, mid)
        sort(mid + 1, r)
        merge(l, mid, r)
    }

    /**
     * mid算在左边
     */
    private fun merge(l: Int, mid: Int, r: Int) {
        for (i in 0..mid - l) {
            leftArray[i] = array[l + i]
        }
        var li = 0
        var ri = mid + 1
        var ai = l
        while (li <= mid - l) {
            if (ri <= r && cmp(array[ri], leftArray[li]!!) < 0) { // 右边没结束而且比左边小
                array[ai++] = array[ri++]
            } else {
                array[ai++] = leftArray[li++]!!
            }
        }
    }

    companion object {
        fun <T : Comparable<T>> insertSort(arr: Array<T>) {
            MergeSort<T>().sort(arr)
        }
    }
}
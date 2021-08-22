package com.wedream.demo.algo.model.sort

class InsertSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "插入排序"

    override var title = "实现插入排序"

    override var tips = "每次选择一个元素插入到已排序区域中适合的位置"

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
        for (i in 1..array.lastIndex) {
            var cur = i
            val e = array[cur]
            val insertIndex = searchInsertPos(0, i - 1, e)
            while (cur > insertIndex) {
                array[cur] = array[cur - 1]
                cur--
            }
            array[cur] = e
        }
    }

    private fun searchInsertPos(l: Int, r: Int, e: T): Int {
        var start = l
        var end = r
        while (start <= end) {
            val mid = (start + end) shr 1
            if (cmp(e, array[mid]) < 0) {
                end = mid - 1
            } else {
                start = mid + 1
            }
        }
        return start
    }

    companion object {
        fun <T : Comparable<T>> insertSort(arr: Array<T>) {
            InsertSort<T>().sort(arr)
        }
    }
}
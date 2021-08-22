package com.wedream.demo.algo.model.sort

class SelectSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "选择排序"

    override var title = "实现选择排序"

    override val tips by lazy {
        "每次选择最小的数加到前面已排序区域的最后。\n" + getSortProperty().toString()
    }

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.N2,
            Complexity.N2,
            Complexity.N2,
            Complexity.Const,
            false
        )
    }

    override fun onSort() {
        for (i in 0 until array.lastIndex) { // 要插入的位置
            var min = i
            for (j in i + 1..array.lastIndex) { // 寻找最小的数
                if (cmp(j, min) < 0) {
                    min = j
                }
            }
            swap(i, min)
        }
    }

    companion object {
        fun <T : Comparable<T>> selectSort(arr: Array<T>) {
            SelectSort<T>().sort(arr)
        }
    }
}
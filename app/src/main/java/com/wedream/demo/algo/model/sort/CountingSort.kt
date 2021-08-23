package com.wedream.demo.algo.model.sort

class CountingSort : SortModel<Int>() {

    override var name = "计数排序"

    override var title = "实现计数排序"

    override var tips = ""

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.N,
            Complexity.N,
            Complexity.N,
            Complexity.N,
            true
        )
    }

    override fun onSort() {
        var min = array[0]
        var max = array[0]
        for (i in array) {
            if (i < min) {
                min = i
            }
            if (i > max) {
                max = i
            }
        }
        val counts = IntArray(max - min + 1) // 根据最大最小值生成统计数组
        for (i in array) {
            counts[i - min]++
        }
        for (i in 1..counts.lastIndex) {
            counts[i] += counts[i - 1] // 生成累计数组
        }
        val newArr = Array(array.size) { 0 }
        for (i in array) {
            newArr[--counts[i - min]] = i // 查找到原来数组元素的位置，然后放入新数组
        }
        System.arraycopy(newArr, 0, array, 0, array.size) // 拷贝回来
    }

    companion object {
        fun countingSort(arr: Array<Int>) {
            CountingSort().sort(arr)
        }
    }
}
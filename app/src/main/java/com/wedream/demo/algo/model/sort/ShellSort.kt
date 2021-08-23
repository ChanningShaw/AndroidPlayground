package com.wedream.demo.algo.model.sort

class ShellSort<T : Comparable<T>> : SortModel<T>() {

    override var name = "希尔排序"

    override var title = "实现希尔排序"

    override var tips = "希尔排序可以认为是插入排序的改进版，是一种分组排序算法。"

    override fun getSortProperty(): SortProperty {
        return SortProperty(
            Complexity.N1x,
            Complexity.N1x,
            Complexity.N2,
            Complexity.Const,
            false
        )
    }

    override fun onSort() {
        val steps = getSteps()
        for (s in steps) {
            sort(s)
        }
    }

    private fun sort(step: Int) {
        for (col in 0 until step) {
            for (begin in col + step..array.lastIndex step step) {
                var cur = begin
                while (cur > col && cmp(cur, cur - step) < 0) {
                    swap(cur, cur - step)
                    cur -= step
                }
            }
        }
    }

    private fun getSteps(): List<Int> {
        val list = arrayListOf<Int>()
        var step = array.size
        while (step > 0) {
            step = step shr 1
            list.add(step)
        }
        return list
    }

    companion object {
        fun <T : Comparable<T>> shellSort(arr: Array<T>) {
            ShellSort<T>().sort(arr)
        }
    }
}
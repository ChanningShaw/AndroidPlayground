package com.wedream.demo.algo.model.sort

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string

abstract class SortModel<T : Comparable<T>> : AlgorithmModel() {

    private var swapCount = 0

    private var cmpCount = 0

    protected lateinit var array: Array<T>

    abstract fun getSortProperty(): SortProperty

    abstract fun onSort()

    override fun execute(option: Option?): ExecuteResult {
        val arr = arrayOf(
            5, 6, 3, 4, 1, 2, 5, 6, 3, 4, 1, 2, 5, 6, 3, 4, 1, 2,
            5, 6, 3, 4, 1, 2, 5, 6, 3, 4, 1, 2, 5, 6, 3, 4, 1, 2
        )

        val input = arr.string()
        cmpCount = 0
        swapCount = 0
        sort(arr as Array<T>)
        val output = arr.string()
        return ExecuteResult(input, output.string() + "\n比较次数：$cmpCount，交换次数：$swapCount")
    }

    fun sort(arr: Array<T>) {
        if (arr.isEmpty()) {
            return
        }
        array = arr
        onSort()
    }

    protected fun swap(i: Int, j: Int) {
        swapCount++
        swap(array, i, j)
    }

    protected fun cmp(i: Int, j: Int): Int {
        cmpCount++
        return array[i].compareTo(array[j])
    }

    protected fun cmp(t1: T, t2: T): Int {
        cmpCount++
        return t1.compareTo(t2)
    }

    data class SortProperty(
        var mean: Complexity,
        var best: Complexity,
        var worst: Complexity,
        var spaceCpl: Complexity,
        var stable: Boolean
    ) {
        override fun toString(): String {
            val stableStr = if (stable) "稳定" else "不稳定"
            return "[平均：$mean, 最好：$best, 最坏：$worst, 空间复杂度：$spaceCpl, 稳定性：$stableStr]"
        }
    }

    enum class Complexity {
        Const, LogN, N, NLogN, N2, N3,
    }

    enum class SortType {
        Select, Bubble, Insert, Heap
    }
}
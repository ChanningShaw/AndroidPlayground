package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.sort.BubbleSort
import com.wedream.demo.algo.model.sort.HeapSort
import com.wedream.demo.algo.model.sort.SelectSort
import com.wedream.demo.algo.model.sort.SortProxy

/**
 * 各种排序算法
 */
object SortAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            SortProxy<Int>(),
        )
    }
}
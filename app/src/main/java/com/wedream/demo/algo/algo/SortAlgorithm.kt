package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.sort.HeapSort

/**
 * 各种排序算法
 */
object SortAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            HeapSort()
        )
    }
}
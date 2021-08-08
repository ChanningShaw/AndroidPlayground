package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.matrix.*

object MatrixAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            CirclePrint(),
            MatrixRotate(),
            ZigZagPrint(),
            SearchInPartiallySortMatrix(),
            MaxSumOfSubMatrix(),
            TopKInMatrix(),
            MaxBorderSize(),
            ShortestPathInMatrix(),
            MatrixTraverse(),
        )
    }
}
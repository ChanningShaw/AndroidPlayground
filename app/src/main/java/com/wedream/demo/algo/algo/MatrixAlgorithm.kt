package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.matrix.CirclePrint
import com.wedream.demo.algo.model.matrix.MatrixRotate
import com.wedream.demo.algo.model.matrix.ZigZagPrint

object MatrixAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            CirclePrint(),
            MatrixRotate(),
            ZigZagPrint()
        )
    }
}
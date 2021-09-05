package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.dp.*

object DPAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            MinCoins(),
            DoubleEndTake(),
            HorseJump(),
            LiveProbability(),
            MoneyCombos(),
            EatGrass(),
        )
    }
}
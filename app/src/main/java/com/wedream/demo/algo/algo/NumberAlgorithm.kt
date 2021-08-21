package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.number.*

object NumberAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            SwapInt(),
            GetBigger(),
            OneCount(),
            OnceNum(),
            RandomNum(),
            ZeroCountInFactorial(),
        )
    }
}
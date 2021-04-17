package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*

object ArrayAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            MaxValuesInSlidingWindow(),
            SubArrayCountWithValueInRange(),
            MonotonicStack(),
            NextGreaterNumber(),
            MaxRectSize(),
            JosephusProblem(),
            PalindromeLink(),
            ListPartition(),
            RandomPointerListCopy(),
            NumListAdd(),
            JudgeWhetherListIntersect()
        )
    }
}
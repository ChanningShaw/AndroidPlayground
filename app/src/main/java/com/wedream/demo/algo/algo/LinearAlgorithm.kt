package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*
import com.wedream.demo.algo.model.array.*
import com.wedream.demo.algo.model.list.*

object LinearAlgorithm {
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
            JudgeWhetherListIntersect(),
            DeleteDuplicateNodeInList(),
            DeleteNodeInList(),
            InsertIntoOrderCircleList(),
            MergeOrderedList()
        )
    }
}
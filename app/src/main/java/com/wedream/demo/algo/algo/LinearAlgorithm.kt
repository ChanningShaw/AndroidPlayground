package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*
import com.wedream.demo.algo.model.array.*
import com.wedream.demo.algo.model.classics.Huffman
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.algo.model.list.*
import com.wedream.demo.algo.model.matrix.MaxRectSize

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
            MergeOrderedList(),
            KMP(),
            MinK(),
            KthMin(),
            LengthOfNeedToSort(),
            ValueOfSpecificOccurNumber(),
            LengthOfIntegrableSubArray(),
            PrintPairOfSumK(),
            SubArrayOfSumK1(),
            SubArrayOfSumK2(),
            SubArrayOfSumK3(),
            SmallSum(),
            NormalNumberSort(),
            AdjustOddOrEven(),
            MaxSumOfSubArray(),
            SmallPeakValueInArray(),
            MaxProductOfSubArray(),
            ProductExceptItself(),
            ArrayPartition(),
            MissingMinValue(),
            MaxGap(),
            MaxProfit(),
            Huffman()
        )
    }
}
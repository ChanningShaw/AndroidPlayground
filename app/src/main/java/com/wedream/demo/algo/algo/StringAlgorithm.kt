package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.array.NumberStrToCharacterStr
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.algo.model.classics.Manacher
import com.wedream.demo.algo.model.matrix.*
import com.wedream.demo.algo.model.string.*

object StringAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            KMP(),
            IsDeformation(),
            IsRotation(),
            NumberConvert(),
            StringStatistics(),
            GetIndexOfNullable(),
            ReplaceSpace(),
            ReverseByWord(),
            RemoveDuplicateLetters(),
            StringDistance(),
            ShortestStringTransPath(),
            BracketsValidity(),
            ExpressionSolution(),
            ConnectStrings(),
            LongestUniqueSubString(),
            MinLengthToWrap(),
            MinCutCount(),
            AllSubSequences(),
            AllPermutations(),
            NumberStrToCharacterStr(),
            Manacher(),
        )
    }
}
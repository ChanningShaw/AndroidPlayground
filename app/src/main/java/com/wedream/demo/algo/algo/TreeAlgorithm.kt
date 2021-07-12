package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*
import com.wedream.demo.algo.model.tree.*

object TreeAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            TreeTraverse(),
            GetTreeLevel(),
            UpdateTreeNodeLevel(),
            PrintTreeNode(),
            GetMaxLengthWithSum(),
            MaxBST(),
            MaxBSTTopology(),
            TowErrorNodesOfBST()
        )
    }
}
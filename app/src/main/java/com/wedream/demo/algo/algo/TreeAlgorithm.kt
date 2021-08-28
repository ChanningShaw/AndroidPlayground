package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*
import com.wedream.demo.algo.model.tree.*

object TreeAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            TreeTraverse(),
            TreeSerialize(),
            GetTreeLevel(),
            UpdateTreeNodeLevel(),
            PrintTreeNode(),
            GetMaxLengthWithSum(),
            MaxBST(),
            MaxBSTTopology(),
            TwoErrorNodesOfBST(),
            TreeContains(),
            SubTree(),
            PostArrayToBST(),
            IsBST(),
            IsCBT(),
            GenerateBST(),
            NextNode(),
            LowestCommonAncestor(),
            MaxDistance(),
            TwoNodeDistance(),
            GeneratePostArray(),
            TreeCount(),
            NodeCountOfCBT(),
            TreeWidth(),
            IsBalancedTree()
        )
    }
}
package com.wedream.demo.algo.algo

import com.wedream.demo.algo.model.*
import com.wedream.demo.algo.model.tree.GetTreeLevel
import com.wedream.demo.algo.model.tree.PrintTreeNode
import com.wedream.demo.algo.model.tree.TreeTraverse
import com.wedream.demo.algo.model.tree.UpdateTreeNodeLevel

object TreeAlgorithm {
    fun getModels(): List<AlgorithmModel> {
        return listOf(
            TreeTraverse(),
            GetTreeLevel(),
            UpdateTreeNodeLevel(),
            PrintTreeNode()
        )
    }
}
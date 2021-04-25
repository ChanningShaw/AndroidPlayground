package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree

class UpdateTreeNodeLevel : AlgorithmModel() {

    override var name = "更新树的节点层级"

    override var title = "树的节点Node有一个属性level，来表示该节点位于" +
            "该树的的第几层，给定一个根节点root，请更新该树的所有节点的level值"

    override var tips = "根节点的level = 1，左右节点的level值 = 根节点level值 + 1"

    override fun execute(option: Option?): Pair<String, String> {
        val root = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        root.left = n1
        root.right = n2
        n1.right = n3
        n3.left = n4
        val output = updateNodeLevel(root)
        return Pair(",", "")
    }

    companion object {
        fun <T> updateNodeLevel(root: BinaryTree.Node<T>) {
            updateNodeLevel(root, 1)
        }

        private fun <T> updateNodeLevel(root: BinaryTree.Node<T>?, level: Int) {
            if (root == null) return
            root.level = level
            updateNodeLevel(root.left, level + 1)
            updateNodeLevel(root.right, level + 1)
        }
    }
}
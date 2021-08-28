package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class IsBalancedTree : AlgorithmModel() {
    override var name = "判断一棵树是否为平衡二叉树"

    override var title = "判断一棵树是否为平衡二叉树"

    override var tips = "树型DP套路"

    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        val n6 = BinaryTree.Node(6)
        val n7 = BinaryTree.Node(7)
        n4.left = n3
        n4.right = n7
        n3.left = n1
        n1.right = n2
        n7.left = n5
        n5.right = n6
        val output = isBalancedTree(n4).isBalanced
        return ExecuteResult(n4.string(), output.string())
    }

    companion object {
        fun isBalancedTree(root: BinaryTree.Node<Int>?): Entity {
            if (root == null) {
                return Entity(true, 0)
            }
            val leftEntity = isBalancedTree(root.left)
            val rightEntity = isBalancedTree(root.right)
            val isBalanced = leftEntity.isBalanced
                    && rightEntity.isBalanced
                    && abs(leftEntity.height - rightEntity.height) <= 1
            val height = max(leftEntity.height, rightEntity.height) + 1
            return Entity(isBalanced, height)
        }
    }

    class Entity(val isBalanced: Boolean, val height: Int)
}
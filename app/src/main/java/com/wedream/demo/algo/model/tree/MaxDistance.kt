package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class MaxDistance : AlgorithmModel() {
    override var name = "二叉树中节点间的最大距离"

    override var title = "从二叉树的节点出发，可以向上或者向下走，但沿途的节点只能经过一次，" +
            "当到达节点B时，路径上的节点数叫做A到B的距离"

    override var tips = "使用树型DP套路。\n" +
            ""

    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        val n6 = BinaryTree.Node(6)
        val n7 = BinaryTree.Node(7)
        val n8 = BinaryTree.Node(8)
        val n9 = BinaryTree.Node(9)
        val n10 = BinaryTree.Node(10)
        n6.left = n3
        n6.right = n9
        n3.left = n1
        n3.right = n4
        n3.parent = n6
        n1.right = n2
        n1.parent = n3
        n4.right = n5
        n4.parent = n3
        n9.left = n8
        n9.right = n10
        n8.parent = n9
        n8.left = n7
        n10.parent = n9
        n2.parent = n1
        n5.parent = n4
        n7.parent = n8
        val output = getMostDistance(n6)
        return ExecuteResult(n6.string(), output.string())
    }

    companion object {
        fun <T> getMostDistance(node: BinaryTree.Node<T>): Int {
            return process(node).maxDis
        }

        private fun <T> process(node: BinaryTree.Node<T>?) : ReturnType {
            if (node == null) {
                return ReturnType(0, 0)
            }
            val leftR = process(node.left)
            val rightR = process(node.right)
            val height = max(leftR.height, rightR.height) + 1
            val maxDis = max(leftR.height + rightR.height + 1, max(leftR.maxDis, rightR.maxDis))
            return ReturnType(height, maxDis)
        }
    }
    private class ReturnType(val height: Int, val maxDis: Int)
}
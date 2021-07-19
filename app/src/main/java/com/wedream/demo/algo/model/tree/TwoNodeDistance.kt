package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.abs

class TwoNodeDistance : AlgorithmModel() {
    override var name = "计算二叉树中两个节点的距离"

    override var title = "给定一颗二叉树的头结点root，以及两个节点n1，n2, 返回n1,n2的距离"

    override var tips = "使用树型DP套路，寻找公共祖先到这2个点的距离和方向"

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
        val output = getDistance(n6, n2, n5)
        return ExecuteResult(
            n6.string() + "\n${n2.value.string()}, ${n5.value.string()}",
            output.string()
        )
    }

    companion object {
        fun getDistance(
            root: BinaryTree.Node<Int>?,
            n1: BinaryTree.Node<Int>,
            n2: BinaryTree.Node<Int>
        ): Int {
            val returnType = process(root, n1, n2)
            return if (returnType.d1 == returnType.d2) {
                abs(returnType.dis1 - returnType.dis2) + 1
            } else returnType.dis1 + returnType.dis2 - 1
        }

        private fun process(
            root: BinaryTree.Node<Int>?,
            n1: BinaryTree.Node<Int>,
            n2: BinaryTree.Node<Int>
        ): ReturnType {
            if (root == null) {
                return ReturnType(-1, true, -1, true)
            }
            val returnType = ReturnType(-1, true, -1, true)
            if (root == n1) {
                returnType.dis1 = 1
            }
            if (root == n2) {
                returnType.dis2 = 1
            }
            if (returnType.isValid()) {
                // 如果根节点找全了，直接返回
                return returnType
            }
            val leftR = process(root.left, n1, n2)
            if (leftR.isValid()) {
                // 如果左子树找全了，直接返回
                return leftR
            }
            if (leftR.dis1 != -1) {
                returnType.dis1 = leftR.dis1 + 1
                returnType.d1 = true
            } else if (leftR.dis2 != -1) {
                returnType.dis2 = leftR.dis2 + 1
                returnType.d2 = true
            }
            if (returnType.isValid()) {
                // 如果根节点 + 左子树找全了，直接返回
                return returnType
            }
            val rightR = process(root.right, n1, n2)
            if (rightR.isValid()) {
                return rightR
            }
            if (rightR.dis1 != -1) {
                returnType.dis1 = rightR.dis1 + 1
                returnType.d1 = false
            }
            if (rightR.dis2 != -1) {
                returnType.dis2 = rightR.dis2 + 1
                returnType.d2 = false
            }
            // 返回这棵树的结果
            return returnType
        }
    }

    private class ReturnType(
        var dis1: Int,
        var d1: Boolean,
        var dis2: Int,
        var d2: Boolean
    ) {
        fun isValid(): Boolean {
            return dis1 != -1 && dis2 != -1
        }
    }
}
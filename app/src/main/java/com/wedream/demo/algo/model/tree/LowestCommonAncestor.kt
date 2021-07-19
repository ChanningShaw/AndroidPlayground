package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class LowestCommonAncestor : AlgorithmModel() {
    override var name = "在二叉树中找到两个节点的最近公共祖先"

    override var title = "给定一颗二叉树的头结点root，以及两个节点n1，n2, 返回n1,n2的最近公共祖先"

    override var tips = "在左右子树中分别找n1, n2，如果找到就返回其一" +
            "，如果都找到就返回当前节点" +
            "，如果都没找到返回null"

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
        val output = getLowestCommonAncestor(n6, n5, n3)
        return ExecuteResult(
            n6.string() + "\n${n5.value.string()}, ${n3.value.string()}",
            output?.value.string()
        )
    }

    companion object {
        fun getLowestCommonAncestor(
            root: BinaryTree.Node<Int>?,
            n1: BinaryTree.Node<Int>,
            n2: BinaryTree.Node<Int>
        ): BinaryTree.Node<Int>? {
            if (root == null || root == n1 || root == n2) {
                return root
            }
            val left = getLowestCommonAncestor(root.left, n1, n2)
            val right = getLowestCommonAncestor(root.right, n1, n2)
            if (left != null && right != null) {
                // 左右子树分别找到了
                return root
            }
            return left ?: right
        }
    }
}
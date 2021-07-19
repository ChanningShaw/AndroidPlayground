package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class NextNode : AlgorithmModel() {
    override var name = "在二叉树中找到一个节点的后续节点"

    override var title = "现在一个新型二叉树节点Node，除了左右孩子以外，还具有指向父节点的指针parent。" +
            "给定一个节点node，要求找到其后续节点。该后续节点就是这棵树中序遍历序列中node的下一个节点"

    override var tips = "1. 如果有右孩子，肯定在右孩子中。后续节点是右孩子中最左边的节点\n" +
            "2. 如果没有右孩子，那么就是当前节点是左孩子的父节点"

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
        val output = nextNode(n7)
        return ExecuteResult(n6.string() + ",\n${n7.value.string()}", output?.value.string())
    }

    companion object {
        fun nextNode(node: BinaryTree.Node<Int>): BinaryTree.Node<Int>? {
            return if (node.right != null) {
                findMostLeft(node.right!!)
            } else {
                var cur = node
                var parent = cur.parent
                while (parent != null && node != parent.left) {
                    cur = parent
                    parent = cur.parent
                }
                parent
            }
        }
        private fun findMostLeft(root: BinaryTree.Node<Int>): BinaryTree.Node<Int> {
            var node = root
            while (node.left != null) {
                node = node.left!!
            }
            return node
        }
    }
}
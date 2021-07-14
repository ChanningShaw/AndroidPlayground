package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class MaxBSTTopology : AlgorithmModel() {
    override var name = "找到二叉树中符合搜索二叉树条件的最大拓扑结构"

    override var title = "给定一棵树的根节点root，已知其所有节点的值都不一样，" +
            "返回其中最大的且符合搜索二叉树条件的最大拓扑结构的大小"

    override var tips = "方法一：二叉树的节点数为N，时间复杂度为O(n^2)\n" +
            "按照搜索二叉树的查找方法，从一个节点查找其所有子节点，如果能找到，那说明该子节点可以作为" +
            "拓扑结构的一部分，并继续考察该节点的孩子节点，直到找到所有符合条件的子节点，" +
            "这样就找到了以该节点为根节点的最大拓扑结构的大小，还需要考察其他所有节点作为根节点的情况。" +
            "解法二："


    override fun execute(option: Option?): Pair<String, String> {
        val root = BinaryTree.Node(6)
        val n0 = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        val n7 = BinaryTree.Node(7)
        val n8 = BinaryTree.Node(8)
        val n9 = BinaryTree.Node(9)
        val n10 = BinaryTree.Node(10)
        val n11 = BinaryTree.Node(11)
        val n12 = BinaryTree.Node(12)
        val n13 = BinaryTree.Node(13)
        val n14 = BinaryTree.Node(14)
        val n15 = BinaryTree.Node(15)
        val n16 = BinaryTree.Node(16)
        val n17 = BinaryTree.Node(17)
        val n18 = BinaryTree.Node(18)
        val n19 = BinaryTree.Node(19)
        val n20 = BinaryTree.Node(20)
        root.left = n1
        root.right = n12
        n1.left = n0
        n1.right = n3
        n12.left = n10
        n12.right = n13
        n10.left = n4
        n10.right = n14
        n4.left = n2
        n4.right = n5
        n14.left = n11
        n14.right = n15
        n13.left = n20
        n13.right = n16
        val output = maxBSTTopology(root)
        return Pair(root.string(), output.string())
    }

    companion object {
        fun maxBSTTopology(root: BinaryTree.Node<Int>?): Int {
            if (root == null) {
                return 0
            }
            var max = bstTopologySize(root, root)
            max = max(maxBSTTopology(root.left), max)
            max = max(maxBSTTopology(root.right), max)
            return max
        }

        private fun bstTopologySize(root: BinaryTree.Node<Int>?, n: BinaryTree.Node<Int>?): Int {
            if (root != null && n != null && isBstNode(root, n)) {
                return bstTopologySize(root, n.left) + bstTopologySize(root, n.right) + 1
            }
            return 0
        }

        /**
         * 从 h能不能找到 n
         */
        private fun isBstNode(h: BinaryTree.Node<Int>?, n: BinaryTree.Node<Int>?): Boolean {
            if (h == null || n == null) {
                return false
            }
            if (h == n) {
                return true
            }
            val next = if (h.value > n.value) h.left else h.right
            return isBstNode(next, n)
        }
    }
}
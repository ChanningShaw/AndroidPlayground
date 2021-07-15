package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class MaxBST : AlgorithmModel() {
    override var name = "找到二叉树中的最大搜索二叉子树*"

    override var title = "给定一棵树的根节点root，已知其所有节点的值都不一样，" +
            "找到含有最多的节点的搜索二叉树，并返回这棵树的头结点。"

    override var tips = "使用树型dp套路：如果题目求解目标是S规则，则求解流程可以定成" +
            "以每一个节点为头结点的子树在S规则下的每一个答案，并且答案一定在其中\n" +
            "具体就这道题，对于以x为头结点的子树来说，最大的搜索二叉树只可能有3种情况：\n" +
            "1.在左子树中 2.在右子树中 3.以x为头结点的子树。\n" +
            "为了比较这3者，在遍历的过程中，需要收集以下数据：左右子树的最大搜索二叉树的根节点" +
            "leftMaxBSTHead、rightMaxBSTHead以及大小leftMaxBSTSize和rightMaxBSTSize，和为了判断是否搜索树" +
            "所需要的最大最小值max、min"


    override fun execute(option: Option?): ExecuteResult {
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
        val output = getMaxBSTSize(root)
        return ExecuteResult(root.string(), output.string())
    }

    companion object {
        fun getMaxBSTSize(root: BinaryTree.Node<Int>): BinaryTree.Node<Int>? {
            return process(root).maxBSTHead
        }

        private fun process(root: BinaryTree.Node<Int>?): Entity {
            if (root == null) {
                return Entity(null, 0, Int.MAX_VALUE, Int.MIN_VALUE)
            }
            val leftEntity = process(root.left)
            val rightEntity = process(root.right)
            val min = min(root.value, min(leftEntity.minValue, rightEntity.minValue))
            val max = max(root.value, max(leftEntity.maxValue, rightEntity.maxValue))
            var maxBSTSize = max(leftEntity.maxBSTSize, rightEntity.maxBSTSize)
            var maxBSTHead =
                if (leftEntity.maxBSTSize > rightEntity.maxBSTSize) leftEntity.maxBSTHead else rightEntity.maxBSTHead
            if (leftEntity.maxBSTHead == root.left
                && rightEntity.maxBSTHead == root.right
                && root.value > leftEntity.maxValue
                && root.value < rightEntity.minValue
            ) {
                // 构成了新的搜索二叉树
                maxBSTSize = leftEntity.maxBSTSize + rightEntity.maxBSTSize + 1
                maxBSTHead = root
            }
            return Entity(maxBSTHead, maxBSTSize, min, max)
        }
    }

    data class Entity(
        val maxBSTHead: BinaryTree.Node<Int>?,
        val maxBSTSize: Int,
        val minValue: Int,
        val maxValue: Int
    )
}
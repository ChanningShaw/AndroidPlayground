package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class IsCBT : AlgorithmModel() {
    override var name = "判断一棵树是否为完全二叉树"

    override var title = "给定一棵树的根节点root，已知其所有节点的值都不一样，" +
            "判断此树是不是完全二叉树"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        n1.left = n2
        n1.right = n3
        n2.right = n4
        val output = isCBT(n1)
        return ExecuteResult(n1.string(), output.string())
    }

    companion object {
        fun isCBT(root: BinaryTree.Node<Int>): Boolean {
            var result = true
            var shouldBeLeaf = false
            TreeTraverse.levelTraverse(root) {
                if (it.left == null && it.right != null) {
                    result = false
                } else if (shouldBeLeaf && (it.left != null || it.right != null)) {
                    result = false
                } else if (it.left == null) {
                    shouldBeLeaf = true
                }
                result
            }
            return result
        }
    }
}
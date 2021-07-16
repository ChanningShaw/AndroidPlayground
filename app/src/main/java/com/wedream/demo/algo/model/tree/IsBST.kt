package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class IsBST : AlgorithmModel() {
    override var name = "判断一棵树是否为搜索二叉树"

    override var title = "给定一棵树的根节点root，已知其所有节点的值都不一样，" +
            "判断此树是不是搜索二叉树"

    override var tips = "按照中序遍历，如果是递增的，则是搜索二叉树，否则不是"


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
        val output = isBST(n4)
        return ExecuteResult(n4.string(), output.string())
    }

    companion object {
        fun isBST(root: BinaryTree.Node<Int>): Boolean {
            var cur = Int.MIN_VALUE
            var result = true
            TreeTraverse.midTraverse(root) {
                if (it.value > cur) {
                    cur = it.value
                    true
                } else {
                    result = false
                    false
                }
            }
            return result
        }
    }
}
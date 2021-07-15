
package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*
import kotlin.math.max

class GetTreeLevel : AlgorithmModel() {

    override var name = "计算树的深度"

    override var title = "给定一棵树的根节点root，计算该树的深度，" +
            "一个树的深度就是该树最深的叶子节点所在的层数"

    override var tips = "当前level = 左右子树level的较大值 + 1，递归调用即可"

    override fun execute(option: Option?): ExecuteResult {
        val root = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        root.left = n1
        root.right = n2
        n1.right = n3
        n3.left = n4
        val output = getTreeLevel(root)
        return ExecuteResult(root.string(), output.toString())
    }

    companion object {
        fun <T> getTreeLevel(root: BinaryTree.Node<T>?): Int {
            if (root == null) return 0
            val leftLevel = getTreeLevel(root.left)
            val rightLevel = getTreeLevel(root.right)
            return max(leftLevel, rightLevel) + 1
        }
    }
}
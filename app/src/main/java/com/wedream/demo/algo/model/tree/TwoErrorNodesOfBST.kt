package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.LogUtils.getStack
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max
import kotlin.math.min

class TwoErrorNodesOfBST : AlgorithmModel() {
    override var name = "找到并调整搜索二叉树中两个错误节点"

    override var title = "一颗二叉树原本是搜索二叉树，但是其中有两个节点调整了位置，" +
            "请找到这两个节点并返回。"

    override var tips = "如果是搜索二叉树，那么按中序遍历，会的一个得升序的序列。" +
            "现在因为两个节点对换了，这个序列中起码会有一个降序，那么第一个错误节点就是，" +
            "第一次降序时较大的节点，第二个节点是最后一次降序时较小的节点（分别位于两端）"


    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        n4.left = n1
        n4.right = n3
        n1.right = n2
        n3.right = n5

        val output = getTowErrorNodes(n4)
        return ExecuteResult(n4.string(), output.string())
    }

    companion object {
        fun getTowErrorNodes(root: BinaryTree.Node<Int>): Array<BinaryTree.Node<Int>?> {
            val errs = arrayOfNulls<BinaryTree.Node<Int>>(2)
            var pre: BinaryTree.Node<Int>? = null
            TreeTraverse.midTraverse(root) {
                if (pre != null && pre!!.value > it.value) {
                    if (errs[0] == null) {
                        errs[0] = pre
                    }
                    errs[1] = it
                }
                pre = it
            }
            return errs
        }
    }
}
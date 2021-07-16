package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class GenerateBST : AlgorithmModel() {
    override var name = "通过有序数组生成搜索二叉树"

    override var title = "给定一个有序数组sortArr， 已知其中没有重复值，用这个有序数组生成一颗平衡搜索二叉树，" +
            "并且该树的中序遍历结果和sortArr一致"

    override var tips = "二分有序数组，中间值作为根节点，左边生成左子树，右边生成右子树"

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(1, 2, 3, 4, 5, 6, 7)
        return ExecuteResult(input.string(), generateBST(input).string())
    }

    companion object {
        fun generateBST(arr: IntArray): BinaryTree.Node<Int>? {
            return generate(arr, 0, arr.lastIndex)
        }

        private fun generate(arr: IntArray, start: Int, end: Int): BinaryTree.Node<Int>? {
            if (start > end) {
                return null
            }
            val mid = (start + end) / 2
            val node = BinaryTree.Node(arr[mid])
            node.left = generate(arr, start, mid - 1)
            node.right = generate(arr, mid + 1, end)
            return node
        }
    }
}
package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string

class PostArrayToBST: AlgorithmModel() {
    override var name = "根据后续数组重建搜索二叉树"

    override var title = "给定一个整形数组，已知其没有重复值，判断arr是否可能是节点类型为" +
            "整形的搜索二叉树的遍历结果，如果是，通过数组重构二叉树"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val input = intArrayOf(2, 1, 3, 6, 5, 7, 4)
        val output = postArrayToBST(input)
        return ExecuteResult(input.string(), output.string())
    }

    companion object {
        fun isPostArray(arr: IntArray): Boolean {
            if (arr.isEmpty()) {
                return false
            }
            return isPost(arr, 0, arr.lastIndex)
        }

        fun postArrayToBST(arr: IntArray): BinaryTree.Node<Int>? {
            if (!isPostArray(arr)) {
                return null
            }
            return posToBST(arr, 0, arr.lastIndex)
        }

        private fun posToBST(arr: IntArray, start: Int, end: Int): BinaryTree.Node<Int>? {
            if (start > end) {
                return null
            }
            val root = BinaryTree.Node(arr[end])
            var left = end - 1
            while (left >= start && arr[left] > arr[end]) {
                left--
            }
            root.left = posToBST(arr, start, left)
            root.right = posToBST(arr, left + 1, end - 1)
            return root
        }

        private fun isPost(arr: IntArray, start: Int, end: Int): Boolean {
            if (start == end) {
                return true
            }
            val root = arr[end]
            var left = end - 1
            while (left >= start && arr[left] > root) {
                left--
            }
            if (left < start || left == end - 1) {
                // 都是左子树或者都是右子树
                return isPost(arr, start, end - 1)
            }
            for (i in start..left) {
                if (arr[i] > root) {
                    return false
                }
            }
            return isPost(arr, start, left) && isPost(arr, left + 1, end - 1)
        }
    }
}
package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import java.lang.StringBuilder

class PrintTreeNode : AlgorithmModel() {

    override var name = "直观地打印二叉树"

    override var title = "给定一个二叉树的根节点root，要求返回一个字符串，" +
            "该字符串可以直观地显示二叉树"

    override var tips = "一、找出该树的深度depth， 并且算出每一个节点本身所有的层级level\n" +
            "二、那么该树最多可能的节点数为2^depth-1个，那么按照每一列最多打印一个节点数的原则，" +
            "最多需要2^depth-1列，那么可以初始化一个字符串数组arr[depth][2^depth-1]，来表示打印的结果，" +
            "每一行表示一层，每一列表示在该层中的位置\n" +
            "三、arr初始化为空字符串，先序遍历该树，遍历的参数是当前的放置的范围(left, right)， 根节点总是放在中间index = (right + left) / 2，" +
            "那么当前节点在数组中的位置就是[level-1][index]，level是节点所在的层级" +
            "左节点的范围是(left, index - 1), 右节点的范围是(index + 1, right)，以此递归\n" +
            "四、最后遍历数组，依次添加到StringBuilder中，输出"

    override fun execute(option: Option?): Pair<String, String> {
        val root = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        root.left = n1
        root.right = n2
        n1.left = n3
        n1.right = n4
        val output = printTree(root)
        return Pair(root.string(), output)
    }

    companion object {
        var treeDepth = 0
        var width = 0
        fun <T> printTree(root: BinaryTree.Node<T>): String {
            treeDepth = 0
            width = 0
            updateNodeLevel(root, 1)
            width = (1 shl treeDepth) - 1
            val arr = Array<Array<String>?>(treeDepth) {
                null
            }
            placeValue(arr, 0, width - 1, root)
            val sb = StringBuilder()
            for (i in arr.indices) {
                for (j in arr[0]!!.indices) {
                    sb.append("${ arr[i]!![j]} ")
                }
                sb.append("\n")
            }
            return sb.toString()
        }

        private fun <T> updateNodeLevel(root: BinaryTree.Node<T>?, level: Int) {
            if (root == null) return
            root.level = level
            if (level > treeDepth) {
                treeDepth = level
            }
            updateNodeLevel(root.left, level + 1)
            updateNodeLevel(root.right, level + 1)
        }

        private fun <T> placeValue(
            arr: Array<Array<String>?>,
            left: Int,
            right: Int,
            root: BinaryTree.Node<T>?
        ) {
            if (root == null) return
            val index = (right + left) / 2
            val level = root.level - 1
            var row = arr[level]
            if (row == null) {
                row = Array(width) {
                    "   "
                }
                arr[level] = row
            }
            row[index] = root.value.string()
            placeValue(arr, left, index - 1, root.left)
            placeValue(arr, index + 1, right, root.right)
        }
    }
}
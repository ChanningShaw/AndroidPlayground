package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*

class TreeTraverse : AlgorithmModel() {

    override var name = "树的遍历"

    override var title = "1. 先序遍历：根、左、右， 用递归和非递归实现\n" +
            "2. 中序遍历：左、根、右， 用递归和非递归实现\n" +
            "3. 后续遍历：左、右、根， 用递归和非递归实现\n" +
            "4. 层次遍历"

    override var tips = "递归遍历：递归调用即可\n" +
            ""

    private val optionList = listOf(
        Option(0, "先序遍历，递归实现"),
        Option(1, "先序遍历，非递归实现"),
        Option(2, "中序遍历，递归实现"),
        Option(3, "中序遍历，非递归实现"),
        Option(4, "后序遍历，递归实现"),
        Option(5, "后序遍历，非递归实现"),
        Option(6, "层次遍历")
    )

    override fun getOptions(): List<Option> {
        return optionList
    }

    override fun execute(option: Option?): Pair<String, String> {
        val root = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        root.left = n1
        root.right = n2
        n1.right = n3
        n3.left = n4
        val optionId = option?.id ?: 0
        val output = when (optionId) {
            0 -> {
                preTraverse(root)
            }
            1 -> {
                preTraverse(root)
            }
            2 -> {
                midTraverse(root)
            }
            3 -> {
                midTraverse(root)
            }
            4 -> {
                postTraverse(root)
            }
            5 -> {
                postTraverse(root)
            }
            6 -> {
                levelTraverse(root)
            }
            else -> {
                preTraverse(root)
            }
        }
        return Pair(root.string(), output)
    }

    companion object {
        fun preTraverse(root: BinaryTree.Node<Int>): String {
            val builder = StringBuilder()
            preTraverse(root) {
                builder.append(it.value)
            }
            return builder.toString()
        }

        fun midTraverse(root: BinaryTree.Node<Int>): String {
            val builder = StringBuilder()
            midTraverse(root) {
                builder.append(it.value)
            }
            return builder.toString()
        }

        fun postTraverse(root: BinaryTree.Node<Int>): String {
            val builder = StringBuilder()
            postTraverse(root) {
                builder.append(it.value)
            }
            return builder.toString()
        }

        fun levelTraverse(root: BinaryTree.Node<Int>): String {
            val list = LinkedList<BinaryTree.Node<Int>>()
            val sb = StringBuilder()
            list.add(root)
            while (list.isNotEmpty()) {
                val n = list.pollFirst()!!
                sb.append(n.value)
                if (n.left != null) {
                    list.add(n.left!!)
                }
                if (n.right != null) {
                    list.add(n.right!!)
                }
            }
            return sb.toString()
        }

        fun <T> preTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Unit
        ) {
            if (root == null) return
            block.invoke(root)
            preTraverse(root.left, block)
            preTraverse(root.right, block)
        }

        fun <T> midTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Unit
        ) {
            if (root == null) return
            midTraverse(root.left, block)
            block.invoke(root)
            midTraverse(root.right, block)
        }

        fun <T> postTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Unit
        ) {
            if (root == null) return
            postTraverse(root.left, block)
            postTraverse(root.right, block)
            block.invoke(root)
        }
    }
}
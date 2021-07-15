package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*

class TreeSerialize : AlgorithmModel() {

    override var name = "树的序列化"

    override var title = "已知二叉树的节点值的类型为整数，设计一种序列化和反序列化的方案"

    override var tips = "方法一，通过先序遍历的方法实现序列化和反序列化"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "序列化"),
            Option(1, "反序列化")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        when (option?.id) {
            0 -> {
                return serialize()
            }
            1 -> {
                val input = "0!1!3!#!#!4!#!#!2!#!#!"
                val head = recoverPreOrder(input)
                return ExecuteResult(input, head.string())
            }
        }
        return serialize()
    }

    private fun serialize(): ExecuteResult {
        val root = BinaryTree.Node(0)
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        root.left = n1
        root.right = n2
        n1.left = n3
        n1.right = n4
        val output = serializeByPre(root)
        return ExecuteResult(root.string(), output)
    }

    companion object {
        fun <T> serializeByPre(root: BinaryTree.Node<T>?): String {
            val builder = StringBuilder()
            serializeByPre(root, builder)
            return builder.toString()
        }

        fun recoverPreOrder(str: String): BinaryTree.Node<Int>? {
            val values = str.split("!")
            val queue = LinkedList<String>()
            queue.addAll(values)
            return recoverPreOrder(queue)
        }

        private fun <T> serializeByPre(root: BinaryTree.Node<T>?, builder: StringBuilder) {
            if (root == null) {
                builder.append("#!")
            } else {
                builder.append("${root.value}!")
                serializeByPre(root.left, builder)
                serializeByPre(root.right, builder)
            }
        }

        private fun recoverPreOrder(queue: Queue<String>): BinaryTree.Node<Int>? {
            val value = queue.poll()
            if (value == "#") {
                return null
            }
            val head = BinaryTree.Node(value.toInt())
            // 用先序遍历的顺序构造整棵树
            head.left = recoverPreOrder(queue)
            head.right = recoverPreOrder(queue)
            return head
        }
    }
}
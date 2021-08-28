package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
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
        val optionId = option?.id ?: 0
        val output = when (optionId) {
            0 -> {
                preTraverse(root, Mode.Recur)
            }
            1 -> {
                preTraverse(root, Mode.NonRecur)
            }
            2 -> {
                midTraverse(root, Mode.Recur)
            }
            3 -> {
                midTraverse(root, Mode.NonRecur)
            }
            4 -> {
                postTraverse(root, Mode.Recur)
            }
            5 -> {
                postTraverse(root, Mode.NonRecur)
            }
            6 -> {
                val sb = StringBuilder()
                levelTraverse(root) {
                    sb.append(it.value)
                    true
                }
                sb.toString()
            }
            else -> {
                preTraverse(root, Mode.Recur)
            }
        }
        return ExecuteResult(root.string(), output)
    }

    companion object {
        fun preTraverse(root: BinaryTree.Node<Int>, mode: Mode): String {
            val builder = StringBuilder()
            if (mode == Mode.Recur) {
                preTraverse(root) {
                    builder.append(it.value)
                    true
                }
            } else {
                preTraverseNonRecur(root) {
                    builder.append(it.value)
                    true
                }
            }
            return builder.toString()
        }

        fun midTraverse(root: BinaryTree.Node<Int>, mode: Mode): String {
            val builder = StringBuilder()
            if (mode == Mode.Recur) {
                midTraverse(root) {
                    builder.append(it.value)
                    true
                }
            } else {
                midTraverseNonRecur(root) {
                    builder.append(it.value)
                    true
                }
            }
            return builder.toString()
        }

        fun postTraverse(root: BinaryTree.Node<Int>, mode: Mode): String {
            val builder = StringBuilder()
            if (mode == Mode.Recur) {
                postTraverse(root) {
                    builder.append(it.value)
                    true
                }
            } else {
                postTraverseNonRecur2(root) {
                    builder.append(it.value)
                    true
                }
            }
            return builder.toString()
        }

        fun levelTraverse(
            root: BinaryTree.Node<Int>,
            block: (node: BinaryTree.Node<Int>) -> Boolean
        ) {
            val list = LinkedList<BinaryTree.Node<Int>>()
            list.add(root)
            while (list.isNotEmpty()) {
                val n = list.pollFirst()!!
                if (!block.invoke(n)) {
                    return
                }
                if (n.left != null) {
                    list.add(n.left!!)
                }
                if (n.right != null) {
                    list.add(n.right!!)
                }
            }
        }

        fun <T> preTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root == null) return
            if (block.invoke(root)) {
                return
            }
            preTraverse(root.left, block)
            preTraverse(root.right, block)
        }

        /**
         * 使用栈实现
         */
        fun <T> preTraverseNonRecur(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root != null) {
                val stack = Stack<BinaryTree.Node<T>>()
                while (stack.isNotEmpty()) {
                    val cur = stack.pop()
                    block.invoke(cur)
                    if (cur.right != null) {
                        // 先push右孩子
                        stack.push(cur.right)
                    }
                    if (cur.left != null) {
                        stack.push(cur.left)
                    }
                }
            }
        }

        fun <T> midTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root == null) return
            midTraverse(root.left, block)
            if (!block.invoke(root)) {
                return
            }
            midTraverse(root.right, block)
        }

        /**
         * 使用栈和一个当前指针实现
         * 对于每一颗子树，先处理左边界
         */
        fun <T> midTraverseNonRecur(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root != null) {
                val stack = Stack<BinaryTree.Node<T>>()
                var cur: BinaryTree.Node<T>? = root
                while (stack.isNotEmpty() || cur != null) {
                    if (cur != null) {
                        stack.push(cur)
                        cur = cur.left
                    } else {
                        cur = stack.pop()
                        if (!block.invoke(cur)) {
                            return
                        }
                        cur = cur.right
                    }
                }
            }
        }

        fun <T> postTraverse(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root == null) return
            postTraverse(root.left, block)
            postTraverse(root.right, block)
            if (!block.invoke(root)) {
                return
            }
        }

        /**
         * 后序非递归遍历1：采用2个栈实现
         */
        fun <T> postTraverseNonRecur1(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root != null) {
                val s1 = Stack<BinaryTree.Node<T>>()
                val s2 = Stack<BinaryTree.Node<T>>()
                s1.push(root)
                while (s1.isNotEmpty()) {
                    val cur = s1.pop()
                    s2.push(cur)
                    if (cur.left != null) {
                        s1.push(cur.left)
                    }
                    if (cur.right != null) {
                        s1.push(cur.right)
                    }
                }
                while (!s2.isEmpty()) {
                    if (!block.invoke(s2.pop())) {
                        return
                    }
                }
            }
        }

        /**
         * 后序非递归遍历2：采用1个栈和2个指针实现
         */
        fun <T> postTraverseNonRecur2(
            root: BinaryTree.Node<T>?,
            block: (node: BinaryTree.Node<T>) -> Boolean
        ) {
            if (root != null) {
                val stack = Stack<BinaryTree.Node<T>>()
                stack.push(root)
                var cur: BinaryTree.Node<T>?
                // head表示最近一次遍历的点
                var head: BinaryTree.Node<T> = root
                while (stack.isNotEmpty()) {
                    cur = stack.peek()
                    // 还是先压左孩子，如果左孩子没有压过的话
                    if (cur.left != null && head != cur.left && head != cur.right /*后面这个条件表示右节点还没被遍历*/) {
                        stack.push(cur.left)
                    } else if (cur.right != null && head != cur.right) {
                        // 没有左孩子，就压右孩子
                        stack.push(cur.right)
                    } else {
                        // 都没有就出栈
                        if (!block.invoke(stack.pop())) {
                            return
                        }
                        head = cur
                    }
                }
            }
        }
    }

    enum class Mode {
        Recur, NonRecur
    }
}
package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string

class TreeCount : AlgorithmModel() {
    override var name = "统计和生成所有不同的二叉树"

    override var title = "一、常规问题：给定一个整数N，如果N<1表示空树结构，否则代表遍历的结果为" +
            "{1,2...,N}，请返回可能的二叉树结构有多少？" +
            "二、进阶问题：生成这所有的子树"

    override var tips = "首先假设n个节点时，其二叉树结构有num[n]种。假设第i个节点作为根节点，" +
            "左子树有i-1个节点是，右节点有n-i个节点，这时整个子树有num[i-1] * num[n-i]种情况。" +
            "要计算出所有左右子树分布的情况并累加，就是n节点时的所有情况。" +
            "所以num[n]的大小依赖于前面num[0]..num[n-1]的值，num[0]=1，num[1]=1，逐个计算其他的值"

    override fun execute(option: Option?): ExecuteResult {
        val input = 3
        val output = generateTrees(input)
        val builder = StringBuilder()
        for (node in output) {
            builder.append(node.string())
            builder.append("\n")
        }
        return ExecuteResult(input.string(), builder.toString())
    }

    companion object {
        fun treeCount(n: Int): Int {
            if (n < 2) {
                return 1
            }
            val num = IntArray(n + 1)
            num[0] = 1
            for (i in 1..n) { // 节点数总数
                // 总结点为i的时候，左右子树的节点数
                for (j in 1..i) {
                    num[i] += num[j - 1] * num[i - j] // 扣除作为根节点的一个
                }
            }
            return num[n]
        }

        fun generateTrees(n: Int): List<BinaryTree.Node<Int>?> {
            return generate(1, n)
        }

        private fun generate(start: Int, end: Int): List<BinaryTree.Node<Int>?> {
            val res = arrayListOf<BinaryTree.Node<Int>?>()
            if (start > end) {
                res.add(null)
            }
            var head: BinaryTree.Node<Int>?
            for (i in start..end) {
                head = BinaryTree.Node(i)
                val leftChildren = generate(start, i - 1)
                val rightChildren = generate(i + 1, end)
                // 左子树 * 右子树
                for (lChild in leftChildren) {
                    for (rChild in rightChildren) {
                        head.left = lChild
                        head.right = rChild
                        res.add(cloneTree(head))
                    }
                }
            }
            return res
        }

        fun <T> cloneTree(root: BinaryTree.Node<T>?): BinaryTree.Node<T>? {
            root ?: return null
            val node = BinaryTree.Node(root.value)
            node.left = cloneTree(root.left)
            node.right = cloneTree(root.right)
            return node
        }
    }
}
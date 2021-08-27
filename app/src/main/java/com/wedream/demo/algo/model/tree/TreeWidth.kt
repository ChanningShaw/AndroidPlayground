package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max

class TreeWidth : AlgorithmModel() {
    override var name = "求一棵树的宽度"

    override var title = "求一棵树的宽度，树的宽度就是每一层的最大节点数"

    override var tips = "按层遍历，记录每一层的结束位置"

    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        val n6 = BinaryTree.Node(6)
        val n7 = BinaryTree.Node(7)
        val n8 = BinaryTree.Node(8)
        n1.left = n2
        n1.left = n2
        n1.right = n4
        n2.right = n3
        n3.right = n7
        n4.left = n5
        n4.right = n6
        n5.left = n8

        val output = getWidth(n1)
        return ExecuteResult(n1.string(), output.string())
    }

    companion object {
        fun <T> getWidth(root: BinaryTree.Node<T>): Int {
            val queue = LinkedList<BinaryTree.Node<T>>()
            queue.add(root)
            var max = Int.MIN_VALUE
            var curLevelNodes = 0
            var currentLevelEnd: BinaryTree.Node<T>? = root
            var nextLevelEnd: BinaryTree.Node<T>? = null
            while (queue.isNotEmpty()) {
                val curNode = queue.poll()
                curLevelNodes++
                if (curNode.left != null) {
                    queue.offer(curNode.left)
                    nextLevelEnd = curNode.left
                }
                if (curNode.right != null) {
                    queue.offer(curNode.right)
                    nextLevelEnd = curNode.right
                }
                if (curNode === currentLevelEnd) {
                    max = max(max, curLevelNodes)
                    curLevelNodes = 0
                    currentLevelEnd = nextLevelEnd
                    nextLevelEnd = null
                }
            }
            return max
        }
    }
}
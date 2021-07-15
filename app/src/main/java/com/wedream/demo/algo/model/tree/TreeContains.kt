package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.tree.TreeContains.Companion.checkContains
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.LogUtils.getStack
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.max
import kotlin.math.min

class TreeContains : AlgorithmModel() {
    override var name = "判断一个树是否包含另一棵树"

    override var title = "给定两棵树t1和t2，判断t1是否包含t2全部的节点"

    override var tips = ""


    override fun execute(option: Option?): ExecuteResult {
        val n1 = BinaryTree.Node(1)
        val n2 = BinaryTree.Node(2)
        val n3 = BinaryTree.Node(3)
        val n4 = BinaryTree.Node(4)
        val n5 = BinaryTree.Node(5)
        val n6 = BinaryTree.Node(6)
        val n7 = BinaryTree.Node(7)
        val n8 = BinaryTree.Node(8)
        val n9 = BinaryTree.Node(9)
        val n10 = BinaryTree.Node(10)
        n1.left = n2
        n1.right = n3
        n2.left = n4
        n2.right = n5
        n3.left = n6
        n3.right = n7
        n4.left = n8
        n4.right = n9
        n5.left = n10

        val n22 = BinaryTree.Node(2)
        val n24 = BinaryTree.Node(4)
        val n25 = BinaryTree.Node(5)
        val n28 = BinaryTree.Node(8)
        n22.left = n24
        n22.right = n25
        n24.left = n28
        val output = n1.contains(n22)
        return ExecuteResult("${n1.string()}\n ${n22.string()}", output.string())
    }

    companion object {
        fun <T> checkContains(t1: BinaryTree.Node<T>?, t2: BinaryTree.Node<T>?) : Boolean {
            if (t2 == null) {
                return true
            }
            if (t1 == null || t1.value != t2.value) {
                return false
            }
            return checkContains(t1.left, t2.left) && checkContains(t1.right, t2.right)
        }
    }
}

fun <T> BinaryTree.Node<T>.contains(root: BinaryTree.Node<T>?): Boolean {
    if (root == null) {
        return true
    }
    return checkContains(this, root)
            || left?.contains(root) == true
            || right?.contains(root) == true
}
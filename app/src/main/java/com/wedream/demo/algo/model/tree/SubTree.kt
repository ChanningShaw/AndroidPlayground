package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.algo.model.tree.SubTree.Companion.subTree
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string

class SubTree : AlgorithmModel() {
    override var name = "判断一个树是否是另一棵树的子树"

    override var title = "给定两棵树t1和t2，判断t1是否是t2的子树"

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
        n1.left = n2
        n1.right = n3
        n2.left = n4
        n2.right = n5
        n3.left = n6
        n3.right = n7
        n4.right = n8
        n5.left = n9

        val n22 = BinaryTree.Node(2)
        val n24 = BinaryTree.Node(4)
        val n25 = BinaryTree.Node(5)
        val n28 = BinaryTree.Node(8)
        val n29 = BinaryTree.Node(9)
        n22.left = n24
        n22.right = n25
        n24.right = n28
        n25.left = n29
        val output = n22.isSubTreeOf2(n1)
        return ExecuteResult("${n1.string()}\n ${n22.string()}", output.string())
    }

    companion object {
        /**
         * 判断t2是不是t1的子树
         */
        fun <T> subTree(t1: BinaryTree.Node<T>?, t2: BinaryTree.Node<T>?) : Boolean {
            if (t1 == null && t2 == null) {
                return true
            }
            if (t1 == null || t2 == null) {
                return false
            }
            return t1.value == t2.value && subTree(t1.left, t2.left) && subTree(t1.right, t2.right)
        }
    }
}

/**
 * root 是不是当前树的子树
 */
fun <T> BinaryTree.Node<T>?.isSubTreeOf1(that: BinaryTree.Node<T>?): Boolean {
    if (this == null) {
        return true
    }
    if (that == null) {
        return false
    }
    return subTree(this, that) || isSubTreeOf1(that.left) || isSubTreeOf1(that.right)
}

fun <T> BinaryTree.Node<T>?.isSubTreeOf2(that: BinaryTree.Node<T>?): Boolean {
    if (this == null) {
        return true
    }
    if (that == null) {
        return false
    }
    val thisString = TreeSerialize.serializeByPre(this)
    val thatString = TreeSerialize.serializeByPre(that)
    return KMP.getIndexOf(thatString, thisString) != -1
}
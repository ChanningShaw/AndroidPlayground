package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string

class NodeCountOfCBT : AlgorithmModel() {
    override var name = "统计完成二叉树的节点数"

    override var title = "给定一颗完全二叉树head，返回这棵树的节点个数，" +
            "要求时间复杂度低于O(N)"

    override var tips = "1. 首先利用完全二叉树的特点，最左的孩子所在的层就是树的高度求出整棵树的高度。" +
            "2. 完全二叉树所有子树中，其左右子树必有其一是满二叉树，根据右子树最左孩子所在的层数来判断左右子树谁是满的，" +
            "满的子树可以直接算出其节点个数，然后递归求解另一颗子树"

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
        n1.right = n3
        n2.left = n4
        n2.right = n5
        n3.left = n6
        n3.right = n7
        n4.left = n8
        val output = nodeCountOfCBT(n1)
        return ExecuteResult(n1.string(), output.string())
    }

    companion object {
        fun <T> nodeCountOfCBT(root: BinaryTree.Node<T>): Int {
            return treeNodeCount(root, 1, mostLeftLevel(root, 1))
        }

        private fun <T> treeNodeCount(node: BinaryTree.Node<T>?, level: Int, h: Int): Int {
            if (level == h) {
                return 1
            }
            return if (mostLeftLevel(node?.right, level + 1) == h) {
                    /* 右子树的最左孩子所在的层级为最下面的一层，说明左子树是满的
                    左子树个数是 1 shl (h - level)，继续求右子树的数目 */
                (1 shl (h - level)) + treeNodeCount(node?.right, level + 1, h)
            } else {
                /* 右子树的最左孩子所在的层级为倒数第二层，说明右子树是满的
                左子树个数是 1 shl (h - level -1)，继续求左子树的数目 */
                (1 shl (h - level - 1)) + treeNodeCount(node?.left, level + 1, h)
            }
        }

        private fun <T> mostLeftLevel(root: BinaryTree.Node<T>?, level: Int): Int {
            var result = level
            var node = root
            while (node != null) {
                result++
                node = node.left
            }
            // 因为层数是+1进来的，所以root为null时，要-1返回
            return result - 1
        }
    }
}
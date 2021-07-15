package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max

class GetMaxLengthWithSum : AlgorithmModel() {

    override var name = "在二叉树中找到累加和为指定值的最长路径"

    override var title = "给定一棵树的根节点root和一个整数sum，求累计和为sum的最长路径长度"

    override var tips = "思路和‘数组中累计和为特定值的最长子数组长度’相同，\n" +
            "使用容器sumMap<Int,Int>来记录某个累积和第一次出现的层数" +
            "递归遍历每一层，当前的累计为curSum, 当前层数为level，如果sumMap中包含了curSum - sum，" +
            "那么说明curMap[curSum -sum]层到当前的level层累积和为sum，与当前已经求得的最大路径求较大值即可。\n" +
            "另外注意，树中和数组中不同之处在于，树中有很多条路径，数组中只有一条路径，所以" +
            "当遍历完一个节点以后，如果level == sumMap[curSum]，那么需要删除，以免影响其他路径遍历"

    override fun execute(option: Option?): ExecuteResult {
        val root = BinaryTree.Node(1)
        val n1 = BinaryTree.Node(-2)
        val n2 = BinaryTree.Node(3)
        val n3 = BinaryTree.Node(4)
        val n4 = BinaryTree.Node(2)
        val n5 = BinaryTree.Node(3)
        val n6 = BinaryTree.Node(1)
        root.left = n1
        root.right = n4
        n1.left = n2
        n2.right = n3
        n4.right = n5
        n5.left = n6

        val output = getMaxLengthWithSum(root, 6)
        return ExecuteResult(root.string(), output.toString())
    }

    companion object {
        fun getMaxLengthWithSum(root: BinaryTree.Node<Int>, sum: Int): Int {
            val sumMap = hashMapOf<Int, Int>()
            sumMap[0] = 0
            return preOrder(root, sum, 0, 1, 0, sumMap)
        }

        private fun preOrder(
            node: BinaryTree.Node<Int>?,
            sum: Int,
            preSum: Int,
            level: Int,
            maxLen: Int,
            sumMap: HashMap<Int, Int>
        ): Int {
            if (node == null) {
                return maxLen
            }
            val curSum = preSum + node.value
            var curLen = maxLen
            if (!sumMap.containsKey(curSum)) {
                sumMap[curSum] = level
            }
            if (sumMap.containsKey(curSum - sum)) {
                //k + sum = cur, 说明k到level之间的路径和为sum， level - sumMap[curSum -sum]求出长度
                //并和之前已经找到的进行比较取较大值
                curLen = max(level - sumMap[curSum - sum]!!, maxLen)
            }
            curLen = preOrder(node.left, sum, curSum, level + 1, curLen, sumMap)
            curLen = preOrder(node.right, sum, curSum, level + 1, curLen, sumMap)
            if (level == sumMap[curSum]) {
                //这是当前路径中遍历的时候加上去的，遍历完以后，需要删除，以免影响其他路径遍历
                // 这是树中和数组中不同之处，因为树中有很多条路径，数组中只有一条路径
                sumMap.remove(curSum)
            }
            return curLen
        }
    }
}
package com.wedream.demo.algo.model.tree

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.util.string
import kotlin.math.max
import kotlin.math.min

class GeneratePostArray : AlgorithmModel() {
    override var name = "通过先序和中序数组生成后序数组"

    override var title = "已知一颗二叉树所有节点值都不相同，给定这棵树正确的先序和后序数组，" +
            "不重建整棵树，直接生成后续数组"

    override var tips = "从后往前构建后续数组，通过先序数组和中序数组各自对应的某个区间来构建。" +
            "为了性能优化，需要一个map保存中序数组数值和下标的对应情况，以方便快速查找数值对应的下标"


    override fun execute(option: Option?): ExecuteResult {
        val pre = intArrayOf(1, 2, 4, 5, 3, 6, 7)
        val mid = intArrayOf(4, 2, 5, 1, 6, 3, 7)
        val output = generatePostArray(pre, mid)
        return ExecuteResult("${pre.string()}, ${mid.string()}", output.string())
    }

    companion object {
        fun generatePostArray(pre: IntArray, mid: IntArray): IntArray {
            if (pre.isEmpty() || mid.isEmpty()) {
                return intArrayOf()
            }
            val map = hashMapOf<Int, Int>()
            for (i in mid.indices) {
                map[mid[i]] = i
            }
            val pos = IntArray(pre.size)
            setPost(pre, 0, pre.lastIndex, mid, 0, mid.lastIndex, pos, pos.lastIndex, map)
            return pos
        }

        private fun setPost(
            pre: IntArray, preStart: Int, preEnd: Int,
            mid: IntArray, midStart: Int, midEnd: Int,
            post: IntArray, postIndex: Int,
            midMap: HashMap<Int, Int>
        ): Int {
            if (preStart > preEnd) {
                return postIndex
            }
            var index = postIndex // 当前生成的位置
            post[index--] = pre[preStart]
            val i = midMap[pre[preStart]]!! // 中序数组中分割的位置
            // 生成后面部分
            index = setPost(
                pre, preEnd - (midEnd - i) + 1, preEnd,// (midEnd - i) 后面部分的长度
                mid, i + 1, midEnd,
                post, index, midMap
            )
            // 再生成前面部分
            return setPost(
                pre, preStart + 1, preStart + i - midStart, // i - midStart前面部分的长度
                mid, midStart, i - 1,
                post, index, midMap
            )
        }
    }
}
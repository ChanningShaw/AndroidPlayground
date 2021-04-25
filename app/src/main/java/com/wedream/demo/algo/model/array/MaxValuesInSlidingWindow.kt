package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.util.*

class MaxValuesInSlidingWindow : AlgorithmModel() {

    override var name = "生成窗口的最大数组值"

    override var title = "有一个整型数组arr和一个大小为w的窗口从数组的最左边滑到最右边，窗口每次向右滑动一个位置。\n" +
            "例如，数组为[4,3,5,4,3,3,6,7]，窗口大小为3时：依次出现的窗口为[4,3,5], [3,5,4], [5,4,3], [4,3,3], [3,3,6], [3,6,7]。\n" +
            "如果数组长度是n，窗口大小是w，则一共产生n-w+1个窗口。 \n" +
            "请实现一个函数。\n" +
            "1、输入：整型数组arr，窗口大小w \n" +
            "2、输出：一个长度大小为n-w+1的数组res，res[i]表示每一种窗口下的最大值。例如上面的例子，应该返回[5,5,5,4,6,7]"

    override var tips = "使用双端队列，遍历一遍数组，假设遍历到的位置是 i，如果队列为空或者队尾所对应的元素大于arr[i]，\n" +
            "    将位置 i 压入队列；否则将队尾元素弹出，再将 i 压入队列。此时，判断队头元素是否等于i - w，\n" +
            "    如果是的话说明此时队头已经不在当前窗口的范围内，删去。\n" +
            "    这样，这个队列就成了一个维护窗口为w的子数组的最大值更新的结构，队头元素就是每个窗口的最大值。"

    override fun execute(option: Option?): Pair<String, String> {
        val input = intArrayOf(4, 3, 5, 4, 3, 3, 6, 7)
        val result = execute(input, 3)
        return Pair(input.string() + ", 3", result.string())
    }

    companion object {
        fun execute(array: IntArray, w: Int): IntArray {
            if (array.isEmpty() || w < 1 || array.size < w) {
                return intArrayOf()
            }
            val result = IntArray(array.size - w + 1)
            val qMax = LinkedList<Int>()
            var index = 0
            for (i in array.indices) {
                while (qMax.isNotEmpty() && array[i] >= array[qMax.peekLast()]) {
                    qMax.pollLast()
                }
                qMax.addLast(i)
                if (qMax.peekFirst() == i - w) {
                    // 已经滑过，需要出列
                    qMax.pollFirst()
                }
                if (i >= w - 1) {
                    // 生成最大值
                    result[index++] = array[qMax.peekFirst()]
                }
            }
            return result
        }
    }
}
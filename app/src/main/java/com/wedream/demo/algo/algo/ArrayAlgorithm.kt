package com.wedream.demo.algo.algo

import java.util.*

object ArrayAlgorithm {
    /**
     * 生成窗口的最大数组值
     * 有一个整型数组arr和一个大小为w的窗口从数组的最左边滑到最右边，窗口每次向右滑动一个位置。
    　　例如，数组为[4,3,5,4,3,3,6,7]，窗口大小为3时：依次出现的窗口为[4,3,5], [3,5,4], [5,4,3], [4,3,3], [3,3,6], [3,6,7]。
    　　如果数组长度是n，窗口大小是w，则一共产生n-w+1个窗口。 
    　　请实现一个函数。

    　　1、输入：整型数组arr，窗口大小w 
    　　2、输出：一个长度大小为n-w+1的数组res，res[i]表示每一种窗口下的最大值。例如上面的例子，应该返回[5,5,5,4,6,7]。

    基本思路：
    使用双端队列，遍历一遍数组，假设遍历到的位置是 i，如果队列为空或者队尾所对应的元素大于arr[i]，
    将位置 i 压入队列；否则将队尾元素弹出，再将 i 压入队列。此时，判断队头元素是否等于i - w，
    如果是的话说明此时队头已经不在当前窗口的范围内，删去。
    这样，这个队列就成了一个维护窗口为w的子数组的最大值更新的结构，队头元素就是每个窗口的最大值。
     */
    fun maxValuesInSlidingWindow(array: IntArray, w: Int): IntArray {
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

    fun subArrayCountWithValueInRange(arr: IntArray, num: Int): Int {
        if (arr.isEmpty() || num < 0) {
            return 0
        }
        val qMin = LinkedList<Int>()
        val qMax = LinkedList<Int>()
        var i = 0
        var j = 0
        var res = 0
        while (i < arr.size) {
            while (j < arr.size) {
                if (qMin.isEmpty() || qMin.peekLast() != j) {
                    while (qMin.isNotEmpty() && arr[j] <= arr[qMin.peekLast()]) {
                        qMin.pollLast()
                    }
                    qMin.addLast(j)
                    while (qMax.isNotEmpty() && arr[j] >= arr[qMax.peekLast()]) {
                        qMax.pollLast()
                    }
                    qMax.addLast(j)
                }
                if (arr[qMax.first] - arr[qMin.first] > num) {
                    break
                }
                j++
            }
            res += j - i
            if (qMin.peekFirst() == i) {
                qMin.pollFirst()
            }
            if (qMax.peekFirst() == i) {
                qMax.pollFirst()
            }
            i++
        }
        return res
    }
}
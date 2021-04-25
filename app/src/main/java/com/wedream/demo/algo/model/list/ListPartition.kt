package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class ListPartition : AlgorithmModel() {

    override var name = "将单向链表按某值划分为左边小、中间相等、右边大的形式"

    override var title = "给定一个单向链表的头结点head和一个整数pivot\n，" +
            "要求将链表调整为左边小于pivot，中间等于pivot，右边大于pivot的形式\n" +
            "例如链表9->0->4->5->1，调整后的形式可以为1->0->9->4->1\n" +
            "也可以为0->1->9->5->4\n" +
            "进阶要求：\n" +
            "1. 要求调整是稳定的，即每部分内部节点前后的顺序不发生变化\n" +
            "2. 额外的空间要求为O(1)"

    override var tips = "普通解法：\n" +
            "使用节点数组来进行partition排序，然后将排序后的节点连接起来即可\n" +
            "进阶解法：\n" +
            "使用3个链表分别保存这3部分：每个链表一个头结点Head、一个尾结点Tail\n，" +
            "然后遍历原链表，把原链表断开，根据值与pivot的大小比较情况往3个链表的Tail追加节点。\n" +
            "最后连接这3个链表即可"

    override fun execute(option: Option?): Pair<String, String> {
        val head = LinkedList.Node(9)
        val n1 = LinkedList.Node(0)
        val n2 = LinkedList.Node(4)
        val n3 = LinkedList.Node(5)
        val n4 = LinkedList.Node(1)
        head.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = n4
        val input = head.string()
        val pivot = 3
        val output = listPartition2(head, pivot)
        return Pair("$input, $pivot", output.string())
    }

    companion object {
        fun listPartition1(head: LinkedList.Node<Int>, pivot: Int): LinkedList.Node<Int> {
            var cur: LinkedList.Node<Int>? = head
            var size = 0
            while (cur != null) {
                size++
                cur = cur.next

            }
            val arr = Array<LinkedList.Node<Int>?>(size) { null }
            cur = head
            for (i in arr.indices) {
                arr[i] = cur
                cur = cur?.next
            }
            arrPartition(arr, pivot)
            for (i in 0..arr.size - 2) {
                arr[i]?.next = arr[i + 1]
            }
            arr[arr.size - 1]?.next = null
            return arr[0]!!
        }

        fun listPartition2(head: LinkedList.Node<Int>, pivot: Int): LinkedList.Node<Int> {
            var sHead: LinkedList.Node<Int>? = null
            var sTail: LinkedList.Node<Int>? = null
            var eHead: LinkedList.Node<Int>? = null
            var eTail: LinkedList.Node<Int>? = null
            var bHead: LinkedList.Node<Int>? = null
            var bTail: LinkedList.Node<Int>? = null
            var cur: LinkedList.Node<Int>? = head
            var next: LinkedList.Node<Int>?

            while (cur != null) {
                next = cur.next
                cur.next = null
                if (cur.value < pivot) {
                    if (sHead == null) {
                        sHead = cur
                        sTail = cur
                    } else {
                        sTail?.next = cur
                        sTail = cur
                    }
                } else if (cur.value == pivot) {
                    if (eHead == null) {
                        eHead = cur
                        eTail = cur
                    } else {
                        eTail?.next = cur
                        eTail = cur
                    }
                } else {
                    if (bHead == null) {
                        bHead = cur
                        bTail = cur
                    } else {
                        bTail?.next = cur
                        bTail = cur
                    }
                }
                cur = next
            }
            if (sTail != null) {
                sTail.next = eHead
                eTail = eTail ?: sTail
            }
            if (eTail != null) {
                eTail.next = bHead
            }
            return sHead ?: eHead ?: bHead!!
        }

        private fun arrPartition(arr: Array<LinkedList.Node<Int>?>, pivot: Int) {
            var small = -1
            var big = arr.size
            var index = 0
            while (index != big) {
                when {
                    arr[index]!!.value < pivot -> {
                        swap(arr, ++small, index++)
                    }
                    arr[index]!!.value == pivot -> {
                        index++
                    }
                    else -> {
                        swap(arr, --big, index)
                    }
                }
            }
        }

        private fun swap(arr: Array<LinkedList.Node<Int>?>, i: Int, j: Int) {
            val temp = arr[i]
            arr[i] = arr[j]
            arr[j] = temp
        }
    }
}
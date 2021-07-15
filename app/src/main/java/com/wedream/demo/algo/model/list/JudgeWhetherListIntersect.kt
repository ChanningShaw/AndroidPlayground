package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string
import kotlin.math.abs

class JudgeWhetherListIntersect : AlgorithmModel() {

    override var name = "判断两个链表是否相交"

    override var title = "一个单链表有可能有环，也可能没有环。给定2个头结点head1和head2\n" +
            "判断这两个链表是否相交，如果相交返回第一个相交的节点，如果不想交，返回null"

    override var tips = "第一步：\n" +
            "首先判断有没有环, 使用快慢两个指针来寻找\n" +
            "然后根据有没有环，分为两种情况：\n" +
            "1)都没有环，\n这种情况2个链表分别走到底，遍历的过程中统计链表的大小，判断尾结点相不相等，\n" +
            "如果不相等，肯定不相交；如果相等，2个链表在同时从头开始遍历\n，" +
            "让长的链表走n1 - n2步，然后一起走，直到他们相等，该节点就是第一个相交的节点\n" +
            "2) 都有环，而且环的入口节点相等\n" +
            "这种情况，其实和1很像，只是不是遍历到尾结点而是遍历到该公共节点\n" +
            "3) 都有环，但是换的入口不相等\n" +
            "从一个环的入口开始往后找，如果遍历一遍都找不到另外一个环的节点，说明不相交\n" +
            "否则返回一个换的入口即可。"

    override fun execute(option: Option?): ExecuteResult {
        val head1 = LinkedList.Node(0)
        val n1 = LinkedList.Node(1)
        val n2 = LinkedList.Node(2)
        val n3 = LinkedList.Node(3)
        val n4 = LinkedList.Node(4)
        val n5 = LinkedList.Node(5).apply {
            isLast = true
        }
        head1.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = n4
        n4.next = n5
        n5.next = n4

        val head2 = LinkedList.Node(6)
        head2.next = n3
        val input = "${head1.string()}\n${head2.string()}"
        val output = judgeWhetherListIntersect(head1, head2).toString()
        return ExecuteResult(input, output)
    }

    companion object {
        fun judgeWhetherListIntersect(
            head1: LinkedList.Node<Int>,
            head2: LinkedList.Node<Int>
        ): LinkedList.Node<Int>? {
            val loopNode1 = getLoopNode(head1)
            val loopNode2 = getLoopNode(head2)
            return if ((loopNode1 == null && loopNode2 != null) || ((loopNode2 == null && loopNode1 != null))) {
                null
            } else if (loopNode1 == null && loopNode2 == null) {
                findIntersectNoLoop(head1, head2)
            } else {
                findIntersectBothLoop(head1, loopNode1!!, head2, loopNode2!!)
            }
        }

        /**
         * 获得一个链表的环的入口
         */
        fun getLoopNode(head: LinkedList.Node<Int>): LinkedList.Node<Int>? {
            if (head.next == null) {
                return null
            }
            var slow: LinkedList.Node<Int>? = head.next
            var fast: LinkedList.Node<Int>? = head.next?.next
            while (slow !== fast && fast?.next != null) {
                fast = fast.next?.next
                slow = slow?.next
            }
            return if (slow === fast) {
                fast = head
                while (slow !== fast) {
                    slow = slow?.next
                    fast = fast?.next
                }
                slow
            } else {
                null
            }
        }

        fun findIntersectNoLoop(
            head1: LinkedList.Node<Int>,
            head2: LinkedList.Node<Int>
        ): LinkedList.Node<Int>? {
            var cur1: LinkedList.Node<Int>? = head1
            var cur2: LinkedList.Node<Int>? = head2
            var n1 = 0
            var n2 = 0
            while (cur1?.next != null) {
                cur1 = cur1.next
                n1++
            }
            while (cur2?.next != null) {
                cur2 = cur2.next
                n2++
            }
            if (cur1 != cur2) {
                return null
            }
            cur1 = if (n1 > n2) head1 else head2
            cur2 = if (cur1 == head1) head2 else head1
            var n = abs(n1 - n2)
            while (n-- > 0) {
                cur1 = cur1?.next
            }
            while (cur1 != cur2) {
                cur1 = cur1?.next
                cur2 = cur2?.next
            }
            return cur1
        }

        fun findIntersectBothLoop(
            head1: LinkedList.Node<Int>,
            loop1: LinkedList.Node<Int>,
            head2: LinkedList.Node<Int>,
            loop2: LinkedList.Node<Int>
        ): LinkedList.Node<Int>? {
            if (loop1 == loop2) {
                var cur1: LinkedList.Node<Int>? = head1
                var cur2: LinkedList.Node<Int>? = head2
                var n1 = 0
                var n2 = 0
                while (cur1 !== loop1) {
                    cur1 = cur1?.next
                    n1++
                }
                while (cur2 !== loop2) {
                    cur2 = cur2?.next
                    n2++
                }
                cur1 = if (n1 > n2) head1 else head2
                cur2 = if (cur1 == head1) head2 else head1
                var n = abs(n1 - n2)
                while (n-- > 0) {
                    cur1 = cur1?.next
                }
                while (cur1 != cur2) {
                    cur1 = cur1?.next
                    cur2 = cur2?.next
                }
                return cur1
            } else {
                var cur1: LinkedList.Node<Int>? = loop1.next
                while (cur1 !== loop1) {
                    if (cur1 === loop2) {
                        return loop2
                    }
                    cur1 = cur1?.next
                }
                return null
            }
        }
    }
}
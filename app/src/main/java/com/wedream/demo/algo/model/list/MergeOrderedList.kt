package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class MergeOrderedList : AlgorithmModel() {

    override var name = "合并两个有序的单链表"

    override var title = "给定两个有序的单链表的有节点head1和head2,\n" +
            "将其合并，合并后的链表依然有序，并返回合并后的头结点"

    override var tips = "1、如果任意一个为null，则返回另一个\n" +
            "2、以较小的头结点作为新的头结点head，2个指针分别遍历两个链表，" +
            "把较小的节点连接到head后面，直到某一个指针为null，\n" +
            "3、把不为null的另外一个指针继续连接到head后面"

    override fun execute(option: Option?): Pair<String, String> {
        val head1 = LinkedList.Node(1)
        val n1 = LinkedList.Node(2)
        val n2 = LinkedList.Node(2)
        val n3 = LinkedList.Node(2)
        val n4 = LinkedList.Node(3)
        head1.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = n4

        val head2 = LinkedList.Node(2)

        val input1 = head1.string()
        val input2 = head2.string()
        val output = merge(head1, head2)
        return Pair("${input1}, $input2", output.string())
    }

    companion object {
        fun merge(
            head1: LinkedList.Node<Int>?,
            head2: LinkedList.Node<Int>?
        ): LinkedList.Node<Int>? {
            if (head1 == null) {
                return head2
            }
            if (head2 == null) {
                return head1
            }
            val newHead = if (head1.value < head2.value) head1 else head2
            var cur = newHead
            var cur1 = if (newHead === head1) head1.next else head1
            var cur2 = if (newHead === head2) head2.next else head2
            while (cur1 != null && cur2 != null) {
                if (cur1.value < cur2.value) {
                    cur.next = cur1
                    cur1 = cur1.next
                } else {
                    cur.next = cur2
                    cur2 = cur2.next
                }
                cur = cur.next!!
            }
            cur.next = cur1 ?: cur2
            return newHead
        }
    }
}
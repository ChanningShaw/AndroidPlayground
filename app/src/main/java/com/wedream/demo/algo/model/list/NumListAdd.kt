package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string
import java.util.*

class NumListAdd : AlgorithmModel() {

    override var name = "两个单链表相加生成新链表"

    override var title = "假设链表中的每一个值都在0-9之间，那么链表整体可以表示一个整数\n" +
            "例如9->3->7，可以表示整数937，给定这种链表的头结点head1和head2，\n" +
            "请生成代表两个整数相加值的结果链表"

    override var tips = "解法一：利用栈\n" +
            "分别遍历两个链表，分别压栈，\n" +
            "每次从两个栈中弹出一个数字，将相加的结果新建成节点n，\n" +
            "注意相加时可能有进位，之后的新节点添加到n的前面。\n" +
            "解法二： 逆序\n" +
            "先将2个链表逆序，然后从头到尾相加，构建新链表\n" +
            "相加完以后再逆序还原"

    override fun execute(option: Option?): Pair<String, String> {
        val head1 = LinkedList.Node(1)
        val n11 = LinkedList.Node(2)
        val n12 = LinkedList.Node(3)
        val n13 = LinkedList.Node(4)
        val n14 = LinkedList.Node(5)
        head1.next = n11
        n11.next = n12
        n12.next = n13
        n13.next = n14

        val head2 = LinkedList.Node(1)
        val n21 = LinkedList.Node(2)
        val n22 = LinkedList.Node(3)
        val n23 = LinkedList.Node(9)
        head2.next = n21
        n21.next = n22
        n22.next = n23
        val input = "${head1.string()}\n${head2.string()}"
        val output = addNumList2(head1, head2)
        return Pair(input, output.string())
    }

    companion object {
        fun addNumList1(
            head1: LinkedList.Node<Int>,
            head2: LinkedList.Node<Int>
        ): LinkedList.Node<Int> {
            val s1 = Stack<Int>()
            val s2 = Stack<Int>()
            var n1: LinkedList.Node<Int>? = head1
            var n2: LinkedList.Node<Int>? = head2

            while (n1 != null) {
                s1.push(n1.value)
                n1 = n1.next
            }
            while (n2 != null) {
                s2.push(n2.value)
                n2 = n2.next
            }
            var a = 0
            var b = 0
            var ca = 0
            var n = 0
            var head: LinkedList.Node<Int>? = null
            while (s1.isNotEmpty() || s2.isNotEmpty()) {
                a = if (s1.isEmpty()) 0 else s1.pop()
                b = if (s2.isEmpty()) 0 else s2.pop()
                n = ca + a + b
                if (n > 10) {
                    n -= 10
                    ca = 1
                } else {
                    ca = 0
                }
                val node = LinkedList.Node(n)
                node.next = head
                head = node
            }
            if (ca > 0) {
                val node = LinkedList.Node(ca)
                node.next = head
                head = node
            }
            return head!!
        }

        fun addNumList2(
            head1: LinkedList.Node<Int>,
            head2: LinkedList.Node<Int>
        ): LinkedList.Node<Int> {
            val h1 = reverseList(head1)
            val h2 = reverseList(head2)
            var cur1: LinkedList.Node<Int>? = h1
            var cur2: LinkedList.Node<Int>? = h2


            var a = 0
            var b = 0
            var ca = 0
            var n = 0
            var head: LinkedList.Node<Int>? = null
            while (cur1 != null || cur2 != null) {
                a = cur1?.value ?: 0
                b = cur2?.value ?: 0
                n = ca + a + b
                if (n > 10) {
                    n -= 10
                    ca = 1
                } else {
                    ca = 0
                }
                val node = LinkedList.Node(n)
                node.next = head
                head = node

                cur1 = cur1?.next
                cur2 = cur2?.next
            }
            reverseList(h1)
            reverseList(h2)
            return head!!
        }

        fun reverseList(head: LinkedList.Node<Int>): LinkedList.Node<Int> {
            var pre: LinkedList.Node<Int>? = null
            var next: LinkedList.Node<Int>?
            var cur: LinkedList.Node<Int>? = head
            while (cur != null) {
                next = cur.next
                cur.next = pre
                pre = cur
                cur = next
            }
            return pre!!
        }
    }
}
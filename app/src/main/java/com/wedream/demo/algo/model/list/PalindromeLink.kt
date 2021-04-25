package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string
import java.util.*

class PalindromeLink : AlgorithmModel() {

    override var name = "判断一个链表是否为回文结构"

    override var title = "给定一个链表的头结点head，判断该链表是否为回文结构"

    override var tips = "方法一：利用栈, 先遍历一次，压入全部，然后在遍历一次，同时弹出栈顶元素比较\n" +
            "方法二：基本和方法一一致，只不过只需要压一遍的元素。需要先找中间节点\n" +
            "方法三：将右半边翻转，然后同时从两头往中间遍历比较，比较完之后，恢复右边链表"

    override fun execute(option: Option?): Pair<String, String> {
        val head = LinkedList.Node(0)
        val n1 = LinkedList.Node(1)
        val n2 = LinkedList.Node(1)
        val n3 = LinkedList.Node(0).apply {
            isLast = true
        }
        head.next = n1
        n1.next = n2
        n2.next = n3
        val output = isPalindrome3(head)
        return Pair(head.string(), output.toString())
    }

    companion object {
        fun isPalindrome1(head: LinkedList.Node<Int>): Boolean {
            val stack = Stack<Int>()
            var cur: LinkedList.Node<Int>? = head
            while (cur != null) {
                stack.push(cur.value)
                cur = cur.next
            }
            cur = head
            while (cur != null) {
                if (cur.value != stack.pop()) {
                    return false
                }
                cur = cur.next
            }
            return true
        }

        fun isPalindrome2(head: LinkedList.Node<Int>): Boolean {
            if (head.next == null) {
                return true
            }
            var right: LinkedList.Node<Int>? = head
            var mid: LinkedList.Node<Int>? = head.next
            while (right?.next != null && right.next?.next != null) {
                mid = mid?.next
                right = right.next?.next
            }

            val stack = Stack<Int>()
            while (right != null) {
                stack.push(right.value)
                right = right.next
            }
            var cur: LinkedList.Node<Int>? = head
            while (stack.isNotEmpty()) {
                if (cur?.value != stack.pop()) {
                    return false
                }
                cur = cur?.next
            }
            return true
        }

        fun isPalindrome3(head: LinkedList.Node<Int>): Boolean {
            if (head.next == null) {
                return true
            }
            var n1: LinkedList.Node<Int>? = head
            var n2: LinkedList.Node<Int>? = head
            while (n2?.next != null && n2.next?.next != null) {
                n1 = n1?.next //中部
                n2 = n2.next?.next // 结尾
            }

            n2 = n1?.next
            n1?.next = null // 断掉链表以便后面翻转
            var n3: LinkedList.Node<Int>?
            while (n2 != null) {
                n3 = n2.next
                n2.next = n1
                n1 = n2
                n2 = n3
            }
            n3 = n1 // 保存最后一个节点
            n2 = head
            var result = true
            while (n1 != null && n2 != null) {
                if (n1.value != n2.value) {
                    result = false
                    break
                }
                n1 = n1.next
                n2 = n2.next
            }

            // 恢复右边链表
            n1 = n3?.next
            n3?.next = null
            while (n1 != null) {
                n2 = n1.next
                n1.next = n3
                n3 = n1
                n1 = n2
            }
            return result
        }
    }
}
package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class InsertIntoOrderCircleList : AlgorithmModel() {

    override var name = "向有序的环形单链表插入新节点"

    override var title = "一个环形单链表从头结点开始非降序，同时尾结点指回头结点\n" +
            "给定头结点head和整数num，请生成节点值为num的新节点，插入到该链表中，\n" +
            "并保证插入后链表还是有序"

    override var tips = "1. 生成新节点n\n" +
            "2. 如果链表为空，返回自己组成的新链表\n" +
            "3. 一前一后pre和cur两个指针遍历链表，直到遍历了一遍或者找到插入位置位置，\n" +
            "插入位置的条件为pre <= num <= cur，\n" +
            "如果没有找到，应该在尾部插入，并返回新节点和头结点较小者为新的头结点。"

    override fun execute(option: Option?): Pair<String, String> {
        val head = LinkedList.Node(1)
        val n1 = LinkedList.Node(2)
        val n2 = LinkedList.Node(2)
        val n3 = LinkedList.Node(2)
        val n4 = LinkedList.Node(3).apply {
            isLast = true
        }
        head.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = n4
        n4.next = head
        val input = head.string()
        val num = 5
        insertInto(head, num)
        return Pair("$input, $num", head.string())
    }

    companion object {
        fun insertInto(head: LinkedList.Node<Int>?, num: Int): LinkedList.Node<Int> {
            val newNode = LinkedList.Node(num)
            if (head == null) {
                newNode.isLast = true
                newNode.next = newNode
                return newNode
            }
            var pre = head
            var cur = head.next
            while (cur != head) {
                if (pre!!.value <= num && num <= cur!!.value) {
                    break
                }
                pre = cur
                cur = cur?.next
            }
            if (cur == head) {
                pre?.isLast = false
                newNode.isLast = true
            }
            pre?.next = newNode
            newNode.next = cur
            return if (head.value < newNode.value) head else newNode
        }
    }
}
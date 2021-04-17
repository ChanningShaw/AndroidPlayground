package com.wedream.demo.algo.model

import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class RandomPointerListCopy : AlgorithmModel() {

    override var name = "复制含有随机指针节点的链表"

    override var title = "普通链表的节点多一个指针rand，这个rand可能指向链表中的任意一个节点,\n" +
            "也有可能指向null。给定一个这样的链表的头结点Head，实现一个算法，能够完整复制该链表，\n" +
            "并返回新的头结点。"

    override var tips = "普通解法：\n" +
            "使用HashMap，保存 原节点-新节点 键值对，先遍历一遍，把键值对都填满，\n" +
            "然后再遍历一遍，有：\n" +
            "新节点的next = 原节点的next对应的新节点\n" +
            "新节点的rand = 原节点的rand对应的新节点\n"

    override fun execute(): Pair<String, String> {
        val head = LinkedList.Node(1)
        val n1 = LinkedList.Node(2)
        val n2 = LinkedList.Node(3)
        val n3 = LinkedList.Node(4)
        val n4 = LinkedList.Node(5)
        head.next = n1
        head.rand = n3
        n1.next = n2
        n2.next = n3
        n3.next = n4
        n3.rand = n1
        val input = head.string()
        val output = copyListWithRand2(head)
        return Pair(input, output.string())
    }

    companion object {
        fun copyListWithRand1(head: LinkedList.Node<Int>): LinkedList.Node<Int> {
            val map = HashMap<LinkedList.Node<Int>, LinkedList.Node<Int>>()
            var cur: LinkedList.Node<Int>? = head
            while (cur != null) {
                map[cur] = LinkedList.Node(cur.value)
                cur = cur.next
            }
            cur = head
            while (cur != null) {
                map[cur]?.next = map[cur.next]
                map[cur]?.rand = map[cur.rand]
                cur = cur.next
            }
            return map[head]!!
        }

        fun copyListWithRand2(head: LinkedList.Node<Int>): LinkedList.Node<Int> {
            var cur: LinkedList.Node<Int>? = head
            var next: LinkedList.Node<Int>?
            while (cur != null) {
                next = cur.next
                cur.next = LinkedList.Node(cur.value)
                cur.next?.next = next
                cur = next
            }

            cur = head
            var curCopy: LinkedList.Node<Int>?
            while (cur != null) {
                next = cur.next?.next
                curCopy = cur.next
                curCopy?.rand = cur.rand?.next
                cur = next
            }

            cur = head
            val res: LinkedList.Node<Int>? = head.next
            while (cur != null) {
                next = cur.next?.next
                curCopy = cur.next
                cur.next = next
                curCopy?.next = next?.next
                cur = next
            }
            return res!!
        }
    }
}
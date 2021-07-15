package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class DeleteNodeInList : AlgorithmModel() {

    override var name = "在单链表中删除指定值的节点"

    override var title = "给定一个无序单链表的头结点head和整数num\n" +
            "将链表中值为null的节点全部删除"

    override var tips = "顺序遍历，找到第一个不为num的节点作为头结点，\n" +
            "然后再继续遍历，如果节点为num，删除即可"

    override fun execute(option: Option?): ExecuteResult {
        val head = LinkedList.Node(1)
        val n1 = LinkedList.Node(2)
        val n2 = LinkedList.Node(3)
        val n3 = LinkedList.Node(2)
        val n4 = LinkedList.Node(3)
        head.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = n4
        val input = head.string()
        val num = 3
        deleteNum(head, num)
        return ExecuteResult("$input, $num", head.string())
    }

    companion object {
        fun deleteNum(head: LinkedList.Node<Int>, num: Int) {
            var cur: LinkedList.Node<Int>? = head
            while (cur != null) {
                if (cur.value != num) {
                    break
                }
                cur = cur.next
            }
            while (cur?.next != null) {
                if (cur.next!!.value == num) {
                    cur.next = cur.next?.next
                } else {
                    cur = cur.next
                }
            }
        }
    }
}
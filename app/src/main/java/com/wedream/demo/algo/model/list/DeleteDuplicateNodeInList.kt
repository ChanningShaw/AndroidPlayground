package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class DeleteDuplicateNodeInList : AlgorithmModel() {

    override var name = "删除无序单链表中的重复节点"

    override var title = "给定一个无序单链表的头结点head，删除其中值重复出现的节点"

    override var tips = "使用哈希表:\n" +
            "每遍历一个节点，查询哈希表中有没有存在，如果不存在，那就加入哈希表；\n" +
            "如果存在，就删除该节点，继续遍历下一个节点\n"

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
        deduplicate(head)
        return ExecuteResult(input, head.string())
    }

    companion object {
        fun deduplicate(head: LinkedList.Node<Int>) {
            val set = hashSetOf<LinkedList.Node<Int>>()
            set.add(head)
            var cur: LinkedList.Node<Int>? = head
            while (cur?.next != null) {
                if (set.contains(cur.next!!)) {
                    cur.next = cur.next?.next
                } else {
                    set.add(cur.next!!)
                    cur = cur.next
                }
            }
        }
    }
}
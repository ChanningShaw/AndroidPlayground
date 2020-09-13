package com.wedream.demo.algo.algo

import com.wedream.demo.algo.AlgorithmRunner
import com.wedream.demo.algo.action.AlgorithmAction
import com.wedream.demo.algo.action.MoveAction
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.LogUtils.log
import kotlin.random.Random

fun <T> LinkedList<T>.print() {
    var temp = head
    while (temp != null) {
        log { temp?.value }
        temp = temp.next
    }
}

inline fun <T> LinkedList<T>.forEach(action: (Int, LinkedList.Node<T>) -> Unit) {
    var temp = head
    var index = 0
    while (temp != null) {
        action(index, temp)
        temp = temp.next
        index++
    }
}

inline fun <T> LinkedList<T>.forEach(action: (LinkedList.Node<T>) -> Unit) {
    var temp = head
    var index = 0
    while (temp != null) {
        action(temp)
        temp = temp.next
        index++
    }
}


object LinkedListAlgorithm {
    fun randomLinkedList(size: Int): LinkedList<Int> {
        if (size < 0) {
            throw IllegalArgumentException()
        }
        val random = Random(System.currentTimeMillis())
        val list = LinkedList(random.nextInt(0, size))
        var temp: LinkedList.Node<Int>? = list.head
        for (i in 1 until size) {
            temp?.next = LinkedList.Node(random.nextInt(0, size))
            temp = temp?.next
        }
        return list
    }

    suspend fun deleteLastK(
        linkedList: LinkedList<Int>,
        k: Int,
        channel: AlgorithmRunner.ChannelWrap
    ) {
        if (linkedList.isEmpty() || k < 1) {
            return
        }
        channel.sendAction(AlgorithmAction.MessageAction("删除倒数第${k}个元素"))
        var lastKth = k
        val head = linkedList.head
        var cur = head
        channel.sendAction(MoveAction(cur))
        while (cur != null) {
            lastKth--
            cur = cur.next
            channel.sendAction(MoveAction(cur))
        }
        // 倒数第k个就是头结点
        if (lastKth == 0) {
            linkedList.head = head?.next
        }
        if (lastKth < 0) {
            cur = head
            channel.sendAction(MoveAction(cur))
            while (++lastKth != 0) {
                cur = cur?.next
                channel.sendAction(MoveAction(cur))
            }
            cur?.next = cur?.next?.next
        }
        channel.sendAction(AlgorithmAction.FinishAction)
    }
}
package com.wedream.demo.algo.structure

class LinkedList<T> {
    constructor()

    constructor(t: T) {
        head = Node(t)
    }

    constructor(node: Node<T>) {
        head = node
    }

    var head: Node<T>? = null

    fun getSize(): Int {
        var size = 0
        var temp = head
        while (temp != null) {
            size++
            temp = temp.next
        }
        return size
    }

    class Node<T>(var value: T) {
        constructor(t: T, n: T) : this(t) {
            next = Node(n)
        }

        var next: Node<T>? = null

        var rand: Node<T>? = null

        // 用于循环链表中表示是否是最后一个
        var isLast = false

        override fun toString(): String {
            return "$value, next = ${next?.value}"
        }
    }

    fun isEmpty(): Boolean {
        return head == null
    }
}
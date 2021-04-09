package com.wedream.demo.algo.structure

import java.util.*

class MinStack {
    private val stackMin = Stack<Int>()
    private val stackData = Stack<Int>()

    fun push(i: Int) {
        if (stackMin.isEmpty()) {
            stackMin.push(i)
        } else if (i <= stackMin.peek()) {
            stackMin.push(i)
        }
        stackData.push(i)
    }

    fun pop(): Int {
        if (stackData.isEmpty()) {
            throw EmptyStackException()
        }
        val i = stackData.pop()
        if (i == stackMin.peek()) {
            stackMin.pop()
        }
        return i
    }

    fun min(): Int {
        if (stackMin.isEmpty()) {
            throw EmptyStackException()
        }
        return stackMin.peek()
    }
}
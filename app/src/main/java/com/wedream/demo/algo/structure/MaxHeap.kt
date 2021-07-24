package com.wedream.demo.algo.structure

import com.wedream.demo.util.ArrayUtils.swap

class MaxHeap(private val capacity: Int) {

    private val data = Array(capacity) { 0 }
    private var last = -1

    fun push(value: Int) {
        data[++last] = value
        bottomUpAdjust(last)
    }

    fun pop(): Int {
        val result = data[0]
        topDownAdjust(0)
        last--
        return result
    }

    fun replace(value: Int) {
        data[0] = value
        topDownAdjust(0)
    }

    fun size(): Int {
        return last + 1
    }

    fun peek(): Int {
        return data[0]
    }

    /**
     * 自下而上调整
     */
    private fun bottomUpAdjust(index: Int) {
        var i = index
        while (i > 0) {
            val parent = (i - 1) / 2
            if (data[parent] < data[i]) {
                swap(parent, i, data)
                i = parent
            } else {
                break
            }
        }
    }

    /**
     * 自上而下调整
     */
    private fun topDownAdjust(index: Int) {
        var cur = index
        var left = cur * 2 + 1
        var right = cur * 2 + 1
        var biggerIndex = cur
        while (left < capacity) {
            if (data[left] > data[cur]) {
                biggerIndex = cur
            }
            if (right < capacity && data[right] > data[biggerIndex]) {
                biggerIndex = right
            }
            if (biggerIndex != cur) {
                swap(biggerIndex, cur, data)
            } else {
                break
            }
            cur = biggerIndex
            left = cur * 2 + 1
            right = cur * 2 + 1
        }
    }

    fun toIntArray(): IntArray {
        val arr = IntArray(size())
        for (i in 0..last) {
            arr[i] = data[i]
        }
        return arr
    }
}
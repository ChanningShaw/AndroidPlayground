package com.wedream.demo.algo.structure

import com.wedream.demo.util.ArrayUtils.swap

class MinHeap<T : Comparable<T>>(private val capacity: Int) {

    private val data = ArrayList<T>(capacity)
    private var last = -1

    fun push(t: T) {
        data.add(++last, t)
        bottomUpAdjust(last)
    }

    fun pop(): T {
        val result = data[0]
        data[0] = data[last]
        topDownAdjust(0)
        last--
        return result
    }

    fun replace(t: T) {
        data[0] = t
        topDownAdjust(0)
    }

    fun size(): Int {
        return last + 1
    }

    fun peek(): T {
        return data[0]
    }

    /**
     * 自下而上调整
     */
    private fun bottomUpAdjust(index: Int) {
        var i = index
        while (i > 0) {
            val parent = (i - 1) / 2
            if (data[i] < data[parent]) {
                swap(data, parent, i)
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
        var right = cur * 2 + 2
        var smallerIndex = cur
        while (left < capacity) {
            if (data[left] < data[cur]) {
                smallerIndex = left
            }
            if (right < capacity && data[right] < data[smallerIndex]) {
                smallerIndex = right
            }
            if (smallerIndex != cur) {
                swap(data, smallerIndex, cur)
            } else {
                break
            }
            cur = smallerIndex
            left = cur * 2 + 1
            right = cur * 2 + 1
        }
    }

    fun toIntArray(): IntArray {
        val arr = IntArray(size())
        for (i in 0..last) {
            arr[i] = data[i] as Int
        }
        return arr
    }
}
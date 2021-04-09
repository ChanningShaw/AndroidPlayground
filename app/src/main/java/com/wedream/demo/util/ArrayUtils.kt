package com.wedream.demo.util

import com.wedream.demo.util.LogUtils.log
import kotlin.random.Random

fun <T> Array<T>.print() {
    val builder = StringBuilder()
    builder.append('[')
    for ((i, t) in withIndex()) {
        builder.append(t)
        if (i != lastIndex) {
            builder.append(',')
        }
    }
    builder.append(']')
    log { builder.toString() }
}

fun IntArray.print() {
    val builder = StringBuilder()
    builder.append('[')
    for ((i, t) in withIndex()) {
        builder.append(t)
        if (i != lastIndex) {
            builder.append(',')
        }
    }
    builder.append(']')
    log { builder.toString() }
}


object ArrayUtils {
    fun randomArray(size: Int): Array<Int> {
        val array = Array(size) { 0 }
        val random = Random(System.currentTimeMillis())
        for (i in array.indices) {
            array[i] = random.nextInt(1, size)
        }
        return array
    }

    fun randomArray(size: Int, from: Int, to: Int): Array<Int> {
        val array = Array(size) { 0 }
        val random = Random(System.currentTimeMillis())
        for (i in array.indices) {
            array[i] = random.nextInt(from, to)
        }
        return array
    }

    fun print(data: Array<Int>) {
        data.forEach {
            log { "$it" }
        }
    }
}
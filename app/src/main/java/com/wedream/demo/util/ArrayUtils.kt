package com.wedream.demo.util

import com.wedream.demo.util.LogUtils.log
import kotlin.random.Random

fun <T> Array<T>.randomPeak(): T {
    return this[0]
}

fun <T> Array<T>.print() {
    for (t in this) {
        log(t.toString())
    }
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
            log("$it")
        }
    }
}
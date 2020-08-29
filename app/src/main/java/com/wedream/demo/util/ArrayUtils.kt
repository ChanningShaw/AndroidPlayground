package com.wedream.demo.util

import kotlin.random.Random

fun <T> Array<T>.randomPeak(): T {
    return this[0]
}

object ArrayUtils{
    fun randomArray(size: Int): Array<Int> {
        val array = Array(size) { 0 }
        val random = Random(System.currentTimeMillis())
        for (i in array.indices) {
            array[i] = random.nextInt(1, size)
        }
        return array
    }
}
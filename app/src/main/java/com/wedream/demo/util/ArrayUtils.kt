package com.wedream.demo.util

import com.wedream.demo.util.LogUtils.log
import kotlin.random.Random

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

    fun <T> swap(data: Array<T>, i: Int, j: Int) {
        val temp = data[i]
        data[i] = data[j]
        data[j] = temp
    }

    fun swap(arr: IntArray, i: Int, j: Int) {
        val temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }

    fun swap(arr: CharArray, i: Int, j: Int) {
        val temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }

    fun <T> swap(arr: ArrayList<T>, i: Int, j: Int) {
        val temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }

    fun insertSort(arr: IntArray, start: Int, end: Int) {
        for (i in (start + 1)..end) {
            for (j in i downTo start + 1) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j)
                } else {
                    break
                }
            }
        }
    }

    fun print(data: Array<Int>) {
        data.forEach {
            log { "$it" }
        }
    }

    fun copyArray(array: IntArray): IntArray {
        val dst = IntArray(array.size)
        System.arraycopy(array, 0, dst, 0, array.size)
        return dst
    }
}
package com.wedream.demo.util

import kotlin.math.sqrt

/**
 * 二元一次方程
 */
object BinaryLinearEquation {
    fun solve(a: Float, b: Float, c: Float): List<Float> {
        val delta = b * b - 4 * a * c
        return when {
            delta < 0f -> {
                emptyList()
            }
            delta == 0f -> {
                val x = -b / (2 * a)
                listOf(x)
            }
            else -> {
                val x1 = (-b + sqrt(delta)) / (2 * a)
                val x2 = (-b - sqrt(delta)) / (2 * a)
                listOf(x1, x2)
            }
        }
    }
}
package com.wedream.demo.util

import java.lang.Exception

object MathUtils {
    fun <T : Number> T.half(): T {
        when (this) {
            is Int -> {
                return (this * 0.5).toInt() as T
            }
            is Long -> {
                return (this * 0.5).toInt() as T
            }
            is Float -> {
                return (this * 0.5).toInt() as T
            }
            is Double -> {
                return (this * 0.5).toInt() as T
            }
        }
        throw Exception("not yet support")
    }
}
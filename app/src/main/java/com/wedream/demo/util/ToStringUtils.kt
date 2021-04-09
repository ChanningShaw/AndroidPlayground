package com.wedream.demo.util


fun <T> Array<T>.string(): String {
    val builder = StringBuilder()
    builder.append('[')
    for ((i, t) in withIndex()) {
        when (t) {
            is Array<*> -> {
                builder.append(t.string())
            }
            is IntArray -> {
                builder.append(t.string())
            }
            else -> {
                builder.append(t)
            }
        }
        if (i != lastIndex) {
            builder.append(',')
        }
    }
    builder.append(']')
    return builder.toString()
}

fun IntArray.string(): String {
    val builder = StringBuilder()
    builder.append('[')
    for ((i, t) in withIndex()) {
        builder.append(t)
        if (i != lastIndex) {
            builder.append(',')
        }
    }
    builder.append(']')
    return builder.toString()
}
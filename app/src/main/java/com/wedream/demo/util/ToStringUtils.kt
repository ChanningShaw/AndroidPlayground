package com.wedream.demo.util


fun <T> T.string(): String {
    val builder = StringBuilder()
    when (this) {
        is Array<*> -> {
            builder.append('[')
            for ((i, t) in this.withIndex()) {
                when (t) {
                    is Array<*> -> {
                        builder.append("\n")
                        builder.append(t.string())
                        if (i != this.lastIndex) {
                            builder.append(',')
                        }
                        builder.append("\n")
                    }
                    is IntArray -> {
                        builder.append("\n")
                        builder.append(t.string())
                        if (i != this.lastIndex) {
                            builder.append(',')
                        }
                        builder.append("\n")
                    }
                    else -> {
                        builder.append(t)
                        if (i != this.lastIndex) {
                            builder.append(',')
                        }
                    }
                }
            }
            builder.append(']')
        }
        is IntArray -> {
            builder.append('[')
            for ((i, t) in withIndex()) {
                builder.append(t)
                if (i != lastIndex) {
                    builder.append(',')
                }
            }
            builder.append(']')
        }
        else -> {
            builder.append(this.toString())
        }
    }
    return builder.toString()
}
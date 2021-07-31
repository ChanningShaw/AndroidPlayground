package com.wedream.demo.util

import com.wedream.demo.algo.model.tree.PrintTreeNode
import com.wedream.demo.algo.structure.BinaryTree
import com.wedream.demo.algo.structure.LinkedList


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
                    is IntArray, is DoubleArray -> {
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
        is DoubleArray -> {
            builder.append('[')
            for ((i, t) in withIndex()) {
                builder.append(t)
                if (i != lastIndex) {
                    builder.append(',')
                }
            }
            builder.append(']')
        }
        is LinkedList.Node<*> -> {
            builder.append(value)
            if (rand != null) {
                builder.append("(${rand?.value})")
            }
            builder.append("->")
            when {
                next == null -> {
                    builder.append("null")
                }
                isLast -> {
                    builder.append("${next?.value}")
                }
                else -> {
                    builder.append(next.string())
                }
            }
        }
        is BinaryTree.Node<*> -> {
            builder.append(PrintTreeNode.printTree(this))
        }

        else -> {
            builder.append(toString())
        }
    }
    return builder.toString()
}
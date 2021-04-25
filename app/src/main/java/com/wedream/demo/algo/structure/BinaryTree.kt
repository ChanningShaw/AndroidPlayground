package com.wedream.demo.algo.structure

class BinaryTree {
    class Node<T>(var value: T) {
        var left: Node<T>? = null
        var right: Node<T>? = null

        // 在树的第几层
        var level = -1

        override fun toString(): String {
            return "$value, left = ${left?.value}, right = ${right?.value}"
        }

        override fun equals(other: Any?): Boolean {
            if (other is Node<*>) {
                if (value != other.value) {
                    return false
                }
                return true
            }
            return false
        }

        override fun hashCode(): Int {
            return value?.hashCode() ?: 0
        }
    }
}
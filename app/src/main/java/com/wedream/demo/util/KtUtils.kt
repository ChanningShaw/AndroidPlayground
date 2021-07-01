package com.wedream.demo.util

object KtUtils {
    fun <T> T?.ifNullAndElse(nullBlock: () -> Unit, block: (t: T) -> Unit) {
        if (this == null) {
            nullBlock.invoke()
        } else {
            block.invoke(this)
        }
    }
}
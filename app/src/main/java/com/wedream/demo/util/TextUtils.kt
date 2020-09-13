package com.wedream.demo.util

import android.graphics.Paint

object TextUtils {
    fun getTextWith(text: String, paint: Paint) {
        val arr = floatArrayOf(0f)
        paint.getTextWidths(text, arr)
    }
}
package com.wedream.demo.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View


/**
 * 根据指定的Activity截图（带空白的状态栏）
 *
 * @return Bitmap
 */
fun Activity.shot(): Bitmap {
    val view: View = window.decorView
    val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}
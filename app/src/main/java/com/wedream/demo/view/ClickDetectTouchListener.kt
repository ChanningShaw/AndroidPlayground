package com.wedream.demo.view

import android.view.MotionEvent
import android.view.View
import com.wedream.demo.util.LogUtils.log
import java.lang.Math.abs

class ClickDetectTouchListener : View.OnTouchListener {

    private var lastX = 0f
    private var lastY = 0f

    @Override
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastY = y
                lastX = x
                // 记录触摸点坐标
            }
            MotionEvent.ACTION_UP -> {
                if (abs(y - lastY) < 5 && abs(x - lastX) < 5) {
                    //如果横纵坐标的偏移量都小于五个像素，那么就把它当做点击事件触发
                    log { "onclick" }
                }
            }
        }
        return false
    }
}
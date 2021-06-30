package com.wedream.demo.videoeditor.timeline.widget

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.wedream.demo.app.ApplicationHolder
import kotlin.math.abs

abstract class ClickDetectTouchListener : View.OnTouchListener {

    private var lastX = 0f
    private var lastY = 0f
    private val touchSlop = ViewConfiguration.get(ApplicationHolder.instance).scaledTouchSlop

    @Override
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastY = y
                lastX = x
            }
            MotionEvent.ACTION_UP -> {
                if (abs(y - lastY) < touchSlop && abs(x - lastX) < touchSlop) {
                    onclick(v)
                }
            }
        }
        return false
    }

    abstract fun onclick(view: View)
}

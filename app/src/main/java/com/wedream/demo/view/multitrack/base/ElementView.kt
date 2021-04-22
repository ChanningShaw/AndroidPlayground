package com.wedream.demo.view.multitrack.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.util.Range
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.util.LogUtils.log
import kotlin.math.abs

abstract class ElementView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var eventListener: ElementEventListener? = null

    private var downX = 0
    private var downY = 0

    private var isLongPress = false
    private var downTime = 0L
    private val myHandler = Handler() {
        onLongPress()
        true
    }

    companion object {
        const val LONG_PRESS_TIME = 300L
        const val CLICK_AREA = 25
    }

    fun setElementEventListener(listener: ElementEventListener) {
        eventListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        if (eventListener == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.toInt()
                downY = event.y.toInt()
                isLongPress = false
                downTime = System.currentTimeMillis()
                val msg = Message.obtain()
                msg.what = 0
                myHandler.sendMessageDelayed(msg, LONG_PRESS_TIME)
                eventListener?.onActionDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                eventListener?.onMove(this, event.x.toInt() - downX, event.y.toInt() - downY)
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - downTime < LONG_PRESS_TIME) {
                    isLongPress = false
                    myHandler.removeMessages(0)
                    if (abs(downX - event.x) < CLICK_AREA && abs(downY - event.y) < CLICK_AREA) {
                        eventListener?.onClick(this)
                    }
                }
                eventListener?.onActionUp(this, event.x.toInt() - downX, event.y.toInt() - downY)
            }
            MotionEvent.ACTION_CANCEL -> {
                isLongPress = false
                myHandler.removeMessages(0)
            }
        }
        return true
    }

    private fun onLongPress() {
        eventListener?.onLongPress(this)
        isLongPress = true
    }

    fun isLongPressed(): Boolean {
        return isLongPress
    }

    interface ElementEventListener {
        fun onActionDown(view: ElementView)
        fun onMove(view: ElementView, deltaX: Int, deltaY: Int)
        fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int)
        fun onLongPress(view: ElementView)
        fun onClick(view: ElementView)
    }
}
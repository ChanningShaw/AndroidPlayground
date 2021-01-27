package com.wedream.demo.view.multitrack.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

abstract class ElementView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var eventListener: ElementEventListener? = null

    private var downX = 0f
    private var downY = 0f

    private var isLongPress = false
    private var downTime = 0L
    private val myHandler = Handler() {
        onLongPress()
        true
    }

    companion object {
        const val LONG_PRESS_TIME = 500L
        const val CLICK_AREA = 25
    }

    fun getElementEventListener(): ElementEventListener? {
        return eventListener
    }

    fun setSegmentEventListener(listener: ElementEventListener) {
        eventListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                isLongPress = false
                downTime = System.currentTimeMillis()
                val msg = Message.obtain()
                msg.what = 0
                myHandler.sendMessageDelayed(msg, LONG_PRESS_TIME)
                getElementEventListener()?.onActionDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                getElementEventListener()?.onMove(this, event.x - downX, event.y - downY)
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - downTime < LONG_PRESS_TIME) {
                    isLongPress = false
                    myHandler.removeMessages(0)
                    if (abs(downX - event.x) < CLICK_AREA && abs(downY - event.y) < CLICK_AREA) {
                        getElementEventListener()?.onClick(this)
                    }
                }
                getElementEventListener()?.onActionUp(this, event.x - downX, event.y - downY)
            }
            MotionEvent.ACTION_CANCEL -> {
                isLongPress = false
                myHandler.removeMessages(0)
            }
        }
        return true
    }

    private fun onLongPress() {
        getElementEventListener()?.onLongPress(this)
        isLongPress = true
    }

    fun isLongPressed(): Boolean {
        return isLongPress
    }

    interface ElementEventListener {
        fun onActionDown(view: ElementView)
        fun onMove(view: ElementView, deltaX: Float, deltaY: Float)
        fun onActionUp(view: ElementView, deltaX: Float, deltaY: Float)
        fun onLongPress(view: ElementView)
        fun onClick(view: ElementView)
    }
}
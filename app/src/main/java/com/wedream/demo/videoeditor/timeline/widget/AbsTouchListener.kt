package com.wedream.demo.videoeditor.timeline.widget

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

abstract class AbsTouchListener(context: Context, private val relative: Boolean) : View.OnTouchListener {

    private var downX = 0.0f
    private var downY = 0.0f

    private var isLongPress = false
    private var downTime = 0L
    private var inMoving = false
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private val myHandler = Handler {
        onLongPress(it.obj as View)
        true
    }

    companion object {
        const val LONG_PRESS_TIME = 500L
        const val MSG_LONG_PRESSED = 0
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (relative) {
                    downX = event.x
                    downY = event.y
                } else {
                    downX = event.rawX
                    downY = event.rawY
                }
                isLongPress = false
                downTime = System.currentTimeMillis()
                val msg = Message.obtain()
                msg.obj = v
                myHandler.sendMessageDelayed(msg, LONG_PRESS_TIME)
                onActionDown(v, downX.toInt(), downY.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = if (relative) (event.x - downX).toInt() else (event.rawX - downX).toInt()
                val deltaY = if (relative) (event.y - downY).toInt() else (event.rawY - downY).toInt()
//        if (abs(deltaX) > touchSlop || abs(deltaY) > touchSlop) {
                if (!inMoving) {
                    inMoving = true
                    onMoveStart(v, deltaX, deltaY)
                } else {
                    onMoving(v, deltaX, deltaY)
                }
//        }
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = if (relative) (event.x - downX).toInt() else (event.rawX - downX).toInt()
                val deltaY = if (relative) (event.y - downY).toInt() else (event.rawY - downY).toInt()
                if (System.currentTimeMillis() - downTime < LONG_PRESS_TIME) {
                    isLongPress = false
                    myHandler.removeMessages(MSG_LONG_PRESSED)
                    if (abs(deltaX) < touchSlop && abs(deltaY) < touchSlop) {
                        onClick(v)
                    }
                }
                if (inMoving) {
                    inMoving = false
                    onMoveStop(v, deltaX, deltaY)
                }
                onActionUp(v, deltaX, deltaY)
            }
            MotionEvent.ACTION_CANCEL -> {
                val deltaX = if (relative) (event.x - downX).toInt() else (event.rawX - downX).toInt()
                val deltaY = if (relative) (event.y - downY).toInt() else (event.rawY - downY).toInt()
                isLongPress = false
                if (inMoving) {
                    inMoving = false
                    onMoveStop(v, deltaX, deltaY)
                }
                myHandler.removeMessages(MSG_LONG_PRESSED)
                onActionCancel(v)
            }
        }
        return true
    }

    abstract fun onActionDown(v: View, x: Int, y: Int)
    abstract fun onMoveStart(v: View, deltaX: Int, deltaY: Int)
    abstract fun onMoving(v: View, deltaX: Int, deltaY: Int)
    abstract fun onMoveStop(v: View, deltaX: Int, deltaY: Int)
    abstract fun onActionUp(v: View, deltaX: Int, deltaY: Int)
    abstract fun onActionCancel(v: View)
    abstract fun onLongPress(v: View)
    abstract fun onClick(v: View)
}

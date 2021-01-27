package com.wedream.demo.view.multitrack

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import kotlin.math.abs

class SegmentView(context: Context, attrs: AttributeSet?, defStyle: Int) : FrameLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var segmentEventListener: SegmentEventListener? = null

    private var downX = 0f
    private var downY = 0f

    private var isLongPress = false
    private var downTime = 0L
    private val myHandler = Handler() {
        onLongPress()
        true
    }

    init {
        setBackgroundResource(R.color.marker_text_style_b_color)
    }

    companion object {
        const val LONG_PRESS_TIME = 500L
        const val CLICK_AREA = 25
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
                segmentEventListener?.onActionDown(this)
            }
            MotionEvent.ACTION_MOVE -> {
                if (isLongPress) {
                    segmentEventListener?.onMove(this, event.x - downX, event.y - downY)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - downTime < LONG_PRESS_TIME) {
                    isLongPress = false
                    myHandler.removeMessages(0)
                    if (abs(downX - event.x) < CLICK_AREA && abs(downY - event.y) < CLICK_AREA) {
                        onClick()
                    }
                }
                segmentEventListener?.onActionUp(this, event.x - downX, event.y - downY)
            }
            MotionEvent.ACTION_CANCEL -> {
                isLongPress = false
                myHandler.removeMessages(0)
            }
        }
        return true
    }

    private fun onClick() {
        segmentEventListener?.onClick(this)
    }

    private fun onLongPress(){
        segmentEventListener?.onLongPress(this)
        isLongPress = true
    }

    fun setSegmentEventListener(listener: SegmentEventListener) {
        segmentEventListener = listener
    }

    interface SegmentEventListener {
        fun onActionDown(view: SegmentView)
        fun onMove(view: SegmentView, deltaX: Float, deltaY: Float)
        fun onActionUp(view: SegmentView, deltaX: Float, deltaY: Float)
        fun onLongPress(view: SegmentView)
        fun onClick(view: SegmentView)
    }
}
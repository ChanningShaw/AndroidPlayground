package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class SegmentView(context: Context, attrs: AttributeSet?, defStyle: Int) : FrameLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var segmentEventListener: SegmentEventListener? = null

    private var downX = 0f
    private var downY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                segmentEventListener?.onActionDown()
            }
            MotionEvent.ACTION_MOVE -> {
                segmentEventListener?.onMove(event.x - downX, event.y - downY)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                segmentEventListener?.onActionUp(event.x - downX, event.y - downY)
            }
        }
        return true
    }

    fun setSegmentEventListener(listener: SegmentEventListener) {
        segmentEventListener = listener
    }

    interface SegmentEventListener {
        fun onActionDown()
        fun onMove(deltaX: Float, deltaY: Float)
        fun onActionUp(deltaX: Float, deltaY: Float)
    }
}
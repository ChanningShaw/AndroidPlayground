package com.wedream.demo.videoeditor.timeline.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import com.wedream.demo.videoeditor.timeline.utils.OperateScaleHelper

class TimelineAxisView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    ConstraintLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val operateScaleHelper = OperateScaleHelper(this)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return operateScaleHelper.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return operateScaleHelper.onTouchEvent(event)
    }

    fun setOnScaleListener(onScaleListener: OperateScaleHelper.OnScaleListener) {
        operateScaleHelper.setOnScaleListener(onScaleListener)
    }
}
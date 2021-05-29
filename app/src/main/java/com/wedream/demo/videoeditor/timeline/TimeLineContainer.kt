package com.wedream.demo.videoeditor.timeline

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup

class TimeLineContainer(context: Context, attrs: AttributeSet?, defStyle: Int) : ViewGroup(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}
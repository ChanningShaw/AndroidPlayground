package com.wedream.demo.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawTextDemoView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()

    init {
        paint.textSize = 80f
        paint.strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {

            paint.color = Color.BLUE
            val baseLine = height * 0.5f
            it.drawText("xiaochunming'blog 博客来了一二三四", 0f, baseLine, paint)

            paint.color = Color.GREEN
            it.drawLine(0f, baseLine, width.toFloat(), baseLine, paint)

            val ascent = paint.fontMetrics.ascent + baseLine
            paint.color = Color.RED
            it.drawLine(0f, ascent, width.toFloat(), ascent, paint)

            val descent = paint.fontMetrics.descent + baseLine
            paint.color = Color.YELLOW
            it.drawLine(0f, descent, width.toFloat(), descent, paint)

            val top = paint.fontMetrics.top + baseLine
            paint.color = Color.BLACK
            it.drawLine(0f, top, width.toFloat(), top, paint)

            val bottom = paint.fontMetrics.bottom + baseLine
            paint.color = Color.GRAY
            it.drawLine(0f, bottom, width.toFloat(), bottom, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        invalidate()
        return true
    }
}
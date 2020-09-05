package com.wedream.demo.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawTextDemoView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()

    init {
        paint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {

            paint.style = Paint.Style.FILL
            paint.textSize = 40f
            paint.color = Color.BLACK
            it.drawText("top", 0f, 50f, paint)
            paint.color = Color.BLUE
            it.drawText("baseLine", 0f, 100f, paint)
            paint.color = Color.GRAY
            it.drawText("bottom", 0f, 150f, paint)
            paint.color = Color.GREEN
            it.drawText("(bottom + top) * 0.5f + baseLine", 0f, 200f, paint)
            paint.color = Color.CYAN
            it.drawText("getTextBounds", 0f, 250f, paint)

            paint.textSize = 80f
            paint.color = Color.BLUE
            val baseLine = height * 0.5f
            it.drawText("-39ABxyao哈哈", 0f, baseLine, paint)

            paint.color = Color.BLUE
            it.drawLine(0f, baseLine, width.toFloat(), baseLine, paint)

            val top = paint.fontMetrics.top + baseLine
            paint.color = Color.BLACK
            it.drawLine(0f, top, width.toFloat(), top, paint)

            val bottom = paint.fontMetrics.bottom + baseLine
            paint.color = Color.GRAY
            it.drawLine(0f, bottom, width.toFloat(), bottom, paint)

            val center = (paint.fontMetrics.bottom + paint.fontMetrics.top) * 0.5f + baseLine
            paint.color = Color.GREEN
            it.drawLine(0f, center, width.toFloat(), center, paint)

            paint.style = Paint.Style.STROKE
            val rect = Rect()
            paint.getTextBounds("-39ABxyao哈哈", 0, 11, rect)
            rect.top += baseLine.toInt()
            rect.bottom += baseLine.toInt()
            paint.color = Color.CYAN
            it.drawLine(0f, rect.centerY().toFloat(), width.toFloat(), rect.centerY().toFloat(), paint)
            it.drawRect(rect, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        invalidate()
        return true
    }
}
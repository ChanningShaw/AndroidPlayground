package com.wedream.demo.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class MyCanvasView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val highlightRect = Rect()

    init {
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(highlightRect, paint)
    }

    fun setRect(left: Int, top: Int, right: Int, bottom: Int) {
        highlightRect.set(left, top, right, bottom)
        invalidate()
    }
}
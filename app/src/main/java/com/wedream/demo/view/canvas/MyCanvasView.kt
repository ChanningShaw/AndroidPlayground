package com.wedream.demo.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SegmentView
import kotlin.random.Random

class MyCanvasView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val highlightRect = Rect()
    private var targetViews = arrayListOf<View>()

    init {
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(highlightRect, paint)
        for (view in targetViews) {
            view.draw(canvas)
        }
    }

    fun setRect(left: Int, top: Int, right: Int, bottom: Int) {
        highlightRect.set(left, top, right, bottom)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun drawView(view: View, left: Int, top: Int, width: Int, height: Int) {
        view.layoutParams = ViewGroup.LayoutParams(width, height)
        view.layout(left, top, left + width, top + height)
        view.setBackgroundResource(R.color.color_green)
        targetViews.add(view)
    }
}
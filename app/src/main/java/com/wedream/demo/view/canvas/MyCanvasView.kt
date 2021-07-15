package com.wedream.demo.view.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SegmentView
import kotlin.random.Random

class MyCanvasView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    AppCompatImageView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val highlightRect = Rect()
    private var color = Color.parseColor("#AACCCCCC")
    private var targetViews = arrayListOf<View>()

    private val colorMatrix = ColorMatrix(floatArrayOf(
        0.33f, 0.33f, 0.33f, 0f, 0f,
        0.33f, 0.33f, 0.33f, 0f, 0f,
        0.33f, 0.33f, 0.33f, 0f, 0f,
        0.0f, 0.0f, 0.0f, 0.9f, 0f
    ))

    private val colorFilter = ColorMatrixColorFilter(colorMatrix)

    init {
        paint.color = Color.WHITE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        setColorFilter(colorFilter)
    }

    override fun onDraw(canvas: Canvas) {
        for (view in targetViews) {
            view.draw(canvas)
        }
//        canvas.drawColor(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun setRect(left: Int, top: Int, right: Int, bottom: Int) {
        highlightRect.set(left, top, right, bottom)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    fun drawView(view: View, left: Int, top: Int, width: Int, height: Int) {
        view.layoutParams = ViewGroup.LayoutParams(width, height)
        view.layout(left, top, left + width, top + height)
        view.setBackgroundResource(R.color.color_green)
        targetViews.add(view)
    }
}
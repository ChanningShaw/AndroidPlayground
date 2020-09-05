package com.wedream.demo.render

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.planegeometry.collapse

class DrawPathView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val rectPaint = Paint()
    private val path = Path()
    private var rectF = RectF(100f, 100f, 200f, 200f)
    private var weakenRight = 0f

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.color = Color.GRAY

        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawRect(rectF, rectPaint)
            path.reset()
            path.moveTo(rectF.left, rectF.centerY())
            paint.style = Paint.Style.FILL
            paint.color = Color.GRAY
            path.quadTo(rectF.left + (weakenRight - rectF.left) * 0.1f, rectF.top, weakenRight, rectF.top);
            path.lineTo(rectF.left, rectF.top)
            path.lineTo(rectF.left, rectF.centerY())
            path.close()
            it.drawPath(path, paint);

            path.reset()
            path.moveTo(rectF.left, rectF.centerY())
            path.quadTo(rectF.left + (weakenRight - rectF.left) * 0.1f, rectF.top, weakenRight, rectF.top);
            paint.style = Paint.Style.STROKE
            paint.color = Color.BLUE
            it.drawPath(path, paint);
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        rectF.set(left.toFloat(), 100f, right.toFloat(), 300f)
        rectF.collapse(30f)
        weakenRight = rectF.left
    }

    fun setVoiceWeakenLength(value: Float) {
        weakenRight = rectF.left + value / 100f * rectF.width()
        invalidate()
    }
}
package com.wedream.demo.render

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.planegeometry.fitRect

class MatrixDemoView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val mat = Matrix()
    private val boundRect = RectF()
    private var rectF = RectF(100f, 100f, 200f, 200f)
    private var outRect = RectF()
    private var outPoints = arrayListOf<PointF>()
    private var rotate = 0f

    init {

    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            paint.color = Color.RED
            it.drawRect(0f, 0f, 100f, 100f, paint)
            it.drawRect(rectF, paint)
            paint.color = Color.BLUE
            paint.strokeWidth = 5f
            if (outPoints.isNotEmpty()) {
                it.drawLine(outPoints[0].x, outPoints[0].y, outPoints[1].x, outPoints[1].y, paint)
                it.drawLine(outPoints[0].x, outPoints[0].y, outPoints[2].x, outPoints[2].y, paint)
                it.drawLine(outPoints[2].x, outPoints[2].y, outPoints[3].x, outPoints[3].y, paint)
                it.drawLine(outPoints[1].x, outPoints[1].y, outPoints[3].x, outPoints[3].y, paint)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        boundRect.set(left.toFloat() + 40, top.toFloat(), right.toFloat() - 140, bottom.toFloat())
        rectF.set(boundRect.fitRect(2f))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        invalidate()
        return true
    }

    fun setRotate(value: Float) {
        this.rotate = value
        val points = floatArrayOf(rectF.left, rectF.top,
            rectF.right, rectF.top,
            rectF.left, rectF.bottom,
            rectF.right, rectF.bottom)
        mat.reset()
        mat.postRotate(value, rectF.centerX(), rectF.centerY())
        mat.mapPoints(points)
        outPoints.clear()
        outPoints.add(PointF(points[0], points[1]))
        outPoints.add(PointF(points[2], points[3]))
        outPoints.add(PointF(points[4], points[5]))
        outPoints.add(PointF(points[6], points[7]))
        invalidate()
    }
}
package com.wedream.demo.planegeometry

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.util.*
import com.wedream.demo.util.PlaneGeometryUtils.twoPointDistance

class PlaneGeometryView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val elementList = listOf(
        LineElement(Line(0f, 0f, 100f, 100f)),
        LineElement(Line(0f, 200f, 200f, 300f)),
        LineElement(Rect(100f, 200f, 300f, 400f)),
        LineElement(Circle(0f, 200f, 200f))
    )

    private var currentSelect: LineElement? = null
    private var currentDoubleSelect: LineElement? = null
    private var lastPointerDistance = 1f
    private var inScaleMode = false

    init {
        elementList[0].paint.strokeWidth = 3f
        elementList[0].paint.color = Color.BLUE

        elementList[1].paint.strokeWidth = 3f
        elementList[1].paint.color = Color.GREEN
    }

    private var downX = 0f
    private var downY = 0f

    private var lastX = 0f
    private var lastY = 0f

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawLines(canvas)
            drawCross(canvas)
        }
    }

    private fun drawLines(canvas: Canvas) {
        for (e in elementList) {
            if (e == currentSelect) {
                e.paint.strokeWidth = 10f
            } else {
                e.paint.strokeWidth = 3f
            }
            e.shape.draw(canvas, e.paint)
        }
    }

    private fun drawCross(canvas: Canvas) {
        for (i in elementList.indices) {
            for (j in i + 1 until elementList.size) {
                elementList[i].shape.crossPointWith(elementList[j].shape).forEach { p ->
                    paint.color = Color.RED
                    paint.strokeWidth = 20f
                    canvas.drawPoint(p.x, p.y, paint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val x = event.x
        val y = event.y
        val point = PointF(x, y)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                currentSelect = null
                downX = x
                downY = y
                selectedDetect(point)
                invalidate()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                doubleSelectedDetect()
                lastPointerDistance = twoFingerDistance(event)
                Log.e("xcm", "lastPointerDistance = $lastPointerDistance")
            }
            MotionEvent.ACTION_POINTER_UP -> {
                currentDoubleSelect = null
            }
            MotionEvent.ACTION_UP -> {
                inScaleMode = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (inScaleMode) {
                    handleScaleMode(event)
                } else {
                    handleTouchMode(event)
                }
                invalidate()
            }
        }
        lastX = event.x
        lastY = event.y
        return true
    }

    private fun handleScaleMode(event: MotionEvent) {
        if (event.pointerCount != 2) {
            return
        }
        currentDoubleSelect?.shape?.let {
            val x = event.getX(0)
            val y = event.getY(0)
            val x1 = event.getX(1)
            val y1 = event.getY(1)
            if (it is ScalableShape) {
                val dis = twoPointDistance(x, y, x1, y1)
                val scale = dis / lastPointerDistance
                lastPointerDistance = dis
                Log.e("xcm", "dis = $dis, scale = $scale")
                it.scaleBy(scale)
            } else {
                when (it) {
                    is Line -> {
                        it.setTo(x, y, x1, y1)
                    }
                }
            }
        }
    }

    private fun handleTouchMode(event: MotionEvent) {
        val deltaX = event.x - lastX
        val deltaY = event.y - lastY
        currentSelect?.shape?.moveBy(deltaX, deltaY)
    }

    private fun selectedDetect(p: PointF) {
        for (e in elementList) {
            if (currentSelect == null && e.shape.isClicked(p)) {
                currentSelect = e
                break
            }
        }
    }

    private fun doubleSelectedDetect() {
        for (e in elementList) {
            if (currentDoubleSelect == null && e == currentSelect) {
                currentDoubleSelect = e
                inScaleMode = true
                break
            }
        }
    }

    data class LineElement(var shape: Shape) {
        var paint: Paint = Paint()

        init {
            paint.style = Paint.Style.STROKE
        }
    }

    private fun twoFingerDistance(event: MotionEvent): Float {
        val x = event.getX(0)
        val y = event.getY(0)
        val x1 = event.getX(1)
        val y1 = event.getY(1)
        return twoPointDistance(x, y, x1, y1)
    }
}
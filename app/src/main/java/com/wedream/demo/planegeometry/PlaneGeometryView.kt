package com.wedream.demo.planegeometry

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import com.wedream.demo.planegeometry.PlaneGeometryUtils.twoPointDistance
import com.wedream.demo.planegeometry.shape.*
import com.wedream.demo.util.Vector2D
import kotlin.random.Random

class PlaneGeometryView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val elementList = mutableListOf<ShapeElement>()

    private var currentSelect: ShapeElement? = null
    private var currentDoubleSelect: ShapeElement? = null
    private var lastPointerDistance = 1f
    private var inScaleMode = false
    private var borderRect = Rect()

    private var downX = 0f
    private var downY = 0f

    private var lastX = 0f
    private var lastY = 0f

    private var mode = Mode.Normal

    private var velocityTracker: VelocityTracker? = null

    private var animationHandler: Handler = Handler {
        currentSelect?.let {
            val speed = it.speed
            it.shape.moveBy(speed.x, speed.y)
            checkCollision()
            invalidate()
            sendBounceMessage()
        }
        false
    }


    init {
        paint.style = Paint.Style.STROKE
    }

    companion object {
        const val MSG_BOUNCE_ANIMATION = 0
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawBorder(canvas)
            drawLines(canvas)
            drawCross(canvas)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    private fun sendBounceMessage() {
        animationHandler.removeMessages(MSG_BOUNCE_ANIMATION)
        animationHandler.sendEmptyMessage(MSG_BOUNCE_ANIMATION)
    }

    /**
     * 碰撞检测
     */
    private fun checkCollision() {
        out@for (segment in borderRect.getSegments()) {
            for (element in elementList) {
                if (segment.isOverlapWith(element.shape)) {
                    // 如果碰撞了，要改变速度
                    element.speed = element.speed.reflectBy(segment.getVector())
                    Log.e("xcm", "change speed = ${element.speed}")
                    break@out
                }
            }
        }
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
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
        velocityTracker?.addMovement(event)
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
            }
            MotionEvent.ACTION_POINTER_UP -> {
                currentDoubleSelect = null
            }
            MotionEvent.ACTION_UP -> {
                inScaleMode = false
                if (mode == Mode.Bounce) {
                    doFly()
                }
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

    private fun doFly() {
        currentSelect?.let {
            velocityTracker?.computeCurrentVelocity(8)
            val xSpeed = velocityTracker?.getXVelocity(0) ?: return
            val ySpeed = velocityTracker?.getYVelocity(0) ?: return
            Log.e("xcm", "xSpeed = $xSpeed, ySpeed = $ySpeed")
            it.speed = Vector2D(xSpeed, ySpeed)
            sendBounceMessage()
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

    data class ShapeElement(var shape: Shape) {
        var paint: Paint = Paint()
        var speed: Vector2D = Vector2D()

        init {
            paint.style = Paint.Style.STROKE
        }
    }

    enum class Mode {
        Normal, Bounce
    }

    private fun twoFingerDistance(event: MotionEvent): Float {
        val x = event.getX(0)
        val y = event.getY(0)
        val x1 = event.getX(1)
        val y1 = event.getY(1)
        return twoPointDistance(x, y, x1, y1)
    }

    fun addCircle() {
        val random = Random(System.currentTimeMillis())
        val x = random.nextInt(0, width).toFloat()
        val y = random.nextInt(0, height).toFloat()
        val radius = random.nextInt(50, 100).toFloat()
        val c = Circle(x, y, radius)
        elementList.add(ShapeElement(c))
        invalidate()
    }

    fun setMode(mode: Mode) {
        this.mode = mode
        if (mode == Mode.Bounce && velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
    }

    fun removeAll(){
        animationHandler.removeMessages(MSG_BOUNCE_ANIMATION)
        elementList.clear()
        invalidate()
    }
}
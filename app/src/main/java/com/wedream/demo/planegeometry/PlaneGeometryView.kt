package com.wedream.demo.planegeometry

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import com.wedream.demo.planegeometry.PlaneGeometryUtils.twoPointDistance
import com.wedream.demo.planegeometry.shape.*
import com.wedream.demo.util.Vector2D
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class PlaneGeometryView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val paint = Paint()
    private val crossPointPaint = Paint()
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

    init {
        paint.style = Paint.Style.STROKE

        crossPointPaint.style = Paint.Style.STROKE
        crossPointPaint.color = Color.RED
        crossPointPaint.strokeWidth = 20f
    }

    private val colors = listOf(Color.BLACK, Color.GREEN, Color.BLUE, Color.GRAY, Color.RED, Color.CYAN, Color.MAGENTA)

    companion object {
        const val MSG_BOUNCE_ANIMATION = 0
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawBorder(canvas)
            drawElements(canvas)
            drawCross(canvas)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        borderRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    /**
     * 碰撞检测
     */
    private fun checkCollision() {
        val tempList = hashSetOf<ShapeElement>()
        for (i in elementList.indices) {
            val element = elementList[i]
            //改变速度
            val speed = element.speed
            element.shape.moveBy(speed.x, speed.y)
            if (tempList.contains(element)) {
                continue
            }
            // 首先检测和边界的碰撞情况
            for (segment in borderRect.getSegments()) {
                if (segment.isOverlapWith(element.shape)) {
                    // 如果碰撞了，要改变速度
                    element.speed = element.speed.reflectBy(segment.getVector())
                    element.paint.color = colors.random()
                    continue
                }
            }
            // 检测与其他物体的碰撞
            for (j in i + 1 until elementList.size) {
                val other = elementList[j]
                if (tempList.contains(other)) {
                    continue
                }
                val shape1 = element.shape
                val shape2 = other.shape
                if (shape1.isOverlapWith(shape2)) {
                    // 圆与圆
                    if (shape1 is Circle && shape2 is Circle) {
                        // TODO
                        // 动量守恒
//                        val v10 = element.speed.magnitude()
//                        val v20 = other.speed.magnitude()
//                        val m1 = shape1.getArea()
//                        val m2 = shape2.getArea()
//                        // 圆心连线的向量
//                        val connectVector = Vector2D(shape2.getCenter() - shape1.getCenter())
//                        val angle1 = element.speed.angleWith(connectVector)
//                        val v10x = v10 * cos(angle1)
//                        val v10y = v10 * sin(angle1)
//                        val angle2 = other.speed.angleWith(connectVector)
//                        val v20x = v20 * cos(angle2)
//                        val v20y = v20 * sin(angle2)
//
//                        val v11x = ((m1 - m2) * v10x + 2 * m2 * v20x) / (m1 + m2)
//                        val v21x = ((m2 - m1) * v20x + 2 * m1 * v10x) / (m1 + m2)
//                        val v11y = ((m1 - m2) * v10y + 2 * m2 * v20y) / (m1 + m2)
//                        val v21y = ((m2 - m1) * v20y + 2 * m1 * v10y) / (m1 + m2)

                        //旋转回去
//                        element.speed = Vector2D(v11x, v11y)
//                        other.speed = Vector2D(v21x, v21y)

                        Log.e("xcm", "before ${element.speed}, ${other.speed}")
                        Log.e("xcm", "after ${element.speed}, ${other.speed}")
                    }
                    tempList.add(element)
                    tempList.add(other)
                }
            }
        }
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawElements(canvas: Canvas) {
        checkCollision()
        for (e in elementList) {
            if (e == currentSelect) {
                e.paint.strokeWidth = 10f
            } else {
                e.paint.strokeWidth = 6f
            }
            e.shape.draw(canvas, e.paint)
        }
        invalidate()
    }

    private fun drawCross(canvas: Canvas) {
        for (i in elementList.indices) {
            for (j in i + 1 until elementList.size) {
                elementList[i].shape.crossPointWith(elementList[j].shape).forEach { p ->
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
            velocityTracker?.computeCurrentVelocity(17)
            val xSpeed = velocityTracker?.getXVelocity(0) ?: return
            val ySpeed = velocityTracker?.getYVelocity(0) ?: return
            val speed = Vector2D(xSpeed / 5f, ySpeed / 5f)
            if (speed.magnitude() > 5f) {
                it.speed = speed
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
        val radius = random.nextInt(50, 100)
        val x = random.nextInt(radius, width - radius).toFloat()
        val y = random.nextInt(radius, height - radius).toFloat()
        val c = Circle(x, y, radius.toFloat())
        val shape = ShapeElement(c)
        shape.paint.color = colors.random()
        elementList.add(shape)
        invalidate()
    }

    fun setMode(mode: Mode) {
        this.mode = mode
        if (mode == Mode.Bounce && velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
    }

    fun removeAll() {
        elementList.clear()
        invalidate()
    }
}
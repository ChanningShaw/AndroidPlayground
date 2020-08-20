package com.wedream.demo.planegeometry.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.wedream.demo.planegeometry.PlaneGeometryUtils
import com.wedream.demo.planegeometry.PlaneGeometryUtils.getCrossPoints
import com.wedream.demo.planegeometry.distanceTo
import kotlin.math.abs

/**
 * 圆的标准式：(x - a)^2 + (y - b)^2 = r^2
 */
class Circle constructor(private var x: Float,
                         private var y: Float,
                         private var radius: Float) : ScalableShape() {
    /**
     * 圆的一般式：
     * x^2 + y^2 + ax + by + c = 0
     */
    private var a = 0f
    private var b = 0f
    private var c = 0f

    init {
        setTo(x, y, radius)
    }

    fun setTo(x: Float, y: Float, radius: Float) {
        this.x = x
        this.y = y
        this.radius = radius
        normalize()
    }

    private fun normalize() {
        a = -2 * x
        b = -2 * y
        c = x * x + y * y - radius * radius
    }

    override fun isClicked(p: PointF): Boolean {
        val distance = p.distanceTo(x, y)
        return abs(distance - radius) <= CLICK_DETECT_THRESHOLD
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(x, y, radius, paint)
    }

    override fun crossPointWith(shape: Shape): List<PointF> {
        when (shape) {
            is Line -> {
                return getCrossPoints(shape, this)
            }
            is Circle -> {
                return getCrossPoints(shape, this)
            }
        }
        return emptyList()
    }

    override fun moveBy(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
        normalize()
    }

    override fun scaleBy(scale: Float) {
        radius *= scale
        normalize()
    }

    override fun getCoefficients(): List<Float> {
        return listOf(a, b, c)
    }

    override fun has(p: PointF): Boolean {
        return p.x * p.x + p.y * p.y + a * p.x + b * p.y + c == 0f
    }

    override fun isOverlapWith(shape: Shape): Boolean {
        when (shape) {
            is Line -> {
                return PlaneGeometryUtils.isOverlapWith(shape, this)
            }
            is Circle ->{
                return PlaneGeometryUtils.isOverlapWith(this, shape)
            }
            else -> return false
        }
    }

    override fun getArea(): Float {
        return Math.PI.toFloat() * radius * radius
    }

    override fun getCenter(): PointF {
        return PointF(x, y)
    }

    fun getRadius(): Float {
        return radius
    }

    override fun toString(): String {
        return "($x, $y, $radius)"
    }
}
package com.wedream.demo.planegeometry.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import com.wedream.demo.planegeometry.PlaneGeometryUtils.getCrossPoints
import com.wedream.demo.planegeometry.PlaneGeometryUtils.isOverlapWith
import com.wedream.demo.util.Vector2D
import kotlin.math.abs
import kotlin.math.sqrt

open class Line : Shape {
    /**
     * 两点式
     */
    constructor(x1: Float,
                y1: Float,
                x2: Float,
                y2: Float) {
        setTo(x1, y1, x2, y2)
    }

    /**
     * 点斜式
     */
    constructor(x: Float, y: Float, k: Float) {
        a = k
        b = -1f
        c = -k * x + y
        type = if (k == 0f) {
            Type.Horizontal
        } else {
            Type.Common
        }
    }

    /**
     * 直线的一般形式 ax + by + c = 0
     */
    private var a = 0f
    private var b = 0f
    private var c = 0f
    private var type = Type.Common

    enum class Type {
        Common, Vertical, Horizontal
    }

    /**
     * 斜率
     */
    fun getSlope(): Float {
        return when (type) {
            Type.Vertical -> Float.POSITIVE_INFINITY
            Type.Horizontal -> 0f
            Type.Common -> -a / b
        }
    }

    fun getX(y: Float): Float {
        return when (type) {
            Type.Vertical -> -c
            Type.Horizontal -> y
            Type.Common -> -(c + b * y) / a
        }
    }

    fun getY(x: Float): Float {
        return when (type) {
            Type.Vertical -> -x
            Type.Horizontal -> -c
            Type.Common -> -(c + a * x) / b
        }
    }

    fun isParallelWith(other: Line): Boolean {
        return getSlope() == other.getSlope()
    }

    override fun crossPointWith(shape: Shape): List<PointF> {
        Log.e("xcm", "$this crossPointWith $shape")
        when (shape) {
            is Line -> {
                return getCrossPoints(this, shape)
            }
            is Circle -> {
                return getCrossPoints(this, shape)
            }
            is Rect -> {
                return getCrossPoints(this, shape)
            }
        }
        return emptyList()
    }

    fun distanceTo(p: PointF): Float {
        return abs(a * p.x + b * p.y + c) / sqrt(a * a + b * b)
    }

    fun getType(): Type {
        return type
    }

    fun footPointOf(p: PointF): PointF {
        val x = (b * b * p.x - a * b * p.y - a * c) / (a * a + b * b)
        val y = (a * a * p.y - a * b * p.x - b * c) / (a * a + b * b)
        return PointF(x, y)
    }

    override fun moveBy(deltaX: Float, deltaY: Float) {
        when (type) {
            Type.Vertical -> {
                c -= deltaX
            }
            Type.Horizontal -> {
                c -= deltaY
            }
            Type.Common -> {
                val k = getSlope()
                val yOffset = -c / b
                a = k
                b = -1f
                c = -k * deltaX + yOffset + deltaY
            }
        }
    }

    fun setTo(x1: Float,
              y1: Float,
              x2: Float,
              y2: Float) {
        when {
            x1 == x2 -> {
                a = 1f
                b = 0f
                c = -x1
                type = Type.Vertical
            }
            y1 == y2 -> {
                a = 0f
                b = 1f
                c = -y1
                type = Type.Horizontal
            }
            else -> {
                a = -1 / (x2 - x1)
                b = 1 / (y2 - y1)
                c = x1 / (x2 - x1) - y1 / (y2 - y1)
                type = Type.Common
            }
        }
    }

    override fun isClicked(p: PointF): Boolean {
        return distanceTo(p) <= CLICK_DETECT_THRESHOLD
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        val x1 = 0f
        val x2 = canvas.width.toFloat()
        canvas.drawLine(x1, getY(x1), x2, getY(x2), paint)
    }

    override fun getCoefficients(): List<Float> {
        return listOf(a, b, c)
    }

    override fun has(p: PointF): Boolean {
        return a * p.x + b * p.y + c == 0f
    }

    override fun isOverlapWith(shape: Shape): Boolean {
        when (shape) {
            is Circle -> {
                return isOverlapWith(this, shape)
            }
        }
        return false
    }

    override fun toString(): String {
        return "Line: $a, $b, $c"
    }

    /**
     * 返回直线的向量
     */
    open fun getVector(): Vector2D {
        return Vector2D(-b, a)
    }
}
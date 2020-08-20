package com.wedream.demo.util

import android.graphics.PointF
import kotlin.math.*

class Vector2D(var x: Float = 0f, var y: Float = 0f) {

    constructor(point: PointF) : this(point.x, point.y)

    companion object {
        const val PI = Math.PI.toFloat()
    }


    fun magnitude(): Float {
        return sqrt(x * x + y * y)
    }

    /**
     * 与x正方向的夹角
     */
    fun angle(): Float {
        val angle = acos(x / magnitude())
        if (y > 0) {
            return 2 * PI - angle
        }
        return angle
    }

    /**
     * 返回夹角
     */
    fun angleWith(other: Vector2D): Float {
        return atan2(other.y, other.x) - atan2(y, x)
    }

    fun reflectBy(mirror: Vector2D): Vector2D {
        val vertical = mirror.getVerticalVector()
        return this - vertical * (this * vertical * 2f)
    }

    fun reflectWith(vertical: Vector2D): Vector2D {
        val normal = vertical / vertical.magnitude()
        return this - normal * (this * normal * 2f)
    }

    fun getVerticalVector(): Vector2D {
        val angle = (angle() + PI * 0.5f) % PI
        return Vector2D(cos(angle), -sin(angle))
    }

    operator fun plus(other: Vector2D): Vector2D {
        x += other.x
        y += other.y
        return this
    }

    operator fun minus(other: Vector2D): Vector2D {
        x -= other.x
        y -= other.y
        return this
    }

    operator fun times(value: Float): Vector2D {
        x *= value
        y *= value
        return this
    }

    operator fun times(other: Vector2D): Float {
        return x * other.x + y * other.y
    }

    operator fun div(value: Float): Vector2D {
        x /= value
        y /= value
        return this
    }

    override fun toString(): String {
        return "Vector ($x, $y)"
    }

    fun isZero(): Boolean {
        return x == 0f && y == 0f
    }

    fun inverse(): Vector2D{
        x = -x
        y = -y
        return this
    }
}
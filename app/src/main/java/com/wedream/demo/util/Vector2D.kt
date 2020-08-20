package com.wedream.demo.util

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2D(var x: Float = 0f, var y: Float = 0f) {

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
        if (y < 0) {
            return 2 * PI - angle
        }
        return angle
    }

    /**
     * 返回夹角的余弦值
     */
    fun angleWith(other: Vector2D): Float {
        return acos(this * other / (magnitude() * other.magnitude()))
    }

    fun reflectBy(other: Vector2D): Vector2D {
        val vertical = other.getVerticalVector()
        return this - vertical * (this * vertical * 2f)
    }

    fun getVerticalVector(): Vector2D {
        val angle = (angle() + PI * 0.5f) % PI
        return Vector2D(cos(angle), sin(angle))
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
}
package com.wedream.demo.util

import android.graphics.PointF
import kotlin.math.*

/**
 * 设计成不可变对象
 *
 * TODO 抽象基类
 */
class Vector2D {

    var x :Float = 0f
    var y: Float = 0f

    constructor(x: Float = 0f, y: Float = 0f) {
        this.x = x
        this.y = y
    }

    constructor(point: PointF) : this(point.x, point.y)

    constructor(direction: Vector2D, magnitude: Float) {

    }

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
     * 范围 -PI ~ PI
     */
    fun angleWith(other: Vector2D): Float {
        return atan2(other.y, other.x) - atan2(y, x)
    }

    /**
     * TODO 优化不可变对象的运算
     */
    fun reflectBy(mirror: Vector2D): Vector2D {
        val vertical = mirror.getVerticalVector()
        return this - (vertical * (this * vertical * 2f))
    }

    fun reflectWith(vertical: Vector2D): Vector2D {
        val normal = vertical / vertical.magnitude()
        return this - (normal * (this * normal * 2f))
    }

    fun getVerticalVector(): Vector2D {
        val angle = (angle() + PI * 0.5f) % PI
        return Vector2D(cos(angle), -sin(angle))
    }

    operator fun plus(other: Vector2D): Vector2D {
        return Vector2D(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2D): Vector2D {
        return Vector2D(x - other.x, y - other.y)
    }

    operator fun times(value: Float): Vector2D {
        return Vector2D(x * value, y * value)
    }

    operator fun times(other: Vector2D): Float {
        return x * other.x + y * other.y
    }

    operator fun div(value: Float): Vector2D {
        return Vector2D(x / value, y / value)
    }

    override fun toString(): String {
        return "($x, $y), magnitude = ${magnitude()}"
    }

    fun isZero(): Boolean {
        return x == 0f && y == 0f
    }

    fun inverse(): Vector2D {
        return Vector2D(-x, -y)
    }

    fun setMagnitude(value: Float): Vector2D {
        val ratio = value / magnitude()
        return Vector2D(x * ratio, y * ratio)
    }

    fun clone(): Vector2D {
        return Vector2D(x, y)
    }
}
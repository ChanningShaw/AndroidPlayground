package com.wedream.demo.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import com.wedream.demo.util.PlaneGeometryUtils.getCrossPoints

class Rect(private var left: Float,
           private var top: Float,
           private var right: Float,
           private var bottom: Float) : ScalableShape() {

    override fun isClicked(p: PointF): Boolean {
        getSegments().forEach {
            if (it.isClicked(p)) {
                return true
            }
        }
        return false
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(left, top, right, bottom, paint)
    }

    override fun crossPointWith(shape: Shape): List<PointF> {
        Log.e("xcm", "$this crossPointWith $shape")
        when (shape) {
            is Line -> {
                return getCrossPoints(shape, this)
            }
        }
        return emptyList()
    }

    override fun moveBy(deltaX: Float, deltaY: Float) {
        left += deltaX
        top += deltaY
        right += deltaX
        bottom += deltaY
    }

    override fun scaleBy(scale: Float) {
        val width = width()
        val height = height()
        val centerX = centerX()
        val centerY = centerY()

        left = centerX - width * 0.5f * scale
        right = centerX + width * 0.5f * scale
        top = centerY - height * 0.5f * scale
        bottom = centerY + height * 0.5f * scale
    }


    override fun getCoefficients(): List<Float> {
        return listOf(left, top, right, bottom)
    }

    override fun has(p: PointF): Boolean {
        return false
    }

    fun getPoints(): List<PointF> {
        return listOf(PointF(left, top), PointF(right, top), PointF(right, bottom), PointF(left, bottom))
    }

    fun getSegments(): List<Segment> {
        return listOf(Segment(left, top, right, top),
            Segment(right, top, right, bottom),
            Segment(left, bottom, right, bottom),
            Segment(left, top, left, bottom))
    }

    override fun toString(): String {
        return "Rect($left, $top, $right, $bottom)"
    }

    override fun getArea(): Float {
        return (right - left) * (bottom - top)
    }

    fun centerX(): Float {
        return (left + right) * 0.5f
    }

    fun centerY(): Float {
        return (top + bottom) * 0.5f
    }

    fun width(): Float {
        return right - left
    }

    fun height(): Float {
        return bottom - top
    }
}
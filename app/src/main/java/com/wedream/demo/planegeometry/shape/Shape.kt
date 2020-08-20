package com.wedream.demo.planegeometry.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

abstract class Shape {
    companion object {
        const val CLICK_DETECT_THRESHOLD = 20f
    }

    abstract fun isClicked(p: PointF): Boolean
    abstract fun draw(canvas: Canvas, paint: Paint)
    abstract fun crossPointWith(shape: Shape): List<PointF>
    abstract fun moveBy(deltaX: Float, deltaY: Float)
    abstract fun getCoefficients(): List<Float>
    abstract fun has(p: PointF): Boolean
    abstract fun isOverlapWith(shape: Shape): Boolean
}
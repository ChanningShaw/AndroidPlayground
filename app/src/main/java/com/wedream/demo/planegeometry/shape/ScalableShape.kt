package com.wedream.demo.planegeometry.shape

import android.graphics.PointF

abstract class ScalableShape : Shape() {
    abstract fun scaleBy(scale: Float)
    abstract fun getArea(): Float
    abstract fun getCenter(): PointF
}
package com.wedream.demo.util

abstract class ScalableShape : Shape() {
    abstract fun scaleBy(scale: Float)
    abstract fun getArea(): Float
}
package com.wedream.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import com.wedream.demo.util.LogUtils.log
import kotlin.math.sqrt

open class ScaleView : FrameLayout {
    companion object {
        const val MAX_SCALE = 15.0f
        const val MIN_SCALE = 0.05f
        private const val TAG = "OperationScaleView"
    }

    var operateScale: Float = 1.0f
    private var oldDistance = 0f
    private var touchSlop = 0
    private var scaleListener: OnScaleListener? = null
    private var needConsumptionTouch = false
    private var lastScale = -1f

    constructor(context: Context) : super(context) {
        initData()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initData()
    }

    private fun initData() {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop

        setWillNotDraw(false)
    }

    fun setOnScaleListener(scaleListener: OnScaleListener) {
        this.scaleListener = scaleListener
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                lastScale = -1.0f
                needConsumptionTouch = false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                needConsumptionTouch = false
                lastScale = -1.0f
                onPointerDown(event)
                scaleListener?.onScaleStart(operateScale)
            }

            MotionEvent.ACTION_MOVE -> {
                needConsumptionTouch = event.pointerCount == 2
            }

            MotionEvent.ACTION_POINTER_UP -> {
                needConsumptionTouch = false
            }

            MotionEvent.ACTION_UP -> {
                needConsumptionTouch = false
            }
        }
        return needConsumptionTouch
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        log { "action = ${event.action}" }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_MOVE -> {
                onTouchMove(event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                onPointerDown(event)
                lastScale = -1.0f
            }
            MotionEvent.ACTION_POINTER_UP -> {
                scaleListener?.onScaleEnd(operateScale)
            }
        }
        return true
    }

    private fun onPointerDown(event: MotionEvent) {
        oldDistance = calculateDistance(event.getX(0), event.getY(0),
            event.getX(1), event.getY(1))
    }

    private fun onTouchMove(event: MotionEvent) {
        if (event.pointerCount == 2) {
            twoPointerOperation(event)
        }
    }

    private fun twoPointerOperation(event: MotionEvent) {
        if (oldDistance <= 0) {
            // 有可能另外一个手指不在View的区域范围内的时候，ACTION_POINTER_DOWN没有触发到，可以先不处理缩放事件.
            return
        }
        val newDistance = calculateDistance(event.getX(0), event.getY(0),
            event.getX(1), event.getY(1))
        val isScaleBig = (newDistance >= oldDistance)
        if ((operateScale <= MIN_SCALE && !isScaleBig) || (operateScale >= MAX_SCALE && isScaleBig)) {
            return
        }
        if (lastScale < 0) {
            lastScale = getCurrentScale(event)
            return
        }

        var scale = if (isScaleBig) {
            val currentScale = newDistance / oldDistance
            val realScaleAdd = (currentScale - lastScale) * getRatio()
            if (realScaleAdd + operateScale < MIN_SCALE) {
                lastScale = currentScale
                MIN_SCALE
            } else {
                lastScale = currentScale
                realScaleAdd + operateScale
            }
        } else {
            val currentScale = oldDistance / newDistance
            val realScaleAdd = (currentScale - lastScale) * getRatio()
            if (operateScale - realScaleAdd < MIN_SCALE) {
                lastScale = currentScale
                MIN_SCALE
            } else {
                lastScale = currentScale
                operateScale - realScaleAdd
            }
        }
        if (scale < MIN_SCALE) {
            scale = MIN_SCALE
        }
        if (kotlin.math.abs(operateScale - scale) > 0.001) {
            operateScale = scale
            scaleListener?.onScaling(scale)
        }
    }

    /**
     * 由于每次都等长读放大，所以需要加系数，让放大体验感更好一些.
     */
    private fun getRatio(): Float {
        return when {
            // 经验调教的值。
            operateScale > 9 -> 3.5f
            operateScale > 7 -> 3.0f
            operateScale > 5 -> 2.5f
            operateScale > 3 -> 2.0f
            operateScale > 1 -> 1.5f
            operateScale <= 0.1 -> 0.1f
            operateScale <= 0.2 -> 0.2f
            operateScale <= 0.3 -> 0.3f
            operateScale <= 0.4 -> 0.4f
            operateScale <= 0.5 -> 0.5f
            operateScale <= 0.6 -> 0.6f
            operateScale <= 0.7 -> 0.7f
            operateScale <= 0.8 -> 0.8f
            operateScale <= 1 -> 0.9f
            else -> 1.0f
        }
    }

    private fun getCurrentScale(event: MotionEvent): Float {
        val newDistance = calculateDistance(event.getX(0), event.getY(0),
            event.getX(1), event.getY(1))
        return newDistance / oldDistance
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()

        return sqrt(x * x + y * y).toFloat()
    }

    interface OnScaleListener {
        fun onScaling(scale: Float)
        fun onScaleStart(scale: Float)
        fun onScaleEnd(scale: Float)
    }
}
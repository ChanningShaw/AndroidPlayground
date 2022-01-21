package com.wedream.demo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wedream.demo.app.track.logEvent
import com.wedream.demo.util.dp

open class Switch(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    val paint = Paint()
    private val uncheckBgColor = Color.GRAY
    private val checkBgColor = Color.RED
    private var animator = ValueAnimator.ofFloat(1f)
    var isCheck = false
    var radius = 7f.dp
    set(value) {
        field = value
        updateCx()
    }
    var gap = 0f
    var cx = 0f

    init {
        paint.color = Color.BLUE
        paint.isAntiAlias = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnClickListener {
            isCheck = !isCheck
            logEvent("switch_click") {
                this["is_check"] = if (isCheck) 1 else 0
            }
            animator.cancel()
            animator.removeAllUpdateListeners()
            val lastCx = cx
            val movingRight = isCheck
            animator.addUpdateListener {
                cx = if (movingRight) {
                    lastCx + it.animatedFraction * (width - radius - gap - lastCx)
                } else {
                    lastCx - it.animatedFraction * (lastCx - (radius + gap))
                }
                invalidate()
            }
            animator.start()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isCheck) {
            paint.color = checkBgColor
            canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), height / 2f, height / 2f, paint)
        } else {
            paint.color = uncheckBgColor
            canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), height / 2f, height / 2f, paint)
        }
        paint.color = Color.WHITE
        canvas.drawCircle(cx, radius + gap, radius, paint)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateCx()
    }

    private fun updateCx() {
        gap = (height - radius * 2) / 2
        cx = if (isCheck) {
            width - radius - gap
        } else {
            radius + gap
        }
        invalidate()
    }
}
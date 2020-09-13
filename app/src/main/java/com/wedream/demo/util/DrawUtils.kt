package com.wedream.demo.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.wedream.demo.util.DrawUtils.ARROW_ANGLE
import com.wedream.demo.util.DrawUtils.ARROW_LENGTH
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


fun Canvas.drawCircleWithText(
    cx: Float,
    cy: Float,
    radius: Float,
    circlePaint: Paint,
    text: String,
    textPaint: Paint
) {
    val rect = Rect()
    drawCircle(cx, cy, radius, circlePaint)
    textPaint.getTextBounds(text, 0, text.length, rect)
    drawText(
        text,
        cx - rect.width() * 0.5f,
        cy + rect.height() * 0.5f,
        textPaint
    )
}

fun Canvas.drawVector(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    paint: Paint
) {
    drawLine(x1, y1, x2, y2, paint)
    val w = ARROW_LENGTH * cos(ARROW_ANGLE).toFloat()
    val h = ARROW_LENGTH * sin(ARROW_ANGLE).toFloat()
    // TODO 画任意方向的箭头
    drawLine(x2 - w, y2 - h, x2, y2, paint)
    drawLine(x2 - w, y2 + h, x2, y2, paint)
}

object DrawUtils {
    const val ARROW_LENGTH = 20f
    const val ARROW_ANGLE = 30f / 180f * PI
}
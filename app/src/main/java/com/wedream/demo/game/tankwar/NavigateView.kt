package com.wedream.demo.game.tankwar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.dp
import kotlin.math.min
import kotlin.math.sqrt

class NavigateView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    val paint = Paint()
    private var currentDirection: Direction = Direction.None
    private var onNavigateCallback: ((Direction) -> Unit)? = null

    init {
        paint.color = Color.BLUE
        paint.strokeWidth = 2f.dp
        paint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(width, height)
        val realWidthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(widthMeasureSpec))
        val realHeightSpec =
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(heightMeasureSpec))
        super.onMeasure(realWidthSpec, realHeightSpec)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawLine(width.toFloat(), 0f, 0f, height.toFloat(), paint)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val direction = judgeDirection(event)
        if (direction != currentDirection) {
            currentDirection = direction
            onNavigateCallback?.invoke(direction)
            log { "$currentDirection" }
            invalidate()
        }
        return true
    }

    private fun judgeDirection(event: MotionEvent): Direction {
        if (event.action == MotionEvent.ACTION_UP) {
            return Direction.None
        }
        val x = event.x
        val y = event.y
        val centerX = width / 2
        val centerY = height / 2
        if (sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y)) <= 50) {
            return currentDirection
        } else if (y >= x && y <= width - x) {
            return Direction.Left
        } else if (y <= x && y <= width - x) {
            return Direction.Up
        } else if (y <= x && y >= width - x) {
            return Direction.Right
        } else if (y >= x && y >= width - x) {
            return Direction.Down
        }
        throw IllegalStateException("")
    }

    fun onNavigate(block: (Direction) -> Unit) {
        onNavigateCallback = block
    }

    enum class Direction {
        Left, Right, Up, Down, None, Keep
    }
}
package com.wedream.demo.game.tankwar

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.util.dp

class BattleFieldView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SquareView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    val paint = Paint()
    private var currentDirection: NavigateView.Direction = NavigateView.Direction.None
    private var tankBitmap = BitmapFactory.decodeResource(resources, R.drawable.tank)

    init {
        paint.color = resources.getColor(R.color.marker_text_style_b_color)
        paint.strokeWidth = 2f.dp
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(tankBitmap, 0f, 0f, paint)
    }

    fun setDirection(direction: NavigateView.Direction) {
        currentDirection = direction
    }
}
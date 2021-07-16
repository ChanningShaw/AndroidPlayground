package com.wedream.demo.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.wedream.demo.app.BaseActivity

class CanvasLayerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(CanvasLayerView(this))
    }
}

class CanvasLayerView(context: Context) : View(context) {
    private var measureWidth = 0//父布局宽度
    private var measureHeight = 0//父布局高度
    private val OFFSET = 100//偏移量

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    init {
        //设置画笔的样式
        mPaint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        measureHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.CYAN)
        val saveCount = canvas.saveLayer(
            OFFSET.toFloat(),
            OFFSET.toFloat(),
            (measureWidth - OFFSET).toFloat(),
            (measureHeight - OFFSET).toFloat(),
            mPaint
        )
        canvas.drawColor(Color.BLUE)
        val saveCount1 = canvas.saveLayer(
            (OFFSET * 2).toFloat(),
            (OFFSET * 2).toFloat(), (measureWidth - OFFSET * 2).toFloat(),
            (measureHeight - OFFSET * 2).toFloat(), mPaint
        )
        // 绿色的层
        canvas.drawColor(Color.GREEN)
        mPaint.color = Color.BLACK
        canvas.drawCircle(300f, 300f, 400f, mPaint)
        canvas.restore()
        // 这里画在蓝色的层
        mPaint.color = Color.RED
        canvas.drawCircle(300f, 300f, 300f, mPaint)
        canvas.restore()
        // 这里画在canvas原来的图层
        mPaint.color = Color.YELLOW
        canvas.drawCircle(200f, 200f, 150f, mPaint)
    }
}
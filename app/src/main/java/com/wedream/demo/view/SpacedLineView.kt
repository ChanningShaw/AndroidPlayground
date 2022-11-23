package com.wedream.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.wedream.demo.R


class SpacedLineView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle) {
        initAttrs(context, attrs)
    }

    private val path = Path()
    private val paint = Paint()
    private var itemWidth = 0f
    private var offsetWidth = 0f
    private var firstColor = 0
    private var secondColor = 0
    private var itemCount = 0
    private var intervalWidth = 0

    private fun initAttrs(context: Context, attributeSet: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SpacedLineView)
        itemWidth = typedArray.getDimension(R.styleable.SpacedLineView_item_width, 17f)
        offsetWidth = typedArray.getDimension(R.styleable.SpacedLineView_offset_width, 3f)
        firstColor = typedArray.getColor(R.styleable.SpacedLineView_first_color, Color.RED)
        secondColor = typedArray.getColor(R.styleable.SpacedLineView_second_color, Color.GREEN)
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val leftSpace = (right - left) - itemWidth
        itemCount = ((leftSpace / itemWidth) * 0.8).toInt()
        intervalWidth = ((leftSpace - itemCount * itemWidth) / (itemCount + 1)).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        var start = -itemWidth / 2
        for (i in 0..itemCount) {
            if (i % 2 == 0) {
                paint.color = secondColor
            } else {
                paint.color = firstColor
            }
            path.reset()
            // y axis down is positive
            path.moveTo(start, height.toFloat()) // left bottom
            path.lineTo(start + offsetWidth, 0f) // left top
            path.lineTo(start + offsetWidth + itemWidth, 0f) // right top
            path.lineTo(start + itemWidth, height.toFloat()) //right bottom
            path.close()
            canvas.drawPath(path, paint)
            start += offsetWidth + itemWidth + intervalWidth
        }
    }
}
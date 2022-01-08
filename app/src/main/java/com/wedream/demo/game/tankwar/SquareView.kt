package com.wedream.demo.game.tankwar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

open class SquareView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(width, height)
        val realWidthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(widthMeasureSpec))
        val realHeightSpec =
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(heightMeasureSpec))
        super.onMeasure(realWidthSpec, realHeightSpec)
    }
}
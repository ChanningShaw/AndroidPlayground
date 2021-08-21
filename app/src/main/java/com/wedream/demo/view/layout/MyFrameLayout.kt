package com.wedream.demo.view.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.wedream.demo.util.LogUtils.log

open class MyFrameLayout(context: Context, attrs: AttributeSet?, defStyle: Int) :
    FrameLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var specificWidth = 0
    private var specificHeight = 0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        log { "onLayout" }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (specificWidth != 0 && specificHeight != 0) {
            setMeasuredDimension(specificWidth, specificHeight)
        }
    }

    fun detachView(child: View) {
        detachViewFromParent(child)
    }

    fun attachView(child: View, index: Int, params: LayoutParams) {
        attachViewToParent(child, index, params)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    fun setSpecificDimen(width: Int, height: Int) {
        specificWidth = width
        specificHeight = height
        requestLayout()
    }
}
package com.wedream.demo.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.util.dp

class FlowLayout(context: Context, attrs: AttributeSet?, defStyle: Int) :
    ViewGroup(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    companion object {
        private val HORIZONTAL_ITEM_MARGIN = 10.dp
        private val VERTICAL_ITEM_MARGIN = 10.dp
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val myWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val myHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val myWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val myHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            // 限定只能是wrap_content
            val widthSpec = MeasureSpec.makeMeasureSpec(myWidthSize, MeasureSpec.UNSPECIFIED)
            val heightSpec = MeasureSpec.makeMeasureSpec(myHeightSize, MeasureSpec.UNSPECIFIED)
            child.measure(widthSpec, heightSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        var top = paddingTop
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            if (left + childWidth + HORIZONTAL_ITEM_MARGIN <= r) {
                // 可以放的下
                child.layout(left, top, left + childWidth, top + childHeight)
            } else {
                // 当前行放不下，换行
                top += childHeight + VERTICAL_ITEM_MARGIN
                left = paddingLeft
                child.layout(left, top, left + childWidth, top + childHeight)
            }
            left += childWidth + HORIZONTAL_ITEM_MARGIN
        }
    }
}
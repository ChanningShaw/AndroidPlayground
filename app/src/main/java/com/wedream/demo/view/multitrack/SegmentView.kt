package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import com.wedream.demo.R
import com.wedream.demo.view.multitrack.base.ElementView

class SegmentView(context: Context, attrs: AttributeSet?, defStyle: Int) : ElementView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        setBackgroundResource(R.color.marker_text_style_b_color)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}
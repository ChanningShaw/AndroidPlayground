package com.wedream.demo.videoeditor.timeline.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class MyHorizontalScrollView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    HorizontalScrollView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

}
package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import com.wedream.demo.view.multitrack.base.ElementView
import com.wedream.demo.view.multitrack.base.MultiTrackAdapter.Companion.FOCUS_Z

class SliderView(context: Context, attrs: AttributeSet?, defStyle: Int) : ElementView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        z = FOCUS_Z
    }
}
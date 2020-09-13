package com.wedream.demo.algo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 负责算法可视化的View
 */
abstract class VisualizationView (context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle), ActionListener {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        invalidate()
        return true
    }
}
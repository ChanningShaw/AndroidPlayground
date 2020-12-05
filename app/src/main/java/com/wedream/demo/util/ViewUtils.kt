package com.wedream.demo.util

import android.util.Size
import android.view.View
import android.view.ViewGroup

object ViewUtils {
    @JvmStatic
    fun measureView(view: View): Size {
        var lp = view.layoutParams
        if (lp == null) {
            lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (view.measuredHeight <= 0 || view.measuredWidth <= 0) {
            val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width)
            val lpHeight = lp.height
            val heightSpec: Int
            heightSpec = if (lpHeight > 0) {
                View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY)
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }
            view.measure(widthSpec, heightSpec)
        }
        return Size(view.measuredWidth, view.measuredHeight)
    }
}
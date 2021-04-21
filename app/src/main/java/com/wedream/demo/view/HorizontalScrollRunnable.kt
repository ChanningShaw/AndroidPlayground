package com.wedream.demo.view

import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.wedream.demo.util.LogUtils.log

class HorizontalScrollRunnable(private val horizontalScrollView: HorizontalScrollView,
                               private val container: ViewGroup,
                               scrollMode: ScrollMode,
                               listener: ScrollListener): ScrollRunnable(scrollMode, listener) {
    override fun run() {
        var offsetX = 0
        var continueScroll = false
        if (scrollMode == ScrollMode.ScrollLeft) {
            // 向右滑动
            if(horizontalScrollView.scrollX > 0) {
                offsetX = -15
                continueScroll = true
            } else {
                continueScroll = false
            }
        } else {
            // 向左滑动
            if (horizontalScrollView.width + horizontalScrollView.scrollX < container.width) {
                offsetX = 20
                continueScroll = true
            } else {
                continueScroll = false
            }
        }
        log { "scrollBy : $offsetX" }
        horizontalScrollView.scrollBy(offsetX, 0)
        listener.onScrolling(offsetX, 0)

        if (continueScroll) {
            horizontalScrollView.postDelayed(this, 16)
        } else {
            listener.onScrollEnd()
        }
    }
}
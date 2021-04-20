package com.wedream.demo.view

import android.graphics.Rect
import android.view.View
import android.widget.HorizontalScrollView
import com.wedream.demo.util.LogUtils.log

class HorizontalScrollRunnable(targetView: View,
                               private val horizontalScrollView: HorizontalScrollView,
                               info: CrossTrackMovementActivity.ViewInfo,
                               scrollMode: ScrollMode,
                               listener: ScrollListener): ScrollRunnable(targetView, info, scrollMode, listener) {
    override fun run() {
        val rect = Rect(info.rect)
        // 转换成全局坐标
        rect.offset(0, info.track.top)
        var offsetX = 0
        var continueScroll = false
        if (scrollMode == ScrollMode.ScrollRight) {
            // 向右滑动
            if(horizontalScrollView.scrollX > 0) {
                offsetX = 40
                continueScroll = true
            } else {
                continueScroll = false
            }
        }
        log { "scrollBy : $offsetX" }
        horizontalScrollView.smoothScrollBy(offsetX, 0)
        rect.offset(offsetX, 0)
        info.rect.set(rect)
        CrossTrackMovementActivity.layoutView(targetView, info.rect)

        if (continueScroll) {
            horizontalScrollView.postDelayed(this, 100)
        } else {
            listener.onScrollEnd()
        }
    }
}
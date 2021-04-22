package com.wedream.demo.view

import android.view.ViewGroup
import android.widget.ScrollView
import com.wedream.demo.util.LogUtils.log

class VerticalScrollRunnable(private val scrollView: ScrollView,
                             private val container: ViewGroup,
                             scrollMode: ScrollMode,
                             listener: ScrollListener) : ScrollRunnable(scrollMode, listener) {
    override fun run() {
        var offsetY = 0
        var continueScroll = false
        if (scrollMode == ScrollMode.ScrollUp) {
            // 向上滑动
            if (scrollView.scrollY > 0) {
                if (scrollView.scrollY >= CrossTrackMovementActivity.TRACK_TOTAL_HEIGHT) {
                    offsetY = -CrossTrackMovementActivity.TRACK_TOTAL_HEIGHT
                    continueScroll = true
                } else {
                    // 滑剩余的部分
                    offsetY = scrollView.scrollY - CrossTrackMovementActivity.TRACK_TOTAL_HEIGHT
                    log { "remain offsetY = $offsetY" }
                    continueScroll = false
                }
            } else {
                continueScroll = false
            }
        } else if (scrollMode == ScrollMode.ScrollDown) {
            // 向下滑动
            val absScrollY = scrollView.height + scrollView.scrollY
            if (container.height - absScrollY > 0) {
                offsetY = CrossTrackMovementActivity.TRACK_TOTAL_HEIGHT
                continueScroll = true
            } else {
                continueScroll = false
            }
        }
        log { "scrollBy : $offsetY" }
        // TODO 支持顺滑滚动
        scrollView.scrollBy(0, offsetY)
        listener.onScrolling(0, offsetY)
        if (continueScroll) {
            scrollView.postDelayed(this, 450)
        } else {
            listener.onScrollEnd()
        }
    }
}
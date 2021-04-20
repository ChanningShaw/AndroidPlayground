package com.wedream.demo.view

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.wedream.demo.util.LogUtils.log
import kotlin.math.abs

class VerticalScrollRunnable(targetView: View,
                             private val scrollView: ScrollView,
                             private val container: ViewGroup,
                             info: CrossTrackMovementActivity.ViewInfo,
                             scrollMode: ScrollMode,
                             listener: ScrollListener) : ScrollRunnable(targetView, info, scrollMode, listener) {
    override fun run() {
        val rect = Rect(info.rect)
        // 转换成全局坐标
        rect.offset(0, info.track.top)
        var offsetY = 0
        var continueScroll = false
        if (scrollMode == ScrollMode.ScrollDown) {
            // 向下滑动
            val absScrollY = scrollView.height + scrollView.scrollY
            if (container.height - absScrollY > 0) {
                if (container.height - absScrollY > CrossTrackMovementActivity.TRACK_HEIGHT) {
                    offsetY = CrossTrackMovementActivity.TRACK_HEIGHT
                    continueScroll = true
                } else {
                    offsetY = rect.bottom - absScrollY
                    continueScroll = false
                }
            } else {
                continueScroll = false
                log { "stopScroll" }
            }
        } else if (scrollMode == ScrollMode.ScrollUp) {
            // 向上滑动
            if (scrollView.scrollY > 0) {
                if (scrollView.scrollY > CrossTrackMovementActivity.TRACK_HEIGHT) {
                    offsetY = -CrossTrackMovementActivity.TRACK_HEIGHT
                    continueScroll = true
                } else {
                    offsetY = -scrollView.scrollY
                    continueScroll = false
                }
            } else {
                continueScroll = false
            }
        }
        log { "scrollBy : $offsetY" }
        scrollView.smoothScrollBy(0, offsetY)
        if (abs(offsetY) >= CrossTrackMovementActivity.TRACK_HEIGHT) {
            rect.offset(0, offsetY)
            rect.offset(0, -info.track.top)
            info.rect.set(rect)
            CrossTrackMovementActivity.layoutView(targetView, info.rect)
        }
        if (continueScroll) {
            scrollView.postDelayed(this, 500)
        } else {
            listener.onScrollEnd()
        }
    }
}
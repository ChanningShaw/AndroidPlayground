package com.wedream.demo.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.multitrack.base.ElementView
import com.wedream.demo.view.multitrack.overlap
import kotlin.math.abs
import kotlin.math.round

class CrossTrackMovementActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var container: FrameLayout

    private val totalSegmentViewMap = mutableMapOf<View, ViewInfo>()
    private val segments = mutableListOf<MutableMap<Int, ViewInfo>>()
    private val trackMap = mutableMapOf<Int, ViewGroup>()

    private var movingStart = false
    private var canPlace = false
    private var originRect = Rect()
    private var newGroupTipsView: View? = null
    private var showingNewTrackTips = false
    private var pendingNewTrackIndex = -1

    // 正在操作的Track
    private var operatingTrack: ViewGroup? = null
    private var operatingSegmentView: View? = null

    // 滚动相关
    private var scrollAction: ScrollRunnable? = null
    private var scrollMode = ScrollMode.None
    private var scrollStopOffset = 0

    companion object {
        private val TRACK_HEIGHT = AndroidUtils.dip2pix(50)
        private const val TIPS_VIEW_HEIGHT = 10
        private const val TIPS_AREA_RATIO = 0.4
        private val TIPS_AREA_HEIGHT = (TRACK_HEIGHT * TIPS_AREA_RATIO).toInt()

        fun layoutView(view: View, rect: Rect) {
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.marginStart = rect.left
            params.topMargin = rect.top
            view.layoutParams = params
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_group_move)
        scrollView = findViewById(R.id.scrollView)
        container = findViewById(R.id.container)
        container.post {
            initRects()
        }
    }

    private var elementEventListener = object : ElementView.ElementEventListener {

        override fun onActionDown(view: ElementView) {
            operatingSegmentView = view
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                val info = totalSegmentViewMap[view] ?: return
                log { "deltaY = $deltaY" }
                if (scrollMode != ScrollMode.None) {
                    // 正在滚动，不跟手移动
                    handleScrolling(info, deltaY)
                } else {
                    info.rect.offset(deltaX, deltaY)
                    handleMove(info)
                }
                layoutView(view, info.rect)
                canPlace = !checkOverlap(view, info)
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            scrollView.requestDisallowInterceptTouchEvent(false)
            if (movingStart) {
                movingStart = false
                view.z -= 10
                view.alpha = 1.0f
                scrollStopOffset = 0
                if (scrollMode != ScrollMode.None) {
                    scrollMode = ScrollMode.None
                    stopScroll()
                }
                scrollView.removeCallbacks(scrollAction)
                (view.parent as ViewGroup).z -= 10
                if (showingNewTrackTips) {
                    operatingTrack?.let { track ->
                        totalSegmentViewMap[operatingSegmentView]?.let {
                            log { "newGroup at ${it.rect}" }
                            if (pendingNewTrackIndex >= 0) {
                                val id = view.tag as Int
                                val oldTrack = track.tag as Int
                                insertSegmentToTrack(id, oldTrack, pendingNewTrackIndex, it.rect)
                            }
                        }
                        hideNewTrackTipsView()
                    }
                } else if (!canPlace) {
                    view.alpha = 1.0f
                    totalSegmentViewMap[view]?.rect?.set(originRect)
                    layoutView(view, originRect)
                } else {
                    // 如果可以放置，先找出移到了哪个容器里面
                    val info = totalSegmentViewMap[view] ?: return
//                    log { "rect = ${info.rect}" }
                    val id = view.tag as Int
                    var hasMoved = false
                    for (i in segments.indices) {
                        val group = trackMap[i] ?: continue
                        val rect = Rect(group.left, group.top, group.right, group.bottom)
                        val globalRect = info.getGlobalRect()
                        if (rect.contains(globalRect)) {
                            log { "在 group $i 中" }
                            // 在其他groups中移除
                            for (j in segments.indices) {
                                if (segments[j].containsKey(id)) {
                                    segments[j].remove(id)?.let {
                                        segments[i][id] = it
                                        it.rect.set(globalRect)
                                        it.rect.offset(0, -group.top)
                                        it.track = group
                                        hasMoved = true
                                    }
                                }
                            }
                        }
                    }
                    if (hasMoved) {
                        clearEmptyTrack()
                        fullUpdate()
                    }
                }
            }
            operatingSegmentView = null
        }

        override fun onLongPress(view: ElementView) {
            scrollView.requestDisallowInterceptTouchEvent(true)
            if (!movingStart) {
                operatingTrack = view.parent as ViewGroup
                movingStart = true
                view.alpha = 0.5f
                view.z += 10
                (view.parent as ViewGroup).z += 10
                originRect.set(totalSegmentViewMap[view]?.rect!!)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    private fun initRects() {
        var offset = 0
        val width = 250
        val m1 = mutableMapOf<Int, ViewInfo>()
        val n = 5
        for (i in 1..n) {
            val rect = Rect(offset, 0, offset + width, TRACK_HEIGHT)
            m1[i] = ViewInfo(rect)
            offset += width
        }
        segments.add(m1)

        val m2 = mutableMapOf<Int, ViewInfo>()
        offset = 0
        for (i in n + 1..2 * n) {
            val rect = Rect(offset, 0, offset + width, TRACK_HEIGHT)
            m2[i] = ViewInfo(rect)
            offset += width
        }
        segments.add(m2)
        fullUpdate()
    }

    private fun insertSegmentToTrack(id: Int, oldTrackIndex: Int, newTrackIndex: Int, rect: Rect) {
        val newRect = Rect(rect.left, 0, rect.right, TRACK_HEIGHT)
        val map = mutableMapOf<Int, ViewInfo>()
        map[id] = ViewInfo(newRect)
        segments[oldTrackIndex].remove(id)
        segments.add(newTrackIndex, map)
        clearEmptyTrack()
        fullUpdate()
    }

    private fun fullUpdate() {
        trackMap.clear()
        container.removeAllViews()
        totalSegmentViewMap.clear()
        var offset = 0
        for ((i, m) in segments.withIndex()) {
            val track = FrameLayout(this)
            track.clipChildren = false
            track.setBackgroundResource(R.drawable.simple_border)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, TRACK_HEIGHT)
            params.topMargin = offset
            container.addView(track, params)
            trackMap[i] = track
            track.tag = i
            addViewsToGroup(track, m)
            offset += TRACK_HEIGHT
        }
    }

    private fun clearEmptyTrack() {
        val iterator = segments.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().isEmpty()) {
                iterator.remove()
            }
        }
    }

    private fun addViewsToGroup(group: ViewGroup, map: MutableMap<Int, ViewInfo>) {
        group.removeAllViews()
        for (e in map) {
            val view = SliderView(this)
            view.setElementEventListener(elementEventListener)
            when (e.key % 3) {
                0 -> {
                    view.setBackgroundResource(R.color.color_green)
                }
                1 -> {
                    view.setBackgroundResource(R.color.color_blue)
                }
                else -> {
                    view.setBackgroundResource(R.color.red_dot_color)
                }
            }
            totalSegmentViewMap[view] = e.value
            view.tag = e.key
            e.value.track = group
            val params = FrameLayout.LayoutParams(e.value.rect.width(), e.value.rect.height())
            params.topMargin = e.value.rect.top
            params.marginStart = e.value.rect.left
            group.addView(view, params)
        }
    }

    private fun checkOverlap(view: View, info: ViewInfo): Boolean {
        val globalRect = info.getGlobalRect()
        for (e in totalSegmentViewMap) {
            if (e.key == view) {
                continue
            }
            val r = e.value.getGlobalRect()
            if (globalRect.overlap(r)) {
                return true
            }
        }
        return false
    }

    private fun handleMove(info: ViewInfo) {
        val rect = Rect(info.rect)
        // 转换成全局坐标
        rect.offset(0, info.track.top)

        detectScroll(info, rect)
        handleNewTrackTips(rect)
        handleAdsorption(rect)
        restrictWithin(rect, container)

        rect.offset(0, -info.track.top)
        info.rect.set(rect)
    }

    private val scrollListener = object :ScrollRunnable.ScrollListener {
        override fun onScrollEnd() {
            scrollMode = ScrollMode.None
        }
    }

    private fun handleScrolling(info: ViewInfo, deltaY: Int) {
        if ((scrollMode == ScrollMode.ScrollDown && deltaY < -TRACK_HEIGHT / 2)
            || (scrollMode == ScrollMode.ScrollUp && deltaY > TRACK_HEIGHT / 2)
        ) {
            stopScroll()
            log { "handleScrolling stopScroll" }
        }
    }

    private fun startScroll(direction: Int, info: ViewInfo) {
        log { "startScroll direction = $direction" }
        operatingSegmentView?.let {
            scrollAction = ScrollRunnable(it, scrollView, container, info, direction, scrollListener)
        }
        scrollView.postDelayed(scrollAction, 300)
    }

    private fun stopScroll() {
        log { "stopScroll" }
        scrollView.removeCallbacks(scrollAction)
        scrollMode = ScrollMode.None
    }

    private fun detectScroll(info: ViewInfo, globalRect: Rect) {
        val absScrollY = scrollView.height + scrollView.scrollY
        if (globalRect.bottom > absScrollY + TRACK_HEIGHT * (1 - TIPS_AREA_RATIO) / 2 && container.height - absScrollY > 0) {
//            log { "handleScroll, rect =  $globalRect, scrollView.height = ${scrollView.height}, scrollY = ${scrollView.scrollY}" }
            if (scrollMode != ScrollMode.ScrollDown) {
                scrollMode = ScrollMode.ScrollDown
                log { "ScrollDown" }
                startScroll(0, info)
            }
        } else if (globalRect.top < scrollView.scrollY - TRACK_HEIGHT * (1 - TIPS_AREA_RATIO) / 2 && scrollView.scrollY > 0) {
            if (scrollMode != ScrollMode.ScrollUp) {
                scrollMode = ScrollMode.ScrollUp
                log { "ScrollUp" }
                startScroll(1, info)
            }
        }
//        log { "scrollMode = $scrollMode, showingNewTrackTips = $showingNewTrackTips" }
    }

    private fun handleNewTrackTips(globalRect: Rect) {
        val centerY = globalRect.centerY()
        var showTips = false
        var tipsPos = 0

        for (i in 1 until trackMap.size) {
            if (centerY >= TRACK_HEIGHT * i - TIPS_AREA_HEIGHT / 2 && centerY <= TRACK_HEIGHT * i + TIPS_AREA_HEIGHT / 2) {
                // 处于新建track区间
                tipsPos = TRACK_HEIGHT * i - TIPS_VIEW_HEIGHT / 2
                showTips = true
                break
            }
        }

        if (!showTips) {
            // 最后检查顶部和底部
            if (globalRect.top < -TIPS_AREA_HEIGHT / 2) {
                tipsPos = 0
                showTips = true
            } else if (globalRect.bottom > container.height + TIPS_AREA_HEIGHT / 2) {
                tipsPos = container.height - TIPS_VIEW_HEIGHT
                showTips = true
            }
        }

        if (showTips) {
            showNewTrackTipsView(tipsPos)
        } else {
            hideNewTrackTipsView()
        }
    }

    private fun handleAdsorption(globalRect: Rect) {
        for (e in trackMap) {
            val centerX = globalRect.centerX()
            val centerY = globalRect.centerY()
            val trackRect = Rect(e.value.left, e.value.top, e.value.right, e.value.bottom)
            if (trackRect.contains(centerX, centerY)) {
                if (abs(centerY - trackRect.centerY()) < TRACK_HEIGHT * (1 - TIPS_AREA_RATIO) / 2) {
                    globalRect.offset(0, trackRect.top - globalRect.top)
                    break
                }
            }
        }
    }

    private fun restrictWithin(globalRect: Rect, viewGroup: ViewGroup) {
        val groupRect = Rect(viewGroup.left, viewGroup.top, viewGroup.right, viewGroup.bottom)
        // 在这个容器里面
        if (globalRect.left < groupRect.left) {
            globalRect.offset(groupRect.left - globalRect.left, 0)
        }
        if (globalRect.top < groupRect.top) {
            globalRect.offset(0, groupRect.top - globalRect.top)
        }
        if (globalRect.right > groupRect.right) {
            globalRect.offset(groupRect.right - globalRect.right, 0)
        }
        if (globalRect.bottom > groupRect.bottom) {
            globalRect.offset(0, groupRect.bottom - globalRect.bottom)
        }
    }

    private fun showNewTrackTipsView(globalTipsPos: Int) {
        val trackView = operatingTrack ?: return
        if (showingNewTrackTips) {
            return
        }
        showingNewTrackTips = true
        val view = newGroupTipsView ?: View(this).apply {
            newGroupTipsView = this
        }
        val trackId = trackView.tag as Int
        log { "showNewTrackTipsView at $trackId" }
        view.setBackgroundResource(R.color.color_yellow)
        view.z = 10f
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, TIPS_VIEW_HEIGHT)
        pendingNewTrackIndex = round(globalTipsPos * 1.0f / TRACK_HEIGHT).toInt()
        params.topMargin = globalTipsPos - trackView.top
        operatingTrack?.addView(view, params)
    }

    private fun hideNewTrackTipsView() {
        if (!showingNewTrackTips) {
            return
        }
        pendingNewTrackIndex = -1
        showingNewTrackTips = false
        operatingTrack?.removeView(newGroupTipsView)
    }

    class ViewInfo(val rect: Rect) {

        lateinit var track: ViewGroup

        fun getGlobalRect(): Rect {
            val r = Rect(rect)
            r.offset(0, track.top)
            return r
        }
    }

    enum class ScrollMode {
        None, ScrollDown, ScrollUp
    }

    /**
     * 边缘上下滚动的Runnable
     */
    class ScrollRunnable(private val targetView: View,
                         private val scrollView: ScrollView,
                         private val container: ViewGroup,
                         private val info: ViewInfo,
                         private val direction: Int,
                         private val listener : ScrollListener) : Runnable {
        override fun run() {
            val rect = Rect(info.rect)
            // 转换成全局坐标
            rect.offset(0, info.track.top)
            var offset = 0
            var continueScroll = false
            if (direction == 0) {
                // 向下滑动
                val absScrollY = scrollView.height + scrollView.scrollY
                if (container.height - absScrollY > 0) {
                     if (container.height - absScrollY > TRACK_HEIGHT) {
                         offset = TRACK_HEIGHT
                         continueScroll = true
                     } else {
                         offset = rect.bottom - absScrollY
                         continueScroll = false
                    }
                } else {
                    continueScroll = false
                    log { "stopScroll" }
                }
            } else {
                // 向上滑动
                if (scrollView.scrollY > 0) {
                    if (scrollView.scrollY > TRACK_HEIGHT) {
                        offset = -TRACK_HEIGHT
                        continueScroll = true
                    } else {
                        offset = -scrollView.scrollY
                        continueScroll = false
                    }
                } else {
                    continueScroll = false
                }
            }
            log { "scrollBy : $offset" }
            scrollView.smoothScrollBy(0, offset)
            if (abs(offset) >= TRACK_HEIGHT) {
                rect.offset(0, offset)
                rect.offset(0, -info.track.top)
                info.rect.set(rect)
                layoutView(targetView, info.rect)
            }
            if (continueScroll) {
                scrollView.postDelayed(this, 500)
            } else {
                listener.onScrollEnd()
            }
        }

        interface ScrollListener {
            fun onScrollEnd()
        }
    }
}
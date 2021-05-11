package com.wedream.demo.view.trackmove

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.*
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.multitrack.base.ElementView
import com.wedream.demo.view.multitrack.overlap
import kotlin.math.round

class CrossTrackMovementActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var trackContainer: FrameLayout
    private lateinit var timelineContainer: FrameLayout
    private lateinit var canvasContainer: FrameLayout

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

    // 当前在容器中的位置
    private var currentOperatingRect = Rect()

    // 按下时在容器中的位置
    private var downRect = Rect()

    // 滚动开始时的位置
    private var scrollingStartRect = Rect()
    private var puppetView: View? = null

    // 滚动相关
    private var scrollAction: ScrollRunnable? = null
    private var scrollMode = ScrollMode.None
    private var scrollStopOffset = 0

    companion object {
        val TRACK_HEIGHT = AndroidUtils.dip2pix(32)
        val TRACK_MARGIN = AndroidUtils.dip2pix(8)
        val TRACK_TOTAL_HEIGHT = TRACK_HEIGHT + TRACK_MARGIN
        private const val TIPS_VIEW_HEIGHT = 10
        private val TIPS_AREA_HEIGHT = TRACK_MARGIN

        fun translateView(view: View, rect: Rect, offsetX: Int = 0, offsetY: Int = 0) {
            view.translationX = (rect.left + offsetX).toFloat()
            view.translationY = (rect.top + offsetY).toFloat()
        }

        fun setViewBg(view: View, id: Int) {
            when (id % 3) {
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_group_move)
        scrollView = findViewById(R.id.scrollView)
        horizontalScrollView = findViewById(R.id.horizontal_scrollView)
        trackContainer = findViewById(R.id.track_container)
        timelineContainer = findViewById(R.id.timeline_container)
        canvasContainer = findViewById(R.id.canvas_container)
        trackContainer.post {
            initRects()
        }
    }

    private var elementEventListener = object : ElementView.ElementEventListener {

        override fun onActionDown(view: ElementView) {
            operatingSegmentView = view
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                puppetView ?: return
                handleMove(deltaX, deltaY)
                translateView()
                canPlace = !checkOverlap(currentOperatingRect)
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            scrollView.requestDisallowInterceptTouchEvent(false)
            if (movingStart) {
                movingStart = false
                canvasContainer.removeView(puppetView)
                puppetView = null
                scrollStopOffset = 0
                stopScroll()
                scrollView.removeCallbacks(scrollAction)
                if (showingNewTrackTips) {
                    operatingTrack?.let { track ->
                        operatingSegmentView?.let {
                            // 转化成track坐标
                            val rect = Rect(currentOperatingRect)
                            rect.offset(0, -track.top)
                            log { "newGroup at $rect" }
                            if (pendingNewTrackIndex >= 0) {
                                val id = it.tag as Int
                                val oldTrack = track.tag as Int
                                insertSegmentToTrack(id, oldTrack, pendingNewTrackIndex, rect)
                            }
                        }
                        hideNewTrackTipsView()
                    }
                } else if (!canPlace) {
                    view.visibility = View.VISIBLE
                } else {
                    // 如果可以放置，先找出移到了哪个容器里面
                    val info = totalSegmentViewMap[view] ?: return
                    val id = view.tag as Int
                    var hasMoved = false
                    for (i in segments.indices) {
                        val group = trackMap[i] ?: continue
                        val rect = Rect(group.left, group.top, group.right, group.bottom)
                        if (rect.contains(currentOperatingRect)) {
                            log { "在 group $i 中" }
                            // 在其他groups中移除
                            for (j in segments.indices) {
                                if (segments[j].containsKey(id)) {
                                    segments[j].remove(id)?.let {
                                        segments[i][id] = it
                                        it.rect.set(currentOperatingRect)
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
                val info = totalSegmentViewMap[view] ?: return
                operatingTrack = view.parent as ViewGroup
                movingStart = true
                // 隐藏原来的view
                view.visibility = View.INVISIBLE
                // 往画布添加新view
                puppetView = SliderView(this@CrossTrackMovementActivity).apply {
                    this.isClickable = false
                    setViewBg(this, view.tag as Int)
                    currentOperatingRect.set(info.getGlobalRect())
                    downRect.set(currentOperatingRect)
                    this.translationX = (currentOperatingRect.left - horizontalScrollView.scrollX).toFloat()
                    this.translationY = (currentOperatingRect.top - scrollView.scrollY).toFloat()
                    val params = FrameLayout.LayoutParams(currentOperatingRect.width(), currentOperatingRect.height())
                    canvasContainer.addView(this, params)
                    alpha = 0.7f
                }
                operatingSegmentView = view
                originRect.set(totalSegmentViewMap[view]?.rect!!)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    private fun initRects() {
        segments.clear()
        segments.addAll(TrackHelper.generateTrackData())
        fullUpdate()
    }

    private fun insertSegmentToTrack(id: Int, oldTrackIndex: Int, newTrackIndex: Int, rect: Rect) {
        if (newTrackIndex == segments.size) {
            trackContainer.post {
                scrollView.scrollTo(0, trackContainer.height)
            }
        }
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
        trackContainer.removeAllViews()
        totalSegmentViewMap.clear()
        var offset = 0
        for ((i, m) in segments.withIndex()) {
            val track = FrameLayout(this)
            track.setBackgroundResource(R.color.color_gray)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, TRACK_HEIGHT)
            params.topMargin = offset
            trackContainer.addView(track, params)
            trackMap[i] = track
            track.tag = i
            addViewsToGroup(track, m)
            offset += TRACK_TOTAL_HEIGHT
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
            setViewBg(view, e.key)
            totalSegmentViewMap[view] = e.value
            view.tag = e.key
            e.value.track = group
            val params = FrameLayout.LayoutParams(e.value.rect.width(), e.value.rect.height())
            params.topMargin = e.value.rect.top
            params.marginStart = e.value.rect.left
            group.addView(view, params)
        }
    }

    private fun checkOverlap(globalRect: Rect): Boolean {
        for (e in totalSegmentViewMap) {
            val r = e.value.getGlobalRect()
            if ((e.key.tag as Int?) == (operatingSegmentView?.tag as Int?)) {
                continue
            }
            if (globalRect.overlap(r)) {
                return true
            }
        }
        return false
    }

    private fun handleMove(deltaX: Int, deltaY: Int) {
        val rect = Rect(downRect)
        rect.offset(deltaX, deltaY)

        var showTips = false
        var tipsPos = 0
        val absScrollY = scrollView.height + scrollView.scrollY
        val absScrollX = horizontalScrollView.width + horizontalScrollView.scrollX

        if (isScrolling(scrollMode)) {
            // 正在滚动，检查是否要停止滚定
            if (scrollMode == ScrollMode.ScrollLeft || scrollMode == ScrollMode.ScrollRight) {
                if ((rect.left > horizontalScrollView.scrollX + TRACK_HEIGHT * 2) && scrollMode == ScrollMode.ScrollLeft) {
                    endScroll()
                }
                if ((rect.right < absScrollX - TRACK_HEIGHT * 2) && scrollMode == ScrollMode.ScrollRight) {
                    endScroll()
                }
            } else if (scrollMode == ScrollMode.ScrollUp || scrollMode == ScrollMode.ScrollDown) {
                if (scrollMode == ScrollMode.ScrollUp) {
                    if (rect.top > scrollView.scrollY + TRACK_HEIGHT / 2) {
                        endScroll()
                    }
                } else if (scrollMode == ScrollMode.ScrollDown) {
                    if (rect.bottom < absScrollY + TRACK_HEIGHT / 2) {
                        endScroll()
                    }
                }
            }
        } else {
            // 没有在滚动
            when {
                rect.top < scrollView.scrollY - TRACK_HEIGHT / 2 && scrollView.scrollY > 0 -> {
                    log { "checkOperationMode top scroll" }
                    if (scrollMode != ScrollMode.ScrollUp) {
                        scrollMode = ScrollMode.ScrollUp
                        startScroll()
                    }
                }
                rect.bottom > absScrollY + TRACK_HEIGHT / 2 && trackContainer.height - absScrollY > 0 -> {
                    log { "checkOperationMode bottom scroll" }
                    if (scrollMode != ScrollMode.ScrollDown) {
                        scrollMode = ScrollMode.ScrollDown
                        startScroll()
                    }
                }
                rect.left < horizontalScrollView.scrollX + TRACK_HEIGHT
                        && horizontalScrollView.scrollX > 0
                        && (deltaX < -10 || scrollMode == ScrollMode.Pending) -> {
                    log { "checkOperationMode left scroll" }
                    if (scrollMode != ScrollMode.ScrollLeft) {
                        scrollMode = ScrollMode.ScrollLeft
                        startScroll()
                    }
                }
                rect.right > absScrollX - TRACK_HEIGHT
                        && timelineContainer.width - absScrollX > 0
                        && (deltaX > 10 || scrollMode == ScrollMode.Pending) -> {
                    log { "checkOperationMode right scroll" }
                    if (scrollMode != ScrollMode.ScrollRight) {
                        scrollMode = ScrollMode.ScrollRight
                        startScroll()
                    }
                }
            }
        }

        when {
            rect.top < -TRACK_HEIGHT / 2 -> {
                log { "checkOperationMode top insert" }
                tipsPos = 0
                showTips = true
                rect.set(rect.left, currentOperatingRect.top, rect.right, currentOperatingRect.bottom)
            }
            rect.bottom > trackContainer.bottom + TRACK_HEIGHT / 2 -> {
                log { "checkOperationMode bottom insert" }
                tipsPos = trackContainer.height - TIPS_VIEW_HEIGHT
                showTips = true
                rect.set(rect.left, currentOperatingRect.top, rect.right, currentOperatingRect.bottom)
            }
            else -> {
                var offset = 0
                log { "checkOperationMode else" }
                val centerY = rect.centerY()
                for (e in trackMap) {
                    if (offset < trackContainer.height && centerY in offset + TRACK_HEIGHT..offset + TRACK_TOTAL_HEIGHT) {
                        // 处于新建track区间
                        tipsPos = offset + TRACK_HEIGHT + (TRACK_MARGIN - TIPS_VIEW_HEIGHT) / 2
                        showTips = true
                        log { "checkOperationMode newTips" }
                        break
                    }
                    offset += TRACK_TOTAL_HEIGHT
                }
            }
        }

        if (showTips) {
            showNewTrackTipsView(tipsPos)
        } else {
            hideNewTrackTipsView()
        }
        adsorptionRestrict(rect)
        currentOperatingRect.set(rect)
    }

    private val scrollListener = object : ScrollRunnable.ScrollListener {

        override fun onScrolling(offsetX: Int, offsetY: Int) {
            log { "onScrolling = $offsetY" }
            currentOperatingRect.offset(offsetX, offsetY)
        }

        override fun onScrollEnd() {
            endScroll()
            // 因为滚动，可能产生了错位，需要重新吸附一下
//            adsorptionRestrict(currentOperatingRect)
//            translateView()
        }
    }

    private fun translateView() {
        puppetView?.let {
            if (isScrolling(scrollMode)) return
            translateView(it, currentOperatingRect, -horizontalScrollView.scrollX, -scrollView.scrollY)
        }
    }

    private fun startScroll() {
        log { "startScroll" }
        if (scrollMode == ScrollMode.ScrollUp || scrollMode == ScrollMode.ScrollDown) {
            scrollAction = VerticalScrollRunnable(scrollView, trackContainer, scrollMode, scrollListener)
        } else if (scrollMode == ScrollMode.ScrollLeft || scrollMode == ScrollMode.ScrollRight) {
            scrollAction = HorizontalScrollRunnable(horizontalScrollView, timelineContainer, scrollMode, scrollListener)
        }
        scrollView.postDelayed(scrollAction, 300)
        scrollingStartRect.set(currentOperatingRect)
    }

    private fun endScroll() {
        log { "endScroll" }
        scrollView.removeCallbacks(scrollAction)
        if (scrollMode != ScrollMode.None) {
            scrollMode = ScrollMode.Pending
        }
    }

    private fun stopScroll() {
        log { "stopScroll" }
        scrollView.removeCallbacks(scrollAction)
        scrollMode = ScrollMode.None
    }

    private fun adsorptionRestrict(globalRect: Rect) {
        // 首先找出可视范围内有哪几条轨
        val canvasRect = Rect(canvasContainer.left, canvasContainer.top, canvasContainer.right, canvasContainer.bottom)
        canvasRect.offset(horizontalScrollView.scrollX, scrollView.scrollY)

        val rectList = mutableListOf<Rect>()
        var upperTop = Int.MAX_VALUE
        var lowerTop = Int.MIN_VALUE
        for (e in trackMap) {
            val trackRect = Rect(e.value.left, e.value.top, e.value.right, e.value.bottom)
            if (trackRect.overlap(canvasRect)) {
                rectList.add(trackRect)
                if (trackRect.top < upperTop) {
                    upperTop = trackRect.top
                }
                if (trackRect.top > lowerTop) {
                    lowerTop = trackRect.top
                }
            }
        }

        // 限制不能滑出可视范围
        if (globalRect.top < upperTop) {
            globalRect.set(globalRect.left, upperTop, globalRect.right, upperTop + TRACK_HEIGHT)
        } else if (globalRect.top > lowerTop) {
            globalRect.set(globalRect.left, lowerTop, globalRect.right, lowerTop + TRACK_HEIGHT)
        }
        if (globalRect.left >= canvasRect.right) {
            globalRect.set(currentOperatingRect.left, globalRect.top, currentOperatingRect.right, globalRect.bottom)
        }
        if (globalRect.right <= canvasRect.left) {
            globalRect.set(currentOperatingRect.left, globalRect.top, currentOperatingRect.right, globalRect.bottom)
        }

        // 限制在轨道中间
        val centerX = globalRect.centerX()
        val centerY = globalRect.centerY()
        for (trackRect in rectList) {
            if (trackRect.contains(centerX, centerY)) {
                globalRect.offset(0, trackRect.top - globalRect.top)
            }
        }
    }

    private fun showNewTrackTipsView(globalTipsPos: Int) {
        if (showingNewTrackTips) {
            return
        }
        showingNewTrackTips = true
        val view = newGroupTipsView ?: View(this).apply {
            newGroupTipsView = this
        }
        view.setBackgroundResource(R.color.color_yellow)
        view.alpha = 0.5f
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, TIPS_VIEW_HEIGHT)
        pendingNewTrackIndex = round(globalTipsPos * 1.0f / TRACK_TOTAL_HEIGHT).toInt()
        params.topMargin = globalTipsPos - scrollView.scrollY
        canvasContainer.addView(view, params)
    }

    private fun hideNewTrackTipsView() {
        if (!showingNewTrackTips) {
            return
        }
        pendingNewTrackIndex = -1
        showingNewTrackTips = false
        canvasContainer.removeView(newGroupTipsView)
    }

    class ViewInfo(val rect: Rect) {

        lateinit var track: ViewGroup

        fun getGlobalRect(): Rect {
            val r = Rect(rect)
            r.offset(0, track.top)
            return r
        }
    }
}
package com.wedream.demo.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

    lateinit var container: FrameLayout

    private val totalSegmentViewMap = mutableMapOf<View, ViewInfo>()
    private val segments = mutableListOf<MutableMap<Int, ViewInfo>>()
    private val trackMap = mutableMapOf<Int, ViewGroup>()

    private var movingStart = false
    private var canPlace = false
    private var originRect = Rect()
    private var newGroupTipsView: View? = null
    private var showingNewTrackTips = false
    private var extraOffsetY = 0

    // 正在操作的Track
    private var operatingTrack: ViewGroup? = null
    private var operatingSegmentView: View? = null
    private var movingStatus = MOVING_STATUS_NORMAL

    private val trackHeight = AndroidUtils.dip2pix(100)
    private val tipsAreaHeight = 30

    companion object {
        const val MOVING_STATUS_NORMAL = 0 // 没有高亮
        const val MOVING_STATUS_HIGHLIGHT = 1 // 高亮
    }

    private var elementEventListener = object : ElementView.ElementEventListener {

        override fun onActionDown(view: ElementView) {
            operatingSegmentView = view
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                val info = totalSegmentViewMap[view] ?: return
                info.rect.offset(deltaX, deltaY + extraOffsetY)
                log { "offsetY = $deltaY" }
                handleMove(info)
                layoutView(view, info.rect)
                canPlace = !checkOverlap(view, info)
                if (!canPlace) {
                    view.alpha = 0.5f
                } else {
                    view.alpha = 1.0f
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                movingStart = false
                view.z -= 10
                (view.parent as ViewGroup).z -= 10
                if (newGroupTipsView?.isAttachedToWindow == true) {
                    operatingTrack?.let { track ->
                        hideNewTrackTipsView()
                        totalSegmentViewMap[operatingSegmentView]?.let {
                            log { "newGroup at ${it.rect}" }
                            val index = checkNewGroupPos(it.getGlobalRect())
                            val id = view.tag as Int
                            val oldTrack = track.tag as Int
                            insertSegmentToTrack(id, oldTrack, index, it.rect)
                        }
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
            if (!movingStart) {
                extraOffsetY = 0
                operatingTrack = view.parent as ViewGroup
                movingStart = true
                view.z += 10
                (view.parent as ViewGroup).z += 10
                originRect.set(totalSegmentViewMap[view]?.rect!!)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_group_move)
        container = findViewById(R.id.container)
        container.post {
            initRects()
        }
    }

    private fun initRects() {
        var offset = 0
        val width = 150
        val m1 = mutableMapOf<Int, ViewInfo>()
        for (i in 1..5) {
            val rect = Rect(offset, 0, offset + width, trackHeight)
            m1[i] = ViewInfo(rect)
            offset += width
        }
        segments.add(m1)

        val m2 = mutableMapOf<Int, ViewInfo>()
        offset = 0
        for (i in 6..10) {
            val rect = Rect(offset, 0, offset + width, trackHeight)
            m2[i] = ViewInfo(rect)
            offset += width
        }
        segments.add(m2)
        fullUpdate()
    }

    private fun insertSegmentToTrack(id: Int, oldTrackIndex: Int, newTrackIndex: Int, rect: Rect) {
        val newRect = Rect(rect.left, 0, rect.right, trackHeight)
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
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, trackHeight)
            params.topMargin = offset
            container.addView(track, params)
            trackMap[i] = track
            track.tag = i
            addViewsToGroup(track, m)
            offset += trackHeight
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

        val centerY = rect.centerY()
        var showTips = false
        var tipsPos = 0
        for (i in 1 until trackMap.size) {
            if (centerY >= trackHeight * i - tipsAreaHeight / 2 && centerY <= trackHeight * i + tipsAreaHeight / 2) {
                // 处于新建track区间
                tipsPos = trackHeight * i - tipsAreaHeight / 2
                showTips = true
                break
            }
        }
        if (showTips) {
            if (movingStatus == MOVING_STATUS_NORMAL) {
                movingStatus = MOVING_STATUS_HIGHLIGHT
            }
            showNewTrackTipsView(tipsPos)
        } else {
            handleSticky(rect)
            if (movingStatus == MOVING_STATUS_HIGHLIGHT) {
                // 滑出高亮区，要进行吸附
                log { "make Adsorption" }
                handleAdsorption(rect)
                movingStatus = MOVING_STATUS_NORMAL
            }
            hideNewTrackTipsView()
        }

        restrictWithin(rect, container)
        rect.offset(0, -info.track.top)
        info.rect.set(rect)
    }

    private fun handleAdsorption(globalRect: Rect) {
        for (e in trackMap) {
            val centerX = globalRect.centerX()
            val centerY = globalRect.centerY()
            val trackRect = Rect(e.value.left, e.value.top, e.value.right, e.value.bottom)
            if (trackRect.contains(centerX, centerY)) {
                extraOffsetY += trackRect.top - globalRect.top
                globalRect.offset(0, extraOffsetY)
                log { "extraOffsetY = $extraOffsetY" }
//                log { "centerY = $centerY, trackRect = $trackRect, globalRect = $globalRect" }
                break
            }
        }
    }

    private fun handleSticky(globalRect: Rect) {
        for (e in trackMap) {
            val centerX = globalRect.centerX()
            val centerY = globalRect.centerY()
            val trackRect = Rect(e.value.left, e.value.top, e.value.right, e.value.bottom)
            if (trackRect.contains(centerX, centerY)) {
                if (abs(centerY - trackRect.centerY()) < trackHeight * 0.3) {
                    // 增加开始拖动的粘滞性
                    globalRect.set(globalRect.left, trackRect.top, globalRect.right, trackRect.bottom)
                    log { "handleSticky" }
                }
                break
            }
        }
    }

    private fun handleNewTrackTips(rect: Rect) {
        val centerY = rect.centerY()
        var showTips = false
        var tipsPos = 0
        for (i in 1 until trackMap.size) {
            if (centerY >= trackHeight * i - tipsAreaHeight / 2 && centerY <= trackHeight * i + tipsAreaHeight / 2) {
                tipsPos = trackHeight * i - tipsAreaHeight / 2
                showTips = true
                break
            }
        }
        if (showTips) {
            showNewTrackTipsView(tipsPos)
        } else {
            hideNewTrackTipsView()
        }
    }

    private fun checkNewGroupPos(rect: Rect): Int {
        val centerY = rect.centerY()
        val height = rect.height()
        val index = round(centerY * 1.0 / height)
        log { "index = $index" }
        return index.toInt()
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
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 20)
        params.topMargin = globalTipsPos - trackView.top
        operatingTrack?.addView(view, params)
    }

    private fun hideNewTrackTipsView() {
        if (!showingNewTrackTips) {
            return
        }
        showingNewTrackTips = false
        operatingTrack?.removeView(newGroupTipsView)
    }

    private fun layoutView(view: View, rect: Rect) {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = rect.left
        params.topMargin = rect.top
        view.layoutParams = params
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
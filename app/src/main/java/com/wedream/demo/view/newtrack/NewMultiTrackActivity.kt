package com.wedream.demo.view.newtrack

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.*
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.multitrack.base.ElementView

class NewMultiTrackActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var trackContainer: FrameLayout
    private lateinit var canvasContainer: FrameLayout
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var timelineContainer: FrameLayout

    private var puppetView: View? = null
    private var movingStart = false

    // 滚动相关
    private var scrollAction: ScrollRunnable? = null
    private var scrollMode = ScrollMode.None
    private var transInfo = TransInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_multi_track)
        trackContainer = findViewById(R.id.track_container)
        canvasContainer = findViewById(R.id.canvas_container)
        horizontalScrollView = findViewById(R.id.horizontal_scrollView)
        timelineContainer = findViewById(R.id.timeline_container)
        scrollView = findViewById(R.id.scrollView)
        addTracks()
    }

    private var elementEventListener = object : ElementView.ElementEventListener {
        override fun onActionDown(view: ElementView) {
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (!movingStart) return
            transInfo.x = deltaX - horizontalScrollView.scrollX
            transInfo.y = deltaY - scrollView.scrollY
            log { "deltaX = $deltaX, deltaY = $deltaY" }
            handleMove(transInfo)
            adsorptionRestrict(transInfo)
            if (transInfo.curRect.left < 0) {
                transInfo.x += -transInfo.curRect.left
            }
            if (transInfo.curRect.right > canvasContainer.right) {
                transInfo.x += canvasContainer.right - transInfo.curRect.right
            }
            if (transInfo.curRect.top < 0 ) {
                transInfo.y += -transInfo.curRect.top
            }
            if (transInfo.curRect.bottom > canvasContainer.bottom) {
                transInfo.y += canvasContainer.bottom - transInfo.curRect.bottom
            }
            puppetView?.let {
                it.translationX = transInfo.x.toFloat()
                it.translationY = transInfo.y.toFloat()
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            if (!movingStart) return
            movingStart = false
            scrollView.requestDisallowInterceptTouchEvent(false)
            puppetView?.let {
                canvasContainer.removeView(it)
            }
            puppetView = null
            stopScroll()
        }

        override fun onLongPress(view: ElementView) {
            view.parent ?: return
            if (movingStart) return
            movingStart = true
            scrollView.requestDisallowInterceptTouchEvent(true)
            view.visibility = View.INVISIBLE
            val parent = view.parent as ViewGroup
            val downRect = Rect(view.left, view.top, view.right, view.bottom)
            downRect.offset(0, parent.top)
            log { "downRect = $downRect" }
            transInfo.setDownRect(downRect)
            val id = view.tag as Int
            puppetView = SliderView(this@NewMultiTrackActivity).apply {
                CrossTrackMovementActivity.setViewBg(this, id)
                val params = FrameLayout.LayoutParams(downRect.width(), downRect.height())
                params.marginStart = downRect.left
                params.topMargin = downRect.top
                alpha = 0.7f
                canvasContainer.addView(this, params)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    private fun adsorptionRestrict(transInfo: TransInfo) {
        if (scrollMode == ScrollMode.ScrollUp || scrollMode == ScrollMode.ScrollDown) return

        val centerX = transInfo.curRect.centerX()
        val centerY = transInfo.curRect.centerY()

        for (i in 0 until trackContainer.childCount) {
            val trackView = trackContainer.getChildAt(i)
            val trackRect = trackView.rect()
            trackRect.offset(0, -scrollView.scrollY)
            if (trackRect.contains(centerX, centerY)) {
                transInfo.y += trackRect.top - transInfo.curRect.top
            }
        }
    }

    private fun handleMove(transInfo: TransInfo) {
        if (isScrolling(scrollMode)) {
            if (scrollMode == ScrollMode.ScrollUp && transInfo.curRect.top > canvasContainer.top - CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                endScroll()
            }
            if (scrollMode == ScrollMode.ScrollDown && transInfo.curRect.bottom < canvasContainer.bottom + CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                endScroll()
            }
            if (scrollMode == ScrollMode.ScrollLeft && transInfo.curRect.left > canvasContainer.left + CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                endScroll()
            }
            if (scrollMode == ScrollMode.ScrollRight && transInfo.curRect.right < canvasContainer.right - CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                endScroll()
            }
        } else {
            log { "transInfo.curRect = ${transInfo.curRect}" }
            if (transInfo.curRect.top < canvasContainer.top - CrossTrackMovementActivity.TRACK_HEIGHT / 2 && scrollView.scrollY > 0) {
                if (scrollMode != ScrollMode.ScrollUp) {
                    scrollMode = ScrollMode.ScrollUp
                    startScroll()
                }
            } else if (transInfo.curRect.bottom > canvasContainer.bottom + CrossTrackMovementActivity.TRACK_HEIGHT / 2
                && scrollView.scrollY + canvasContainer.height < trackContainer.height
            ) {
                if (scrollMode != ScrollMode.ScrollDown) {
                    scrollMode = ScrollMode.ScrollDown
                    startScroll()
                }
            }
            if (transInfo.curRect.left < canvasContainer.left + CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                if (scrollMode != ScrollMode.ScrollLeft) {
                    scrollMode = ScrollMode.ScrollLeft
                    startScroll()
                }
            } else if (transInfo.curRect.right > canvasContainer.right - CrossTrackMovementActivity.TRACK_HEIGHT / 2) {
                if (scrollMode != ScrollMode.ScrollRight) {
                    scrollMode = ScrollMode.ScrollRight
                    startScroll()
                }
            }
        }
    }

    private val scrollListener = object : ScrollRunnable.ScrollListener {

        override fun onScrolling(offsetX: Int, offsetY: Int) {
        }

        override fun onScrollEnd() {
            endScroll()
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

    private fun addTracks() {
        var offset = 0
        for (i in 0..10) {
            val track = FrameLayout(this)
            track.setBackgroundResource(R.color.color_gray)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, CrossTrackMovementActivity.TRACK_HEIGHT)
            params.topMargin = offset
            trackContainer.addView(track, params)
            addViewsToGroup(i, track)
            offset += CrossTrackMovementActivity.TRACK_TOTAL_HEIGHT
        }
    }

    private fun addViewsToGroup(id: Int, trackView: ViewGroup) {
        val width = 200
        val view = SliderView(this)
        view.setElementEventListener(elementEventListener)
        CrossTrackMovementActivity.setViewBg(view, id)
        view.tag = id
        val params = FrameLayout.LayoutParams(width, CrossTrackMovementActivity.TRACK_HEIGHT)
        params.marginStart = (width * id)
        params.topMargin = 0
        trackView.addView(view, params)
    }
}

class TransInfo {
    private var downRect = Rect()
    var curRect = Rect()
    var x = 0
        set(value) {
            field = value
            curRect.set(downRect.left + value, downRect.top + y, downRect.right + value, downRect.bottom + y)
            log { "curRect = $curRect" }
        }
    var y = 0
        set(value) {
            field = value
            curRect.set(downRect.left + x, downRect.top + value, downRect.right + x, downRect.bottom + value)
        }

    fun setDownRect(rect: Rect) {
        downRect.set(rect)
        curRect.set(rect)
    }
}

fun View.rect(): Rect {
    return Rect(left, top, right, bottom)
}
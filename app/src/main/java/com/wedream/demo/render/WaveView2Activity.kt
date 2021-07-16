package com.wedream.demo.render

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.SeekBar
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.KtUtils.asTo
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.timeline.utils.TimeRange
import com.wedream.demo.videoeditor.timeline.widget.AbsTouchListener
import com.wedream.demo.view.MyFrameLayout
import kotlin.math.min

class WaveView2Activity : BaseActivity() {

    private lateinit var segmentView: FrameLayout
    private lateinit var waveContainer: FrameLayout
    private lateinit var trackView: MyFrameLayout
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var seekBar: SeekBar

    private var segmentSize = 10
    private var originLength = 2000
    private var visibleRange = TimeRange(0, DeviceParams.SCREEN_WIDTH * 2)
    private var addedWaveViewRange = TimeRange(0, 0)
    private val data = WaveViewActivity.benchMark3
    private val segmentCount = data.size / segmentSize + 1
    private val segmentWidth = originLength / segmentCount // 一个片段的像素宽度
    private var marginOffset = AndroidUtils.dip2pix(500)
    private var scale = 1.0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_view2)
        segmentView = findViewById(R.id.segment_view)
        waveContainer = findViewById(R.id.wave_view_container)
        trackView = findViewById(R.id.track_view)
        seekBar = findViewById(R.id.seek_bar)
        trackView.setSpecificDimen(AndroidUtils.dip2pix(2000), AndroidUtils.dip2pix(100))
        horizontalScrollView = findViewById(R.id.horizontal_scrollView)
        horizontalScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            visibleRange.set(scrollX, scrollX + visibleRange.duration)
            log { visibleRange }
            updateWaveViews()
        }
        segmentView.setOnTouchListener(object : AbsTouchListener(this@WaveView2Activity, true) {

            var movingLeft = false
            var originWidth = 0

            override fun onActionDown(v: View, x: Int, y: Int) {
                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                movingLeft = x <= v.width - x
                originWidth = v.layoutParams.width
            }

            override fun onMoveStart(v: View, deltaX: Int, deltaY: Int) {

            }

            override fun onMoving(v: View, deltaX: Int, deltaY: Int) {
                segmentView.layoutParams.asTo<ViewGroup.MarginLayoutParams>()?.let {
                    if (movingLeft) {
                        it.marginStart += deltaX
                        it.width -= deltaX
                    } else {
                        it.width = originWidth + deltaX
                    }
                    segmentView.layoutParams = it
                    if (movingLeft) {
                        updateWaveViewOffset(deltaX)
                    }
                }
            }

            override fun onMoveStop(v: View, deltaX: Int, deltaY: Int) {
            }

            override fun onActionUp(v: View, deltaX: Int, deltaY: Int) {
                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
            }

            override fun onActionCancel(v: View) {
            }

            override fun onLongPress(v: View) {
            }

            override fun onClick(v: View) {
            }
        })
        seekBar.progress = 25
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val newScale = progressToScale(progress)
                onScaleChange(newScale / scale, newScale)
                scale = newScale
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        updateWaveViews()
    }

    private fun progressToScale(progress: Int): Double {
        val speed = when {
            progress < 25 -> {
                0.1 + (progress - 0.0) / 25 * 0.9
            }
            progress >= 25 && progress < 50 -> {
                1.0 + (progress - 25.0) / 25 * 1.0
            }
            progress >= 50 && progress < 75 -> {
                2.0 + (progress - 50.0) / 25 * 3.0
            }
            progress >= 75 -> {
                5.0 + (progress - 75.0) / 25 * 5.0
            }
            else -> {
                1.0
            }
        }
        return (speed * 100).toInt() / 100.0
    }

    private fun updateWaveViews() {
        for (i in 0 until segmentCount) {
            val start = segmentWidth * i
            val end = start + segmentWidth
            if (!visibleRange.overlap(start + marginOffset, end + marginOffset)) {
                continue
            }
            if (addedWaveViewRange.overlap(start + marginOffset, end + marginOffset)) {
                continue
            }
            val startIndex = segmentSize * i
            val endIndex = segmentSize * (i + 1)
            val waveView = WaveView(this)
            log { "addView" }
            waveView.setBackgroundResource(R.drawable.simple_border)
            waveView.setItemWidth(segmentWidth.toFloat() / segmentSize)
            val params = FrameLayout.LayoutParams(segmentWidth, FrameLayout.LayoutParams.MATCH_PARENT)
            params.marginStart = start
            waveView.setData(data, startIndex, min(endIndex, data.lastIndex))
            waveContainer.addView(waveView, params)
            addedWaveViewRange.right = start + marginOffset + segmentWidth
        }
    }

    private fun onScaleChange(scaleDelta: Double, newScale: Double) {
//        segmentView.layoutParams?.let {
//            it.width = (it.width * scaleDelta).toInt()
//            log { "segmentView = ${it.width}" }
//            segmentView.layoutParams = it
//        }
        segmentView.scaleX = newScale.toFloat()
    }

    private fun updateWaveViewOffset(deltaX: Int) {
        for (i in 0 until waveContainer.childCount) {
            val child = waveContainer.getChildAt(i)
            child.layoutParams.asTo<ViewGroup.MarginLayoutParams>()?.let {
                it.marginStart -= deltaX
                child.layoutParams = it
            }
        }
    }
}
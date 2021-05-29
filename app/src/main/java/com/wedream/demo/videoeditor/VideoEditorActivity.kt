package com.wedream.demo.videoeditor

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.OperateScaleHelper
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import com.wedream.demo.videoeditor.timeline.widget.MyHorizontalScrollView
import com.wedream.demo.videoeditor.timeline.widget.TimelineAxisView
import com.wedream.demo.view.MyFrameLayout
import com.wedream.demo.view.canvas.MyCanvasView
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity.Companion.setViewBg

class VideoEditorActivity : DisposableActivity() {

    private lateinit var timelineContainer: MyFrameLayout
    private lateinit var trackContainer: MyFrameLayout
    private lateinit var scrollView: MyHorizontalScrollView
    private lateinit var canvasView: MyCanvasView
    private lateinit var timelineAxisView: TimelineAxisView

    private var videoEditor = VideoEditor()
    private val timelineViewModel = TimelineViewModel(videoEditor)
    private var timelineWrapWidth = TimelineUtils.time2Width(5.0 * 60, timelineViewModel.getScale())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_editor)
        initViews()
        initListeners()
        timelineViewModel.loadProject()
    }

    private fun initViews() {
        timelineContainer = findViewById(R.id.timeline_container)
        trackContainer = findViewById(R.id.track_container)
        scrollView = findViewById(R.id.timeline_scroll_view)
        canvasView = findViewById(R.id.timeline_canvas)
        timelineAxisView = findViewById(R.id.timeline)
        trackContainer.layoutParams?.let {
            it as ViewGroup.MarginLayoutParams
            it.marginStart = DeviceParams.SCREEN_WIDTH / 2
            trackContainer.layoutParams = it
        }
        scrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            if (scrollX > timelineViewModel.getRealTimeWidth()) {
                scrollView.scrollTo(timelineViewModel.getRealTimeWidth(), 0)
                return@setOnScrollChangeListener
            }
            onScrollChanged(scrollX)
        }
        timelineAxisView.setOnScaleListener(object : OperateScaleHelper.OnScaleListener {
            override fun onScaling(scale: Float) {
                timelineViewModel.setScale(scale.toDouble())
            }

            override fun onScaleStart(scale: Float) {
            }

            override fun onScaleEnd(scale: Float) {
            }
        })
    }

    private fun initListeners() {
        addToAutoDisposable(timelineViewModel.message.subscribe({
            val segments = timelineViewModel.getSegments()
            for (s in segments) {
                val view = SliderView(this)
                trackContainer.addView(view, s.width, FrameLayout.LayoutParams.MATCH_PARENT)
                view.translationX = s.left.toFloat()
                setViewBg(view, s.id)
            }
            while (timelineWrapWidth < timelineViewModel.getRealTimeWidth()) {
                timelineWrapWidth *= 2
            }
            timelineContainer.setSpecificDimen(timelineWrapWidth, AndroidUtils.dip2pix(100))
            trackContainer.setSpecificDimen(timelineWrapWidth, AndroidUtils.dip2pix(100))
        }, {
            it.printStackTrace()
        }))
    }

    private fun onScrollChanged(scrollX: Int) {
        timelineViewModel.getSegmentByPos(scrollX)?.let {
            val left = it.left - scrollX + DeviceParams.SCREEN_WIDTH / 2
            val right = left + it.width
            canvasView.setRect(left, 0, right, AndroidUtils.dip2pix(100))
        }
    }
}
package com.wedream.demo.videoeditor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.wedream.demo.R
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.videoeditor.controller.PreviewController
import com.wedream.demo.videoeditor.controller.TimelineCanvasController
import com.wedream.demo.videoeditor.controller.TrackContainerController
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.editor.EditorState
import com.wedream.demo.videoeditor.editor.EditorUpdater
import com.wedream.demo.videoeditor.menu.MenuController
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.statusbar.EditorStatusBarController
import com.wedream.demo.videoeditor.timeline.config.Config.MAIN_TRACK_HEIGHT
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.OperateScaleHelper
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import com.wedream.demo.videoeditor.timeline.widget.ClickDetectTouchListener
import com.wedream.demo.videoeditor.timeline.widget.MyHorizontalScrollView
import com.wedream.demo.videoeditor.timeline.widget.TimelineAxisView
import com.wedream.demo.view.MyFrameLayout
import java.util.*

class VideoEditorActivity : DisposableActivity() {

    private lateinit var timelineContainer: MyFrameLayout
    private lateinit var trackContainer: MyFrameLayout
    private lateinit var scrollView: MyHorizontalScrollView
    private lateinit var timelineAxisView: TimelineAxisView
    private lateinit var menuContainer: LinearLayout
    private lateinit var editorStatusBar: LinearLayoutCompat
    private lateinit var preview: TextView

    private var editorState = EditorState()
    private var editorUpdater = EditorUpdater(editorState)
    private var editorGovernor = EditorGovernor()
    private val timelineViewModel = TimelineViewModel(editorGovernor)
    private var timelineWrapWidth = TimelineUtils.time2Width(5.0 * 60, timelineViewModel.getScale())
    private var scalingStartScale = 1.0
    private var scalingStartScrollX = 0

    val objects = listOf(timelineViewModel, editorGovernor)

    init {
        editorGovernor.setTimelineViewModel(timelineViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_editor)
        initViews()
        initListeners()
        initControllers()
        editorGovernor.loadProject(editorUpdater)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        timelineContainer = findViewById(R.id.timeline_container)
        trackContainer = findViewById(R.id.track_container)
        scrollView = findViewById(R.id.timeline_scroll_view)
        timelineAxisView = findViewById(R.id.timeline)
        menuContainer = findViewById(R.id.menu_container)
        preview = findViewById(R.id.preview_text)
        editorStatusBar = findViewById(R.id.editor_status_bar)

        scrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            if (scrollX > timelineViewModel.getRealTimeWidth()) {
                scrollView.scrollTo(timelineViewModel.getRealTimeWidth(), 0)
                return@setOnScrollChangeListener
            }
            timelineViewModel.setScrollX(scrollX)
        }
        timelineAxisView.setOnScaleListener(object : OperateScaleHelper.OnScaleListener {
            override fun onScaling(scale: Float) {
                // 缩放的时候要进行平移
                val pos = scalingStartScrollX * (scale / scalingStartScale)
                scrollView.scrollTo(pos.toInt(), 0)
                timelineViewModel.setScale(scale.toDouble())
            }

            override fun onScaleStart(scale: Float) {
                scalingStartScrollX = scrollView.scrollX
                scalingStartScale = timelineViewModel.getScale()
            }

            override fun onScaleEnd(scale: Float) {
            }
        })
        scrollView.setOnTouchListener(object : ClickDetectTouchListener() {
            override fun onclick(view: View) {
                MessageChannel.sendMessage(TimeLineMessageHelper.MSG_TIMELINE_BLANK_CLICK)
            }
        })
        timelineContainer.setSpecificDimen(timelineWrapWidth, MAIN_TRACK_HEIGHT)
        trackContainer.setSpecificDimen(timelineWrapWidth, MAIN_TRACK_HEIGHT)
    }

    private fun initListeners() {
        addToAutoDisposable(MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
                TimeLineMessageHelper.unpackTimelineChangedMessage(it) {
                    handleTimelineChanged()
                }
            } else if (it.what == TimeLineMessageHelper.MSG_SEGMENT_SELECTED) {
                TimeLineMessageHelper.unpackSegmentSelectedMsg(it) {
                    timelineViewModel.getSegment(it)?.let {
                        if (timelineViewModel.getScrollX() !in it.left..it.right) {
                            seekTo(it.left + 1)
                        }
                    }
                }
            }
        })
    }

    private fun seekTo(pos: Int) {
        scrollView.scrollTo(pos, 0)
    }

    private fun handleTimelineChanged() {
        val realWidth = timelineViewModel.getRealTimeWidth()
        var changed = false
        while (timelineWrapWidth < realWidth) {
            timelineWrapWidth *= 2
            changed = true
        }
        if (changed) {
            timelineContainer.setSpecificDimen(timelineWrapWidth, MAIN_TRACK_HEIGHT)
            trackContainer.setSpecificDimen(timelineWrapWidth, MAIN_TRACK_HEIGHT)
        }
        if (scrollView.scrollX > timelineViewModel.getRealTimeWidth()) {
            scrollView.scrollTo(timelineViewModel.getRealTimeWidth(), 0)
        }
    }

    private fun initControllers() {
        val trackContainerController = TrackContainerController()
        trackContainerController.create(timelineAxisView)
        trackContainerController.bind(objects)
        val timelineCanvasController = TimelineCanvasController()
        timelineCanvasController.create(timelineAxisView)
        timelineCanvasController.bind(objects)
        val menuController = MenuController()
        menuController.create(menuContainer)
        menuController.bind(objects)
        val previewController = PreviewController()
        previewController.create(preview)
        previewController.bind(objects)
        val editorStatusBarController = EditorStatusBarController()
        editorStatusBarController.create(editorStatusBar)
        editorStatusBarController.bind(objects)
    }
}
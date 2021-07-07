package com.wedream.demo.videoeditor.controller

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.inject.Inject
import com.wedream.demo.util.KtUtils.ifNullAndElse
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.decorview.DecorViewManager
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.ActionType
import com.wedream.demo.videoeditor.timeline.data.Segment
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.widget.SegmentTouchListener
import com.wedream.demo.view.MyFrameLayout
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity

class TrackContainerController : ViewController() {

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    @Inject
    lateinit var videoEditor: VideoEditor

    private var decorViewManager: DecorViewManager? = null

    private lateinit var trackContainer: MyFrameLayout
    private var segmentViewMap = hashMapOf<Long, View>()
    private var pendingAction = hashMapOf<Long, ActionType>()
    private var segmentTouchListener: SegmentTouchListener? = null


    override fun onBind() {
        trackContainer = findViewById(R.id.track_container)
        segmentTouchListener = SegmentTouchListener(getActivity())
        decorViewManager = DecorViewManager(videoEditor, timelineViewModel, trackContainer.context)
        initListeners()
    }

    private fun initListeners() {
        MessageChannel.subscribe {
            when (it.what) {
                TimeLineMessageHelper.MSG_TIMELINE_CHANGED -> {
                    TimeLineMessageHelper.unpackTimelineChangedMessage(it) {
                        val visibleRange = timelineViewModel.getVisibleRange()
                        for (event in it.events) {
                            if (event.actionType == ActionType.Add) {
                                val segment = timelineViewModel.getSegment(event.id) ?: continue
                                if (!visibleRange.overlap(segment.left, segment.right)) {
                                    pendingAction[segment.id] = event.actionType
                                    removeSegmentView(event.id)
                                } else {
                                    pendingAction.remove(segment.id)
                                    addSegmentView(segment)
                                }
                            } else if (event.actionType == ActionType.Delete) {
                                removeSegmentView(event.id)
                                pendingAction.remove(event.id)
                            } else if (event.actionType == ActionType.Modify) {
                                val segment = timelineViewModel.getSegment(event.id) ?: continue
                                if (!visibleRange.overlap(segment.left, segment.right)) {
                                    pendingAction[segment.id] = event.actionType
                                    removeSegmentView(event.id)
                                } else {
                                    pendingAction.remove(event.id)
                                    updateSegmentView(segment)
                                }
                            }
                        }
                        handlePendingActions()
                        decorViewManager?.handleSelect(it.currentSelectedId, trackContainer)
                    }
                }
            }
        }
    }

    private fun handlePendingActions() {
        val visibleRange = timelineViewModel.getVisibleRange()
        log { "visibleRange = $visibleRange" }
        val iterator = pendingAction.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            val segment = timelineViewModel.getSegment(entity.key)
            log { "pending segment = $segment" }
            if (segment == null) {
                iterator.remove()
                continue
            } else {
                if (visibleRange.overlap(segment.left, segment.right)) {
                    if (entity.value == ActionType.Add) {
                        addSegmentView(segment)
                    } else if (entity.value == ActionType.Modify) {
                        updateSegmentView(segment)
                    }
                    iterator.remove()
                }
            }
        }
    }

    private fun addSegmentView(segment: Segment) {
        val view = TextView(getActivity())
        segmentViewMap[segment.id] = view
        trackContainer.addView(view, segment.width, FrameLayout.LayoutParams.MATCH_PARENT)
        bindSegmentView(view, segment)
    }

    private fun removeSegmentView(id: Long) {
        segmentViewMap.remove(id)?.let {
            trackContainer.removeView(it)
        }
    }

    private fun updateSegmentView(segment: Segment) {
        segmentViewMap[segment.id].ifNullAndElse({
            addSegmentView(segment)
        }, { segmentView ->
            segmentView.layoutParams?.let {
                it.width = segment.width
                segmentView.layoutParams = it
            }
            log { "update ${segment}" }
            bindSegmentView(segmentView, segment)
        })
    }

    private fun bindSegmentView(view: View, segment: Segment) {
        view as TextView
        view.setTextColor(Color.WHITE)
        view.text = segment.id.toString()
        view.setTag(R.id.view_tag_segment, segment)
        view.setOnTouchListener(segmentTouchListener)
        CrossTrackMovementActivity.setViewBg(view, segment.id)
        view.translationX = DeviceParams.SCREEN_WIDTH * 0.5f + segment.left.toFloat()
    }
}
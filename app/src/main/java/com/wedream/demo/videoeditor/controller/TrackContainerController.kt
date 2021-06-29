package com.wedream.demo.videoeditor.controller

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.videoeditor.decorview.DecorViewManager
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.ActionType
import com.wedream.demo.videoeditor.timeline.data.Segment
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.widget.SegmentTouchListener
import com.wedream.demo.view.MyFrameLayout
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity

class TrackContainerController : ViewController<TimelineViewModel>() {

    private var decorViewManager: DecorViewManager? = null

    private lateinit var trackContainer: MyFrameLayout
    private var segmentViewMap = hashMapOf<Long, View>()
    private var segmentTouchListener: SegmentTouchListener? = null

    override fun onBind() {
        trackContainer = findViewById(R.id.track_container)
        segmentTouchListener = SegmentTouchListener(getActivity())
        decorViewManager = DecorViewManager((getModel()), trackContainer.context)
        initListeners()
    }

    private fun initListeners() {
        MessageChannel.subscribe(TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
            TimeLineMessageHelper.unpackTimelineChangedMessage(it) {
                for (event in it.events) {
                    if (event.actionType == ActionType.Add) {
                        val segment = getModel().getSegment(event.id) ?: continue
                        val view = TextView(getActivity())
                        segmentViewMap[event.id] = view
                        trackContainer.addView(view, segment.width, FrameLayout.LayoutParams.MATCH_PARENT)
                        updateSegmentView(view, segment)
                    } else if (event.actionType == ActionType.Delete) {
                        segmentViewMap.remove(event.id)?.let {
                            trackContainer.removeView(it)
                        }
                    } else if (event.actionType == ActionType.Modify) {
                        val segment = getModel().getSegment(event.id) ?: continue
                        segmentViewMap[event.id]?.let { segmentView ->
                            // view.layout(0, 0, s.width, TIMELINE_HEIGHT)
                            segmentView.layoutParams?.let {
                                it.width = segment.width
                                segmentView.layoutParams = it
                            }
                            updateSegmentView(segmentView, segment)
                        }
                    }
                }
                decorViewManager?.handleSelect(it.currentSelectedId, trackContainer)
            }
        }
    }

    private fun updateSegmentView(view: View, segment: Segment) {
        view as TextView
        view.setTextColor(Color.WHITE)
        view.text = segment.id.toString()
        view.setTag(R.id.view_tag_segment, segment)
        view.setOnTouchListener(segmentTouchListener)
        CrossTrackMovementActivity.setViewBg(view, segment.id)
        view.translationX = DeviceParams.SCREEN_WIDTH * 0.5f + segment.left.toFloat()
    }
}
package com.wedream.demo.videoeditor.controller

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.TimeLineMessageHelper.MSG_TIMELINE_CHANGE
import com.wedream.demo.videoeditor.timeline.widget.SegmentTouchListener
import com.wedream.demo.view.MyFrameLayout
import com.wedream.demo.view.multitrack.SegmentView
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity

class TrackContainerController : Controller<TimelineViewModel>() {

    private lateinit var trackContainer: MyFrameLayout
    private var segmentMap = hashMapOf<Int, View>()
    private var segmentTouchListener: SegmentTouchListener? = null

    override fun onBind() {
        trackContainer = findViewById(R.id.track_container)
        segmentTouchListener = SegmentTouchListener(getActivity())
        initListeners()
    }

    private fun initListeners() {
        addToAutoDisposes(getModel().message.subscribe({
            if (it == MSG_TIMELINE_CHANGE) {
                val segments = getModel().getSegments()
                for (s in segments) {
                    var view = segmentMap[s.id]
                    if (view == null) {
                        view = TextView(getActivity())
                        segmentMap[s.id] = view
                        trackContainer.addView(view, s.width, FrameLayout.LayoutParams.MATCH_PARENT)
                    } else {
//                        view.layout(0, 0, s.width, TIMELINE_HEIGHT)
                        view.layoutParams?.let {
                            it.width = s.width
                            view.layoutParams = it
                        }
                    }
                    view as TextView
                    view.setTextColor(Color.WHITE)
                    view.text = s.id.toString()
                    view.setTag(R.id.view_tag_segment, s)
                    view.setOnTouchListener(segmentTouchListener)
                    CrossTrackMovementActivity.setViewBg(view, s.id)
                    view.translationX = s.left.toFloat()
                }
            }
        }, {
            it.printAndDie()
        }))
    }
}
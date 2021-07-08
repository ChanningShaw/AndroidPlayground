package com.wedream.demo.videoeditor.controller

import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.inject.Inject
import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.config.Config
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.view.canvas.MyCanvasView

class TimelineCanvasController : ViewController() {

    private lateinit var canvasView: MyCanvasView

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    private var lastSelectedId = Constants.INVALID_ID

    override fun onBind() {
        super.onBind()
        canvasView = findViewById(R.id.timeline_canvas)
        initListeners()
    }

    private fun initListeners() {
        MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
                TimeLineMessageHelper.unpackTimelineChangedMessage(it) {
                    lastSelectedId = it.currentSelectedId
//                    invalidateHighlight()
                }
            }
        }
    }

    private fun invalidateHighlight() {
        if (lastSelectedId == Constants.INVALID_ID) {
            timelineViewModel.getCurrentSegment()?.let {
                val left = it.left - timelineViewModel.getScrollX() + DeviceParams.SCREEN_WIDTH / 2
                val right = left + it.width
                canvasView.setRect(left, 0, right, Config.MAIN_TRACK_HEIGHT)
            }
        } else {
            canvasView.setRect(0, 0, -1, -1)
        }
    }
}
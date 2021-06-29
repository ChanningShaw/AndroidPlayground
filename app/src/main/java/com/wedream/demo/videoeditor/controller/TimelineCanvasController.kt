package com.wedream.demo.videoeditor.controller

import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.config.Config
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.view.canvas.MyCanvasView

class TimelineCanvasController : ViewController<TimelineViewModel>() {

    private lateinit var canvasView: MyCanvasView

    override fun onBind() {
        super.onBind()
        canvasView = findViewById(R.id.timeline_canvas)
        initListeners()
    }

    private fun initListeners() {
        MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_TIMELINE_CHANGED
                || it.what == TimeLineMessageHelper.MSG_TIMELINE_SCROLL_CHANGED
            ) {
                invalidateHighlight()
            }
        }
    }

    private fun invalidateHighlight() {
        getModel().getCurrentSegment()?.let {
            val left = it.left - getModel().getScrollX() + DeviceParams.SCREEN_WIDTH / 2
            val right = left + it.width
            canvasView.setRect(left, 0, right, Config.TIMELINE_HEIGHT)
        }
    }
}
package com.wedream.demo.videoeditor.controller

import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.videoeditor.timeline.config.Config
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.TimeLineMessageHelper
import com.wedream.demo.view.canvas.MyCanvasView

class TimelineCanvasController : Controller<TimelineViewModel>() {

    private lateinit var canvasView: MyCanvasView

    override fun onBind() {
        super.onBind()
        canvasView = findViewById(R.id.timeline_canvas)
        initListeners()
    }

    private fun initListeners() {
        addToAutoDisposes(getModel().message.subscribe {
            if (it == TimeLineMessageHelper.MSG_TIMELINE_CHANGE
                || it == TimeLineMessageHelper.MSG_TIMELINE_SCROLL_CHANGED
            ) {
                invalidateHighlight()
            }
        })
    }

    private fun invalidateHighlight() {
        getModel().getCurrentSegment()?.let {
            val left = it.left - getModel().getScrollX() + DeviceParams.SCREEN_WIDTH / 2
            val right = left + it.width
            canvasView.setRect(left, 0, right, Config.TIMELINE_HEIGHT)
        }
    }
}
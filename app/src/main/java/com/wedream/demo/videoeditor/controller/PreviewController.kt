package com.wedream.demo.videoeditor.controller

import android.widget.TextView
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class PreviewController : ViewController<TimelineViewModel>() {

    lateinit var previewTextView: TextView

    override fun onBind() {
        super.onBind()
        previewTextView = getRootView() as TextView
        MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_TIMELINE_CHANGED || it.what == TimeLineMessageHelper.MSG_TIMELINE_SCROLL_CHANGED) {
                updatePreview()
            }
            updatePreview()
        }
    }

    private fun updatePreview() {
        getModel().getCurrentSegment()?.let {
            previewTextView.text = it.id.toString()
        }
    }
}
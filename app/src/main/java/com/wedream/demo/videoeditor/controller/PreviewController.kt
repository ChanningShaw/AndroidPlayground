package com.wedream.demo.videoeditor.controller

import android.widget.TextView
import com.wedream.demo.inject.Inject
import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class PreviewController : ViewController() {

    lateinit var previewTextView: TextView

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    var currentSegmentId = Constants.INVALID_ID

    override fun onBind() {
        super.onBind()
        previewTextView = getRootView() as TextView
        MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
                updatePreview()
            }
            updatePreview()
        }
    }

    private fun updatePreview() {
        val newSegmentId = timelineViewModel.getCurrentSegment()?.id ?: Constants.INVALID_ID
        if (currentSegmentId != newSegmentId) {
            currentSegmentId = newSegmentId
            previewTextView.text = currentSegmentId.toString()
        }
    }
}
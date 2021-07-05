package com.wedream.demo.videoeditor.controller

import android.view.View
import android.widget.TextView
import com.wedream.demo.inject.Inject
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class PreviewController(rootView: View) : ViewController(rootView) {

    lateinit var previewTextView: TextView

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

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
        timelineViewModel.getCurrentSegment()?.let {
            previewTextView.text = it.id.toString()
        }
    }
}
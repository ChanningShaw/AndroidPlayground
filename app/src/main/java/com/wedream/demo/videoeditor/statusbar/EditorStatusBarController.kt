package com.wedream.demo.videoeditor.statusbar

import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.inject.Inject
import com.wedream.demo.videoeditor.controller.ViewController
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class EditorStatusBarController : ViewController() {
    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    @Inject
    lateinit var editorGovernor: EditorGovernor

    lateinit var currentTimeTextView: TextView

    override fun onBind() {
        currentTimeTextView = findViewById(R.id.editor_current_time)
        addToAutoDisposes(MessageChannel.subscribe(TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
            val time = String.format("%.2f", timelineViewModel.getCurrentTime())
            val projectDuration = String.format("%.2f", editorGovernor.getProjectDuration())
            currentTimeTextView.text = "$time/$projectDuration"
        })
    }
}
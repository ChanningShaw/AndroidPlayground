package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper

class EditorState : EditorUpdater.EditorUpdateListener {

    var selectedSegmentId = -1L

    var editorUpdater : EditorUpdater? = null

    init {
        initListeners()
    }

    fun attachEditorUpdater(editorUpdater: EditorUpdater) {
        this.editorUpdater = editorUpdater
    }

    private fun initListeners() {
        MessageChannel.subscribe(TimeLineMessageHelper.MSG_SEGMENT_CLICK) {
            TimeLineMessageHelper.unpackSegmentClickMsg(it) {
                selectedSegmentId = it.id
            }
            editorUpdater?.notifyEditorStateChanged()
        }
    }

    override fun onEditorUpdate(data: EditorData) {

    }
}
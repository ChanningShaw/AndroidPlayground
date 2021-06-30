package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper

class EditorState : EditorUpdater.EditorUpdateListener {

    var selectedSegmentId = Constants.INVALID_ID

    var editorUpdater : EditorUpdater? = null

    init {
        initListeners()
    }

    fun attachEditorUpdater(editorUpdater: EditorUpdater) {
        this.editorUpdater = editorUpdater
    }

    private fun initListeners() {
        MessageChannel.subscribe {
            if (it.what == TimeLineMessageHelper.MSG_SEGMENT_CLICK) {
                TimeLineMessageHelper.unpackSegmentClickMsg(it) {
                    if (it.id == selectedSegmentId) {
                        // 反选
                        selectedSegmentId = Constants.INVALID_ID
                    } else {
                        selectedSegmentId = it.id
                        MessageChannel.sendMessage(TimeLineMessageHelper.packSegmentSelectedMsg(it.id))
                    }
                }
            } else if (it.what == TimeLineMessageHelper.MSG_TIMELINE_BLANK_CLICK) {
                selectedSegmentId = Constants.INVALID_ID
            }
            editorUpdater?.notifyEditorStateChanged()
        }
    }

    override fun onEditorUpdate(data: EditorData) {

    }
}
package com.wedream.demo.videoeditor.message

import com.wedream.demo.videoeditor.editor.EditorData

object TimeLineMessageHelper {
    const val MSG_TIMELINE_CHANGED = 0
    const val MSG_TIMELINE_SCROLL_CHANGED = 1

    fun packTimelineChangedMessage(data: EditorData): KyMessage {
        return KyMessage.obtain().apply {
            what = MSG_TIMELINE_CHANGED
            arg1 = data
        }
    }

    fun unpackTimelineChangedMessage(msg: KyMessage, block: (data: EditorData) -> Unit) {
        block.invoke(msg.arg1 as EditorData)
    }
}
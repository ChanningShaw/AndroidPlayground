package com.wedream.demo.videoeditor.message

import com.wedream.demo.videoeditor.editor.EditorData
import com.wedream.demo.videoeditor.timeline.data.Segment

object TimeLineMessageHelper {
    const val MSG_TIMELINE_CHANGED = 0
    const val MSG_SEGMENT_CLICK = 2
    const val MSG_TIMELINE_SEEK_TO = 3
    const val MSG_SEGMENT_SELECTED = 4
    const val MSG_TIMELINE_BLANK_CLICK = 5

    fun packTimelineChangedMessage(data: EditorData): KyMessage {
        return KyMessage.obtain().apply {
            what = MSG_TIMELINE_CHANGED
            arg1 = data
        }
    }

    fun unpackTimelineChangedMessage(msg: KyMessage, block: (data: EditorData) -> Unit) {
        block.invoke(msg.arg1 as EditorData)
    }

    fun packSegmentClickMsg(segment: Segment): KyMessage {
        return KyMessage.obtain().apply {
            what = MSG_SEGMENT_CLICK
            arg1 = segment
        }
    }

    fun unpackSegmentClickMsg(msg: KyMessage, block: (segment: Segment) -> Unit) {
        block.invoke(msg.arg1 as Segment)
    }

    fun packSegmentSelectedMsg(id: Long): KyMessage {
        return KyMessage.obtain().apply {
            what = MSG_SEGMENT_SELECTED
            arg1 = id
        }
    }

    fun unpackSegmentSelectedMsg(msg: KyMessage, block: (id: Long) -> Unit) {
        block.invoke(msg.arg1 as Long)
    }
}
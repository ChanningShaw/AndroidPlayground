package com.wedream.demo.view.multitrack

import com.wedream.demo.view.multitrack.base.SegmentData

data class TextSegmentData(override val id: Long,
                           override var trackLevel: Int,
                           override var start: Int,
                           override var end: Int)
    : SegmentData(id, trackLevel, start, end) {

    override fun <T> copy(): T {
        return TextSegmentData(id, trackLevel, start, end) as T
    }
}
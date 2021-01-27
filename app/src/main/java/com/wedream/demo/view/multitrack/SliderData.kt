package com.wedream.demo.view.multitrack

class SliderData(id: Long,
                 trackLevel: Int,
                 start: Int,
                 end: Int)
    : TrackElementData(id, trackLevel, start, end) {
    private var targetSegmentId = -1L

    fun getTargetSegmentId(): Long {
        return targetSegmentId
    }

    fun setTargetSegmentId(id: Long) {
        this.targetSegmentId = id
    }
}
package com.wedream.demo.view.multitrack

class SliderData(id: Long,
                 left: Int,
                 width: Int,
                 trackLevel: Int)
    : TrackElementData(id, left, width, trackLevel) {

    var targetSegmentId = -1L
}
package com.wedream.demo.videoeditor.timeline.data

class VideoSegment(
    id: Long,
    left: Int,
    right: Int,
    path: String,
) : Segment(id, left, right, SegmentType.Video) {

}
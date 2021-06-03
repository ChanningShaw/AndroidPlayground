package com.wedream.demo.videoeditor.timeline.data

class TextSegment(
    id: Int,
    left: Int, right: Int,
    text: String = ""
) : Segment(id, left, right, SegmentType.Text) {

}
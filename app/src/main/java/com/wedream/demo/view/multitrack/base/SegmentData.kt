package com.wedream.demo.view.multitrack.base

abstract class SegmentData(val id: Long,
                           var trackLevel: Int,
                           var start: Int,
                           var end: Int){
    fun update(deltaTrackLevel: Int, deltaStart: Int, deltaEnd: Int) {
        this.trackLevel += deltaTrackLevel
        this.start += deltaStart
        this.end += deltaEnd
    }
}
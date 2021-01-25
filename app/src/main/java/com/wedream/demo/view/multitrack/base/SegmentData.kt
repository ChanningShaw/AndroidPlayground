package com.wedream.demo.view.multitrack.base

abstract class SegmentData(open val id: Long,
                           open var trackLevel: Int,
                           open var start: Int,
                           open var end: Int){

    var isSelected = false

    fun update(deltaTrackLevel: Int, deltaStart: Int, deltaEnd: Int) {
        this.trackLevel += deltaTrackLevel
        this.start += deltaStart
        this.end += deltaEnd
    }

    fun set(trackLevel: Int, start: Int, end: Int) {
        this.trackLevel = trackLevel
        this.start = start
        this.end = end
    }

    fun set(other: SegmentData) {
        this.trackLevel = other.trackLevel
        this.start = other.start
        this.end = other.end
    }

    abstract  fun <T> copy(): T
}
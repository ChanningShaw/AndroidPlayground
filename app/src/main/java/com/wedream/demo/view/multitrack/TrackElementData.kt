package com.wedream.demo.view.multitrack

import com.wedream.demo.view.multitrack.base.ElementData

open class TrackElementData(id: Long,
                            private var trackLevel: Int,
                            start: Int,
                            end: Int)
    : ElementData(id, start, end) {

    override fun <T> copy(): T {
        return TrackElementData(getId(), trackLevel, getStart(), getEnd()) as T
    }

    override fun set(other: ElementData) {
        super.set(other)
        if (other is TrackElementData) {
            this.trackLevel = other.trackLevel
        }
    }

     fun setTrackLevel(level: Int) {
        this.trackLevel = level
    }

    fun getTrackLevel(): Int {
        return trackLevel
    }
}
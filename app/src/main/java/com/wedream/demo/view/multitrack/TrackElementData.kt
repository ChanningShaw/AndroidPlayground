package com.wedream.demo.view.multitrack

import com.wedream.demo.view.multitrack.base.ElementData

data class TrackElementData(override val id: Long,
                            var trackLevel: Int,
                            override var start: Int,
                            override var end: Int)
    : ElementData(id, start, end) {

    override fun <T> copy(): T {
        return TrackElementData(id, trackLevel, start, end) as T
    }

    override fun set(other: ElementData) {
        super.set(other)
        if (other is TrackElementData) {
            this.trackLevel = other.trackLevel
        }
    }
}
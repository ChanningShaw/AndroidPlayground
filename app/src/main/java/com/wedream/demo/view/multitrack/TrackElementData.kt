package com.wedream.demo.view.multitrack

import com.wedream.demo.view.multitrack.base.ElementData

open class TrackElementData(id: Long) : ElementData(id) {

    companion object {
        const val DEFAULT_TRACK_HEIGHT = 100
        const val DEFAULT_TRACK_MARGIN = 20
    }

    constructor(id: Long, left: Int, width: Int, level: Int, levelHeight: Int = DEFAULT_TRACK_HEIGHT, levelMargin: Int = DEFAULT_TRACK_MARGIN) : this(id) {
        this.left = left
        this.width = width
        this.height = levelHeight
        this.levelHeight = levelHeight
        this.levelMargin = levelMargin
        this.trackLevel = level
    }

    var levelHeight = 0
        private set
    var levelMargin = 0
        private set

    var trackLevel = 0
    set(value) {
        field = value
        top = value * (levelHeight + levelMargin)
    }

    override fun <T> copy(): T {
        return TrackElementData(id, left, top, trackLevel) as T
    }

    override fun set(other: ElementData) {
        super.set(other)
        if (other is TrackElementData) {
            this.trackLevel = other.trackLevel
        }
    }
}
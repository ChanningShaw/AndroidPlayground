package com.wedream.demo.app.track

import java.io.Serializable

class TrackNodeSnapshot(val id: String,
                        private val trackParams: TrackParams,
                        private val previous: TrackNodeSnapshot?) : ITrackNode, Serializable {

    var trackThreadId: String? = null

    override fun parentTrackNode(): ITrackNode? {
        return null
    }

    override fun previousTrackNode(): ITrackNode? {
        return previous
    }

    override fun fillTrackParams(params: TrackParams) {
        params.putAll(this.trackParams)
    }

    override fun toString(): String {
        return "TrackNodeSnapshot[id:$id](tid:$trackThreadId)"
    }
}
package com.wedream.demo.app.track

import android.os.SystemClock
import java.util.*

class ContinuousEvent(eventName: String) : Event(eventName) {
    companion object {
        private val eventsPool = WeakHashMap<String, ContinuousEvent>()

        fun get(eventName: String): ContinuousEvent? {
            return eventsPool[eventName]
        }
    }

    private var startTime = -1L

    internal fun start() {
        startTime = SystemClock.elapsedRealtime()
        eventsPool.remove(name)
        eventsPool[name] = this
    }

    internal fun end(node: ITrackNode? = null, updater: ((TrackParams, Long) -> Unit)? = null) {
        eventsPool.remove(name)
        updater?.invoke(params, SystemClock.elapsedRealtime() - startTime)
        node?.let {
            node(it)
        }
        emit()
    }
}
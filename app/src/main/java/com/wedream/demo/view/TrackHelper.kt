package com.wedream.demo.view

import android.graphics.Rect
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity

object TrackHelper {
    fun generateTrackData(): List<MutableMap<Int, CrossTrackMovementActivity.ViewInfo>> {

        val segments = mutableListOf<MutableMap<Int, CrossTrackMovementActivity.ViewInfo>>()

        var offset = 0
        val width = 250
        val m1 = mutableMapOf<Int, CrossTrackMovementActivity.ViewInfo>()
        val n = 5
        for (i in 1..n) {
            val rect = Rect(offset, 0, offset + width, CrossTrackMovementActivity.TRACK_HEIGHT)
            m1[i] = CrossTrackMovementActivity.ViewInfo(rect)
            offset += width
        }
        segments.add(m1)

        val m2 = mutableMapOf<Int, CrossTrackMovementActivity.ViewInfo>()
        offset = 0
        for (i in n + 1..2 * n) {
            val rect = Rect(offset, 0, offset + width, CrossTrackMovementActivity.TRACK_HEIGHT)
            m2[i] = CrossTrackMovementActivity.ViewInfo(rect)
            offset += width
        }
        segments.add(m2)
        return segments
    }
}
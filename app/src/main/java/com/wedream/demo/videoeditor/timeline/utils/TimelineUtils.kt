package com.wedream.demo.videoeditor.timeline.utils

object TimelineUtils {
    // 1s = 40 pix
    private const val THUMBNAIL_WIDTH = 40

    fun width2time(width: Int, scale: Double = 1.0): Double {
        return width / scale / THUMBNAIL_WIDTH
    }

    fun time2Width(time: Double, scale: Double = 1.0): Int {
        return (time * scale * THUMBNAIL_WIDTH).toInt()
    }
}
package com.wedream.demo.videoeditor.timeline.data

data class Segment(
    val id: Int,
    var left: Int,
    var right: Int
) {
    val width
        get() = right - left

    fun contains(pos: Int): Boolean {
        return pos in left..right
    }
}
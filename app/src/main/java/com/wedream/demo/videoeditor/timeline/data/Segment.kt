package com.wedream.demo.videoeditor.timeline.data

open class Segment(
    val id: Int,
    var left: Int,
    var right: Int,
    val type: SegmentType
) {
    val width
        get() = right - left

    fun contains(pos: Int): Boolean {
        return pos in left..right
    }

    override fun toString(): String {
        return "{id=$id, [$left, $right], type=$type}"
    }
}

enum class SegmentType {
    Text,
}
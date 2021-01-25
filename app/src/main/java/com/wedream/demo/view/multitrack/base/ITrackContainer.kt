package com.wedream.demo.view.multitrack.base

interface ITrackContainer<S : SegmentData> {

    fun newTrack(): Int

    fun addSegment(data: S)
}
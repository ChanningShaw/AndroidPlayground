package com.wedream.demo.videoeditor.timeline.data

import androidx.lifecycle.ViewModel
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.timeline.utils.TimeLineMessageHelper
import com.wedream.demo.videoeditor.timeline.utils.TimeLineMessageHelper.MSG_TIMELINE_CHANGE
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class TimelineViewModel(private val videoEditor: VideoEditor) : ViewModel() {

    private var segmentMap = hashMapOf<Int, Segment>()
    private var timelineRealWidth = 0
    private var scale = 1.0
    private var timelineScrollX = 0

    private var _message = PublishSubject.create<Int>()

    fun loadProject() {
        segmentMap.clear()
        timelineRealWidth = 0
        val assets = videoEditor.getAssets()
        for (asset in assets) {
            val start = TimelineUtils.time2Width(asset.start, scale)
            val end = TimelineUtils.time2Width(asset.end, scale)
            timelineRealWidth += (end - start)
            segmentMap[asset.id] = Segment(asset.id, start, end)
        }
        sendMessage(MSG_TIMELINE_CHANGE)
    }

    val message: Flowable<Int> = _message.toFlowable(BackpressureStrategy.MISSING)
    fun sendMessage(what: Int) {
        _message.onNext(what)
    }

    fun getSegments(): List<Segment> {
        return segmentMap.values.toList()
    }

    fun getScale(): Double {
        return scale
    }

    fun setScale(scale: Double) {
        this.scale = scale
        loadProject()
    }

    fun setScrollX(scrollX: Int) {
        this.timelineScrollX = scrollX
        sendMessage(TimeLineMessageHelper.MSG_TIMELINE_SCROLL_CHANGED)
    }

    fun getScrollX(): Int {
        return timelineScrollX
    }

    fun getRealTimeWidth(): Int {
        return timelineRealWidth
    }

    fun getSegmentByPos(pos: Int): Segment? {
        for (s in segmentMap.values) {
            if (s.contains(pos)) {
                return s
            }
        }
        return null
    }

    fun getCurrentSegment(): Segment? {
        return getSegmentByPos(timelineScrollX)
    }
}
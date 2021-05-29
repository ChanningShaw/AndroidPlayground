package com.wedream.demo.videoeditor.timeline.data

import androidx.lifecycle.ViewModel
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class TimelineViewModel(private val videoEditor: VideoEditor) : ViewModel() {

    private var segmentMap = hashMapOf<Int, Segment>()
    private var timelineRealWidth = 0
    private var scale = 1.0

    private var _message = PublishSubject.create<Int>()

    fun loadProject() {
        val assets = videoEditor.loadProject()
        for (asset in assets) {
            val start = TimelineUtils.time2Width(asset.start)
            val end = TimelineUtils.time2Width(asset.end)
            timelineRealWidth += (end - start)
            segmentMap[asset.id] = Segment(asset.id, start, end)
        }
        sendMessage(0)
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
}
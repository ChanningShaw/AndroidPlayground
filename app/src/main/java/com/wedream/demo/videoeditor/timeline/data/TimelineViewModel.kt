package com.wedream.demo.videoeditor.timeline.data

import android.view.Choreographer
import androidx.lifecycle.ViewModel
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.videoeditor.editor.EditorData
import com.wedream.demo.videoeditor.editor.EditorUpdater
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.ActionEvent
import com.wedream.demo.videoeditor.project.ActionType
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.asset.MainTrackAsset
import com.wedream.demo.videoeditor.project.asset.PlacedAsset
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.timeline.utils.TimeRange
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils

class TimelineViewModel(private val editorGovernor: EditorGovernor) : ViewModel() {

    private var segmentMap = hashMapOf<Long, Segment>()
    private var timelineRealWidth = 0
    private var scale = 1.0
    private var timelineScrollX = 0
    private var visibleRange = TimeRange(-DeviceParams.SCREEN_WIDTH, DeviceParams.SCREEN_WIDTH * 2)

    init {
        EditorUpdater.getNotifier().registerEditorUpdateListener(object : EditorUpdater.EditorUpdateListener{
            override fun onEditorUpdate(data: EditorData) {
                updateTimeline(data)
            }
        })
    }

    private fun updateTimeline(editorData: EditorData) {
        timelineRealWidth = TimelineUtils.time2Width(editorGovernor.getProjectDuration(), scale)
        for (event in editorData.events) {
            if (event.actionType == ActionType.Add) {
                val asset = editorGovernor.getAsset(event.id) ?: continue
                addSegment(asset)
            } else if (event.actionType == ActionType.Delete) {
                segmentMap.remove(event.id)
            } else if (event.actionType == ActionType.Modify) {
                val asset = editorGovernor.getAsset(event.id) ?: continue
                if (asset is PlacedAsset) {
                    val start = TimelineUtils.time2Width(asset.getStart(), scale)
                    val end = if (asset is ISpeed) {
                        start + TimelineUtils.time2Width(asset.duration / asset.getSpeed(), scale)
                    } else {
                        TimelineUtils.time2Width(asset.getEnd(), scale)
                    }
                    segmentMap[asset.id]?.let {
                        it.left = start
                        it.right = end
                    }
                }
            }
        }
        if (editorData.mainTrackModified) {
            // 主轨被修改了，修改全部重新计算
            val assets = editorGovernor.getAssets()
            var assetStart = 0.0
            for (asset in assets) {
                val realDuration = if (asset is ISpeed) {
                    asset.duration / asset.getSpeed()
                } else {
                    asset.duration
                }
                val start = TimelineUtils.time2Width(assetStart, scale)
                val end = start + TimelineUtils.time2Width(realDuration, scale)
                segmentMap[asset.id]?.let {
                    it.left = start
                    it.right = end.toInt()
                    editorData.events.add(ActionEvent(it.id, ActionType.Modify))
                }
                assetStart += realDuration
            }
        }
        MessageChannel.sendMessage(TimeLineMessageHelper.packTimelineChangedMessage(editorData))
    }

    private fun addSegment(asset: Asset) {
        if (asset is PlacedAsset) {
            if (asset is ISpeed) {
                val start = TimelineUtils.time2Width(asset.getStart(), scale)
                val end = start + TimelineUtils.time2Width(asset.duration / asset.getSpeed(), scale)
                segmentMap[asset.id] = generateSegment(asset, start, end)
            } else {
                val start = TimelineUtils.time2Width(asset.getStart(), scale)
                val end = start + TimelineUtils.time2Width(asset.getEnd(), scale)
                segmentMap[asset.id] = generateSegment(asset, start, end)
            }
        } else {
            segmentMap[asset.id] = generateSegment(asset, 0, 0)
        }
    }

    private fun generateSegment(asset: Asset, start: Int, end: Int): Segment {
        return when (asset) {
            is MainTrackAsset -> {
                VideoSegment(asset.id, 0, 0, "")
            }
            else -> {
                TextSegment(asset.id, start, end, asset.id.toString())
            }
        }
    }

    fun getVisibleRange(): TimeRange {
        return TimeRange(visibleRange.left, visibleRange.right)
    }

    fun getSegments(): List<Segment> {
        return segmentMap.values.toList()
    }

    fun getScale(): Double {
        return scale
    }

    fun setScale(scale: Double) {
        this.scale = scale
        val editorData = EditorData()
        editorData.mainTrackModified = true
        for (asset in editorGovernor.getAssets()) {
            editorData.events.add(ActionEvent(asset.id, ActionType.Modify))
        }
        updateTimeline(editorData)
    }

    fun setScrollX(scrollX: Int) {
        this.timelineScrollX = scrollX
        visibleRange.set(-DeviceParams.SCREEN_WIDTH + scrollX, DeviceParams.SCREEN_WIDTH * 2 + scrollX)
        val editorData = EditorData()
        editorData.timelineScrollX = timelineScrollX
        updateTimeline(editorData)
    }

    fun getScrollX(): Int {
        return timelineScrollX
    }

    fun getCurrentTime(): Double {
        return TimelineUtils.width2time(timelineScrollX, scale)
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

    fun getSegment(id: Long): Segment? {
        return segmentMap[id]
    }

    fun getCurrentSegment(): Segment? {
        return getSegmentByPos(timelineScrollX)
    }
}
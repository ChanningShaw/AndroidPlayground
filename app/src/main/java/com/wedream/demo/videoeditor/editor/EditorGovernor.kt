package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class EditorGovernor {

    private val videoEditor = VideoEditor()
    private var timelineViewModel: TimelineViewModel? = null

    fun loadProject(updater: EditorUpdater) {
        videoEditor.loadProject(updater)
    }

    fun setTimelineViewModel(model: TimelineViewModel) {
        timelineViewModel = model
    }

    fun getCurrentTime(): Double {
        return timelineViewModel?.getCurrentTime() ?: 0.0
    }

    fun getProjectDuration(): Double {
        return videoEditor.getProjectDuration()
    }

    fun getAssetByTime(time: Double): Asset? {
        return videoEditor.getAssetByTime(time)
    }

    fun getAsset(id: Long): Asset? {
        return videoEditor.getAsset(id)
    }

    fun getAssets(): List<Asset> {
        return videoEditor.getAssets()
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.AddAssetAction -> {
                val id = IdUtils.nextId()
                val newAsset =
                    Asset(
                        id,
                        action.assetType,
                        action.duration
                    )
                videoEditor.addAssetAt(newAsset, action.pos)
            }
            is Action.DeleteAssetAction -> {
                videoEditor.deleteAsset(action.id)
            }

            is Action.SplitAssetAction -> {
                val originAsset = videoEditor.getAsset(action.id) ?: return
                val asset = Asset(
                    IdUtils.nextId(),
                    originAsset.type,
                    originAsset.fixDuration,
                    action.pos,
                    originAsset.getClipEnd()
                )
                videoEditor.addAssetAt(asset, getCurrentTime())
                originAsset.setClipEnd(action.pos)
            }

            is Action.CopyAssetAction -> {
                val originAsset = videoEditor.getAsset(action.id) ?: return
                val newAsset = originAsset.cloneObject()
                videoEditor.addAssetAt(newAsset, getCurrentTime())
            }

            else -> {

            }
        }
    }
}
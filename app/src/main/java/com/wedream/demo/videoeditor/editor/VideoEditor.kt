package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.VideoProject
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class VideoEditor : EditorUpdater.EditorUpdateListener {

    lateinit var timelineViewModel: TimelineViewModel
    private var project = VideoProject()
    private var projectDuration = 0.0

    companion object {
    }

    fun loadProject(updater: EditorUpdater){
        project.load(updater)
    }

    fun getCurrentTime(): Double {
        return timelineViewModel.getCurrentTime()
    }

    override fun onEditorUpdate(data: EditorData) {
        projectDuration = 0.0
        for (asset in getAssets()) {
            projectDuration += asset.duration
        }
    }

    fun getAssets(): List<Asset> {
        return project.getAssets()
    }

    fun getAsset(id: Long): Asset? {
        return project.getAsset(id)
    }

    fun getAssetByTime(time: Double): Asset? {
        return project.getAssetByTime(time)
    }

    fun getProjectDuration(): Double {
        return projectDuration
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
                addAssetAt(newAsset, action.pos)
            }
            is Action.DeleteAssetAction -> {
                project.deleteAsset(action.id)
            }

            is Action.SplitAssetAction -> {
                val originAsset = getAsset(action.id) ?: return
                val asset = Asset(
                    IdUtils.nextId(),
                    originAsset.type,
                    originAsset.fixDuration,
                    action.pos,
                    originAsset.getClipEnd()
                )
                addAssetAt(asset, getCurrentTime())
                originAsset.setClipEnd(action.pos)
            }

            is Action.CopyAssetAction -> {
                val originAsset = getAsset(action.id) ?: return
                val newAsset = originAsset.cloneObject()
                addAssetAt(newAsset, getCurrentTime())
            }

            else -> {

            }
        }
    }

    fun addAssetAt(asset: Asset, pos: Double) {
        var index = project.findAssetIndex(pos)
        if (index == -1) {
            index = project.getAssets().lastIndex
        }
        project.addAsset(asset, index + 1)
    }
}
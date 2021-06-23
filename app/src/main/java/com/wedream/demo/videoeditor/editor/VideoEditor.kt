package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.VideoProject
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class VideoEditor {

    lateinit var timelineViewModel: TimelineViewModel
    private var project = VideoProject()

    companion object {
        const val MIN_ASSET_DURATION = 0.1
    }

    fun loadProject(){
        project.load()
    }

    fun getCurrentTime(): Double {
        return timelineViewModel.getCurrentTime()
    }

    fun onProjectChange(block: (editorData: EditorData) -> Unit) {
        project.onProjectChange(block)
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
        return project.getProjectDuration()
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
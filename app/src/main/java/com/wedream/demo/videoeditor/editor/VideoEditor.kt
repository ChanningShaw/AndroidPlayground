package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.VideoProject
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class VideoEditor {

    lateinit var timelineViewModel: TimelineViewModel
    private var project = VideoProject()

    fun loadProject(){
        project.load()
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
                var start = 0.0
                var end = 0.0
                for ((i, asset) in getAssets().withIndex()) {
                    end += asset.duration
                    if (action.pos in start..end) {
                        val id = IdUtils.nextId()
                        val newAsset =
                            Asset(
                                id,
                                action.assetType,
                                action.duration
                            )
                        project.addAsset(newAsset, i + 1)
                        break
                    }
                    start += asset.duration
                }
            }
            is Action.DeleteAssetAction -> {
                project.deleteAsset(action.id)
            }
        }
    }
}
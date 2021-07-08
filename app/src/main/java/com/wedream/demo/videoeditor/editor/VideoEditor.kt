package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.VideoProject
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed

internal class VideoEditor : IEditor {

    private var project = VideoProject()
    private var projectDuration = 0.0

    companion object {
    }

    init {
        EditorUpdater.getNotifier().registerEditorUpdateListener(object : EditorUpdater.EditorUpdateListener {
            override fun onEditorUpdate(data: EditorData) {
                projectDuration = 0.0
                for (asset in getAssets()) {
                    projectDuration += if (asset is ISpeed) {
                        asset.duration / asset.getSpeed()
                    } else {
                        asset.duration
                    }
                }
            }
        }, true)
    }

    override fun loadProject(updater: EditorUpdater) {
        project.load(updater)
    }

    override fun getAssets(): List<Asset> {
        return project.getAssets()
    }

    override fun getAsset(id: Long): Asset? {
        return project.getAsset(id)
    }

    override fun getAssetByTime(time: Double): Asset? {
        return project.getAssetByTime(time)
    }

    override fun getProjectDuration(): Double {
        return projectDuration
    }

    override fun addAssetAt(asset: Asset, pos: Double) {
        var index = project.findAssetIndex(pos)
        if (index == -1) {
            index = project.getAssets().lastIndex
        }
        project.addAsset(asset, index + 1)
    }

    override fun deleteAsset(id: Long) {
        project.deleteAsset(id)
    }
}
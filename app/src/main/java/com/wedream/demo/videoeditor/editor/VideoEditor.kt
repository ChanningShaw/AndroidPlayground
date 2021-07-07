package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.VideoProject
import com.wedream.demo.videoeditor.project.asset.Asset

internal class VideoEditor {

    private var project = VideoProject()
    private var projectDuration = 0.0

    companion object {
    }

    init {
        EditorUpdater.getNotifier().registerEditorUpdateListener(object : EditorUpdater.EditorUpdateListener {
            override fun onEditorUpdate(data: EditorData) {
                projectDuration = 0.0
                for (asset in getAssets()) {
                    projectDuration += asset.duration
                }
            }
        }, true)
    }

    fun loadProject(updater: EditorUpdater) {
        project.load(updater)
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

    fun addAssetAt(asset: Asset, pos: Double) {
        var index = project.findAssetIndex(pos)
        if (index == -1) {
            index = project.getAssets().lastIndex
        }
        project.addAsset(asset, index + 1)
    }

    fun deleteAsset(id: Long) {
        project.deleteAsset(id)
    }
}
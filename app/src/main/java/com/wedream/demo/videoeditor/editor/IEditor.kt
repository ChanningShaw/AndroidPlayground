package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.asset.Asset

interface IEditor {
    fun loadProject(updater: EditorUpdater)

    fun getAssets(): List<Asset>

    fun getAsset(id: Long): Asset?

    fun getAssetByTime(time: Double): Asset?

    fun getProjectDuration(): Double

    fun addAssetAt(asset: Asset, pos: Double)

    fun deleteAsset(id: Long)
}
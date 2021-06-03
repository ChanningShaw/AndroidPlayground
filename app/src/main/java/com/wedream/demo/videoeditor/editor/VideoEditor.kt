package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.Asset

class VideoEditor {

    private var assets = arrayListOf<Asset>()

    init {
        loadProject()
    }

    private fun loadProject() {
        var offset = 0.0
        val duration = 5.0
        for (i in 0..100) {
            assets.add(Asset(i, offset, offset + duration))
            offset += duration
        }
    }

    fun getAssets(): List<Asset> {
        return assets
    }
}
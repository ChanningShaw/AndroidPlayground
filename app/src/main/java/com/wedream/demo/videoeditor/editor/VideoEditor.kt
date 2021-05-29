package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.Asset

class VideoEditor {
    fun loadProject(): List<Asset> {
        var offset = 0.0
        val duration = 5.0
        val assets = arrayListOf<Asset>()
        for (i in 0..100) {
            assets.add(Asset(i, offset, offset + duration))
            offset += duration
        }
        return assets
    }
}
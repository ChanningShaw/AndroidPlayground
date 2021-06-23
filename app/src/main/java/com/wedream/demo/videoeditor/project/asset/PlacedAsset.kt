package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType

class PlacedAsset(
    id: Long,
    assetType: AssetType,
    fixDuration: Double,
    private var start: Double,
    private var end: Double
) : Asset(id, assetType, fixDuration) {

    fun getStart(): Double {
        return start
    }

    fun getEnd() : Double {
        return end
    }

    fun setStart(start: Double) {
        this.start = start
    }

    fun setEnd(end: Double) {
        this.end = end
    }

    fun setDuration(start: Double, end: Double) {
        this.start = start
        this.end = end
    }
}
package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType

class PlacedAsset(
    id: Long,
    assetType: AssetType,
    fixDuration: Double,
    var start: Double,
    var end: Double
) : Asset(id, assetType, fixDuration) {
    override val duration
        get() = end - start
}
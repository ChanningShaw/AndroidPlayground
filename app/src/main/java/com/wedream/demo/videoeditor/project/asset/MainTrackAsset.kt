package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.project.asset.operation.SpeedImpl

class MainTrackAsset(
    id: Long,
    private var path: String,
    assetType: AssetType,
    fixDuration: Double,
) : Asset(id, assetType, fixDuration), ISpeed by SpeedImpl() {

}
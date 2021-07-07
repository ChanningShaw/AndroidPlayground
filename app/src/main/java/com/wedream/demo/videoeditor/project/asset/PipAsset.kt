package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.project.asset.operation.SpeedImpl

class PipAsset(
    id: Long,
    private var path: String,
    assetType: AssetType,
    fixDuration: Double,
    start: Double, end: Double
) : PlacedAsset(id, assetType, fixDuration, start, end), ISpeed by SpeedImpl(){

}
package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.project.asset.operation.SpeedImpl

class PipAsset(
    id: Long,
    private var path: String,
    fixDuration: Double,
    start: Double, end: Double
) : PlacedAsset(id, AssetType.Video, fixDuration, start, end), ISpeed {

    override val duration: Double
        get() = super.duration / getSpeed()

    private val speedImpl = SpeedImpl(this)

    override fun setSpeed(speed: Double) {
        speedImpl.setSpeed(speed)
    }

    override fun getSpeed(): Double {
        return speedImpl.getSpeed()
    }
}
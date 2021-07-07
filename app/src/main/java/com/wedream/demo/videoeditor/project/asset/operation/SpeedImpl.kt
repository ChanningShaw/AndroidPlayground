package com.wedream.demo.videoeditor.project.asset.operation

import com.wedream.demo.videoeditor.project.asset.Asset

class SpeedImpl(private val asset: Asset) : ISpeed {

    private var speed = 1.0

    override fun setSpeed(speed: Double) {
        this.speed = speed
        asset.markDirty()
    }

    override fun getSpeed(): Double {
        return speed
    }
}
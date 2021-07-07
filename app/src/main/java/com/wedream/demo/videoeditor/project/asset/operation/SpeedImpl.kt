package com.wedream.demo.videoeditor.project.asset.operation

class SpeedImpl : ISpeed {

    private var speed = 1.0

    override fun setSpeed(speed: Double) {
        this.speed = speed
    }

    override fun getSpeed(): Double {
        return speed
    }
}
package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.videoeditor.project.AssetType

open class Asset(
    val id: Long,
    val type: AssetType,
    val fixDuration: Double
) {
    open val duration
        get() = fixDuration
}
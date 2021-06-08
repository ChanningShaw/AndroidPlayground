package com.wedream.demo.videoeditor.project

class Asset(
    val id: Long,
    val type: AssetType,
    var start: Double,
    var end: Double
) {
    val duration
        get() = end - start
}
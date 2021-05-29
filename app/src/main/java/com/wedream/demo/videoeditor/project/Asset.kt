package com.wedream.demo.videoeditor.project

class Asset(
    val id: Int,
    val start: Double,
    val end: Double
) {
    val duration
        get() = end - start
}
package com.wedream.demo.view.multitrack

/**
 * 一种绑定于其他ElementData的ElementData
 */
open class AttachElementData(id: Long,
                        left: Int,
                        width: Int,
                        trackLevel: Int)
    : TrackElementData(id, left, width, trackLevel) {

    var targetId = -1L
}
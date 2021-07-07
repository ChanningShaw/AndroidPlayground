package com.wedream.demo.videoeditor.editor.action

import com.wedream.demo.videoeditor.project.AssetType

sealed class Action {
    class AddAssetAction(
        val pos: Double,
        val duration: Double,
        val assetType: AssetType
    ) : Action()

    class DeleteAssetAction(
        val id: Long
    ) : Action()

    /**
     * @param pos 分割的位置（相对素材的时间）
     */
    class SplitAssetAction(val id: Long, val pos: Double) : Action()

    class CopyAssetAction(val id: Long) : Action()

    class SpeedAction(val id: Long, val speed: Double) : Action()
}
package com.wedream.demo.videoeditor.editor.action

import com.wedream.demo.videoeditor.project.AssetType

sealed class Action {
    class AddAssetAction(
        val pos: Double,
        val duration: Double,
        val assetType: AssetType
    ) : Action()
}
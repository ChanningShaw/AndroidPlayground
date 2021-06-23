package com.wedream.demo.videoeditor.project

import com.wedream.demo.videoeditor.project.asset.Asset

interface ProjectModifyListener {

    fun notifyItemAdded(asset: Asset)

    fun notifyItemDeleted(asset: Asset)

    fun notifyItemModified(asset: Asset)
}
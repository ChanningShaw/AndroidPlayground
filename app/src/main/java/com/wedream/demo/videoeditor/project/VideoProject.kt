package com.wedream.demo.videoeditor.project

import com.wedream.demo.util.IdUtils
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.util.ToastUtils
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.editor.EditorData
import com.wedream.demo.videoeditor.editor.EditorUpdater
import com.wedream.demo.videoeditor.project.asset.MainTrackAsset
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject

class VideoProject : ProjectModifyListener {
    // 主轨素材，有序
    private var assets = arrayListOf<Asset>()
    private var assetMap = hashMapOf<Long, Asset>()

    private var updater: EditorUpdater? = null

    fun load(updater: EditorUpdater) {
        this.updater = updater
        initProject()
    }

    override fun notifyItemAdded(asset: Asset) {
        updater?.notifyItemAdded(asset)
    }

    override fun notifyItemDeleted(asset: Asset) {
        updater?.notifyItemDeleted(asset)
    }

    override fun notifyItemModified(asset: Asset) {
        updater?.notifyItemModified(asset)
    }

    private fun initProject() {
        var offset = 0.0
        val duration = 5.0
        for (i in 0..20) {
            val id = IdUtils.nextId()
            addAsset(MainTrackAsset(id, "", AssetType.Video, duration))
            offset += duration
        }
    }

    fun getAssets(): List<Asset> {
        return assets
    }

    fun getAsset(id: Long): Asset? {
        return assetMap[id]
    }

    fun getAssetByTime(time: Double): Asset? {
        var start = 0.0
        var end = 0.0
        for (asset in assets) {
            end += asset.duration
            if (time in start..end) {
                return asset
            }
            start += asset.duration
        }
        return null
    }

    fun addAsset(asset: Asset) {
        addAsset(asset, assets.size)
    }

    fun addAsset(asset: Asset, index: Int) {
        assetMap[asset.id] = asset
        assets.add(index, asset)
        asset.setModifyListener(this)
        notifyItemAdded(asset)
    }

    fun deleteAsset(id: Long) {
        if (assets.size <= 1) {
            ToastUtils.showToast("视频只剩下一个片段，不能再删除")
            return
        }
        val iterator = assets.iterator()
        var asset: Asset? = null
        while (iterator.hasNext()) {
            asset = iterator.next()
            if (asset.id == id) {
                iterator.remove()
                break
            }
        }
        assetMap.remove(id)
        asset?.let {
            it.removeModifyListener()
            notifyItemDeleted(it)
        }
    }

    fun findAssetIndex(pos: Double): Int {
        var start = 0.0
        var end = 0.0
        for ((i, asset) in getAssets().withIndex()) {
            end += asset.duration
            if (pos in start..end) {
                return i
            }
            start += asset.duration
        }
        return -1
    }
}
package com.wedream.demo.videoeditor.project

import android.view.Choreographer
import com.wedream.demo.util.IdUtils
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.util.ToastUtils
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.editor.EditorData
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject

class VideoProject : ProjectModifyListener {
    // 主轨素材，有序
    private var assets = arrayListOf<Asset>()
    private var assetMap = hashMapOf<Long, Asset>()

    private var _projectChanged = PublishSubject.create<EditorData>()
    private var projectChanged = _projectChanged.toFlowable(BackpressureStrategy.LATEST)

    private var changedMap = hashMapOf<Asset, ActionType>()
    private var projectDuration = 0.0

    private var frameCallback = Choreographer.FrameCallback {
        notifyProjectChanged()
    }

    fun load() {
        initProject()
    }

    private fun notifyProjectChanged() {
        projectDuration = 0.0
        for (asset in assets) {
            projectDuration += asset.duration
        }
        val timelineData = EditorData()
        for (e in changedMap) {
            if (e.key.type == AssetType.Video) {
                timelineData.mainTrackModified = true
            }
            timelineData.events.add(ActionEvent(e.key.id, e.value))
        }
        changedMap.clear()
        _projectChanged.onNext(timelineData)
    }

    override fun notifyItemAdded(asset: Asset) {
        changedMap[asset] = ActionType.Add
        resetFrameCallback()
    }

    override fun notifyItemDeleted(asset: Asset) {
        changedMap[asset] = ActionType.Delete
        resetFrameCallback()
    }

    override fun notifyItemModified(asset: Asset) {
        changedMap[asset] = ActionType.Modify
        resetFrameCallback()
    }

    private fun resetFrameCallback() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    fun onProjectChange(block: (editorData: EditorData) -> Unit) {
        val d = projectChanged.subscribe({
            block.invoke(it)
        }, {
            it.printAndDie()
        })
    }

    fun getProjectDuration(): Double {
        return projectDuration
    }

    private fun initProject() {
        var offset = 0.0
        val duration = 5.0
        for (i in 0..5) {
            val id = IdUtils.nextId()
            addAsset(Asset(id, AssetType.Video, duration))
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
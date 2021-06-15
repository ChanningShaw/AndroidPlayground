package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.videoeditor.contains
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject

class VideoEditor {

    // 主轨素材，有序
    private var assets = arrayListOf<Asset>()
    private var assetMap = hashMapOf<Long, Asset>()
    lateinit var timelineViewModel: TimelineViewModel
    private var _projectChanged = PublishSubject.create<Int>()

    init {
        loadProject()
    }

    private var projectChanged = _projectChanged.toFlowable(BackpressureStrategy.LATEST)

    private fun loadProject() {
        var offset = 0.0
        val duration = 5.0
        for (i in 0..5) {
            val id = IdUtils.nextId()
            assets.add(Asset(id, AssetType.Video, duration))
            offset += duration
        }
    }

    private fun notifyProjectChanged() {
        _projectChanged.onNext(0)
    }

    fun onProjectChange(block: () -> Unit) {
        val d = projectChanged.subscribe({
            block.invoke()
        }, {
            it.printAndDie()
        })
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

    fun handleAction(action: Action) {
        when (action) {
            is Action.AddAssetAction -> {
                var start = 0.0
                var end = 0.0
                for ((i, asset) in assets.withIndex()) {
                    end += asset.duration
                    if (action.pos in start..end) {
                        val id = IdUtils.nextId()
                        val newAsset =
                            Asset(
                                id,
                                action.assetType,
                                action.duration
                            )
                        assetMap[id] = newAsset
                        assets.add(i + 1, newAsset)
                        break
                    }
                    start += asset.duration
                }
                notifyProjectChanged()
            }
            is Action.DeleteAssetAction -> {
                val iterator = assets.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().id == action.id) {
                        iterator.remove()
                        break
                    }
                }
                assetMap.remove(action.id)
                notifyProjectChanged()
            }
        }
    }
}
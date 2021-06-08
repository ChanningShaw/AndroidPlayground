package com.wedream.demo.videoeditor.editor

import com.wedream.demo.util.IdUtils
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.Asset
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject

class VideoEditor {

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
            assets.add(Asset(id, AssetType.Video, offset, offset + duration))
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

    fun handleAction(action: Action) {
        when (action) {
            is Action.AddAssetAction -> {
                val duration = action.duration
                var index = assets.lastIndex
                for ((i, asset) in assets.withIndex()) {
                    if (action.pos >= asset.start && action.pos <= asset.end) {
                        index = i + 1
                        val newAsset =
                            Asset(IdUtils.nextId(), action.assetType, asset.end, asset.end + action.duration)
                        assets.add(index, newAsset)
                        break
                    }
                }
                for (i in index + 1 until assets.size) {
                    assets[i].start += duration
                    assets[i].end += duration
                }
                notifyProjectChanged()
            }
        }
    }
}
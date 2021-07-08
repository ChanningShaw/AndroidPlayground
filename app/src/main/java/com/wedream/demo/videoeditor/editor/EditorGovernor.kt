package com.wedream.demo.videoeditor.editor

import android.view.Choreographer
import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.asset.Asset
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class EditorGovernor {

    private val videoEditor = VideoEditor()
    private var timelineViewModel: TimelineViewModel? = null

    fun loadProject(updater: EditorUpdater) {
        videoEditor.loadProject(updater)
    }

    fun setTimelineViewModel(model: TimelineViewModel) {
        timelineViewModel = model
    }

    fun getCurrentTime(): Double {
        return timelineViewModel?.getCurrentTime() ?: 0.0
    }

    fun getProjectDuration(): Double {
        return videoEditor.getProjectDuration()
    }

    fun getAssetByTime(time: Double): Asset? {
        return videoEditor.getAssetByTime(time)
    }

    fun getAsset(id: Long): Asset? {
        return videoEditor.getAsset(id)
    }

    fun getAssets(): List<Asset> {
        return videoEditor.getAssets()
    }

    fun post(runnable: Runnable) {
        EditorUpdater.getNotifier().postAction(runnable)
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.AddAssetAction -> {
                val id = IdUtils.nextId()
                val newAsset =
                    Asset(
                        id,
                        action.assetType,
                        action.duration
                    )
                videoEditor.addAssetAt(newAsset, action.pos)
            }
            is Action.DeleteAssetAction -> {
                videoEditor.deleteAsset(action.id)
            }
            is Action.SplitAssetAction -> {
                val originAsset = videoEditor.getAsset(action.id) ?: return
                val asset = Asset(
                    IdUtils.nextId(),
                    originAsset.type,
                    originAsset.fixDuration,
                    action.pos,
                    originAsset.getClipEnd()
                )
                videoEditor.addAssetAt(asset, getCurrentTime())
                originAsset.setClipEnd(action.pos)
            }
            is Action.CopyAssetAction -> {
                val originAsset = videoEditor.getAsset(action.id) ?: return
                val newAsset = originAsset.cloneObject()
                videoEditor.addAssetAt(newAsset, getCurrentTime())
            }
            is Action.SpeedAction -> {
                val originAsset = videoEditor.getAsset(action.id) as? ISpeed ?: return
                originAsset.setSpeed(action.speed)
                post {
                    playAsset(action.id)
                }
            }
            else -> {

            }
        }
    }

    fun playAsset(id: Long) {
        timelineViewModel?.getSegment(id)?.let {
            play(it.left, it.right)
        }
    }

    fun play(from: Int, to: Int) {
        var current = from
        var callback: Choreographer.FrameCallback? = null
        callback= Choreographer.FrameCallback {
            if (current < to) {
                current += 5
                MessageChannel.sendMessage(TimeLineMessageHelper.packTimelineSeekToMsg(current))
                Choreographer.getInstance().postFrameCallback(callback)
            } else {
                MessageChannel.sendMessage(TimeLineMessageHelper.packTimelineSeekToMsg(to))
            }
        }
        Choreographer.getInstance().postFrameCallback(callback)
    }
}
package com.wedream.demo.videoeditor.editor

import android.view.Choreographer
import com.wedream.demo.videoeditor.project.ActionEvent
import com.wedream.demo.videoeditor.project.ActionType
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.asset.Asset

class EditorUpdater(private val editorState: EditorState) {

    private var assetChangedMap = hashMapOf<Asset, ActionType>()
    private var videoEditorListener: EditorUpdateListener? = null
    private var updateListeners = arrayListOf<EditorUpdateListener>()
    private val updateNotifier = EditorUpdateNotifier()

    private var frameCallback = Choreographer.FrameCallback {
        notifyEditorChanged()
    }

    companion object {
        private lateinit var notifier: EditorUpdateNotifier
        fun getNotifier(): EditorUpdateNotifier {
            return notifier
        }
    }

    init {
        notifier = updateNotifier
        editorState.attachEditorUpdater(this)
    }

    private fun notifyEditorChanged() {
        val editorData = EditorData()
        for (e in assetChangedMap) {
            if (e.key.type == AssetType.Video) {
                editorData.mainTrackModified = true
            }
            editorData.events.add(ActionEvent(e.key.id, e.value))
        }
        editorData.currentSelectedId = editorState.selectedSegmentId

        reset()

        // 1.先更新 videoEditor
        videoEditorListener?.onEditorUpdate(editorData)
        // 2.在更新 EditorState
        editorState.onEditorUpdate(editorData)
        // 3.最后更新其他listener
        for (l in updateListeners) {
            l.onEditorUpdate(editorData)
        }
    }
    fun reset(){
        assetChangedMap.clear()
    }

    fun notifyItemAdded(asset: Asset) {
        assetChangedMap[asset] = ActionType.Add
        resetFrameCallback()
    }

    fun notifyItemDeleted(asset: Asset) {
        assetChangedMap[asset] = ActionType.Delete
        resetFrameCallback()
    }

    fun notifyItemModified(asset: Asset) {
        assetChangedMap[asset] = ActionType.Modify
        resetFrameCallback()
    }

    fun notifyEditorStateChanged() {
        resetFrameCallback()
    }

    private fun resetFrameCallback() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    inner class EditorUpdateNotifier {
        fun registerEditorUpdateListener(listener: EditorUpdateListener, fromEditor: Boolean = false) {
            if (fromEditor) {
                updateListeners.add(listener)
            } else {
                videoEditorListener = listener
            }
        }

        fun removeEditorUpdateListener(listener: EditorUpdateListener) {
            updateListeners.remove(listener)
        }
    }

    interface EditorUpdateListener {
        fun onEditorUpdate(data: EditorData)
    }
}
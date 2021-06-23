package com.wedream.demo.videoeditor.menu

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.wedream.demo.util.ToastUtils
import com.wedream.demo.videoeditor.controller.Controller
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.editor.VideoEditor.Companion.MIN_ASSET_DURATION
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import kotlin.math.abs

class MenuController(private val videoEditor: VideoEditor) : Controller<MenuViewModel>() {

    private val menuList = listOf(
        MenuEntity("新增素材") {
            videoEditor.handleAction(
                Action.AddAssetAction(
                    videoEditor.timelineViewModel.getCurrentTime(),
                    5.0,
                    AssetType.Video
                )
            )
        },
        MenuEntity("删除素材") {
            val currentTime = videoEditor.timelineViewModel.getCurrentTime()
            videoEditor.getAssetByTime(currentTime)?.let {
                videoEditor.handleAction(
                    Action.DeleteAssetAction(it.id)
                )
            }
        },
        MenuEntity("分割素材") {
            val currentSegment = videoEditor.timelineViewModel.getCurrentSegment() ?: return@MenuEntity
            val startTime = TimelineUtils.width2time(currentSegment.left, videoEditor.timelineViewModel.getScale())
            val endTime = TimelineUtils.width2time(currentSegment.right, videoEditor.timelineViewModel.getScale())
            val currentTime = videoEditor.getCurrentTime()
            if (abs(currentTime - startTime) < MIN_ASSET_DURATION || abs(currentTime - endTime) < MIN_ASSET_DURATION) {
                ToastUtils.showToast("当前位置素材时长太短，不支持分割")
                return@MenuEntity
            }
            videoEditor.handleAction(
                Action.SplitAssetAction(currentSegment.id, currentTime - startTime)
            )
        }
    )

    override fun onBind() {
        val rootView = getRootView() as ViewGroup
        for (m in menuList) {
            val button = Button(getActivity())
            button.text = m.name
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            button.setOnClickListener {
                m.clickAction.invoke()
            }
            rootView.addView(button, params)
        }
    }
}
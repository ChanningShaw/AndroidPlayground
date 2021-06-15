package com.wedream.demo.videoeditor.menu

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.wedream.demo.videoeditor.controller.Controller
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.AssetType

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
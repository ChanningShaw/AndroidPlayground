package com.wedream.demo.videoeditor.menu

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.wedream.demo.inject.Inject
import com.wedream.demo.util.ToastUtils
import com.wedream.demo.videoeditor.const.Constants.MIN_ASSET_DURATION
import com.wedream.demo.videoeditor.controller.ViewController
import com.wedream.demo.videoeditor.dialog.base.EditorDialog
import com.wedream.demo.videoeditor.dialog.base.EditorDialogType
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import kotlin.math.abs

class MenuController : ViewController() {

    @Inject
    lateinit var editorGovernor: EditorGovernor

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    private val menuList = listOf(
        MenuEntity("新增") {
            editorGovernor.handleAction(
                Action.AddAssetAction(
                    editorGovernor.getCurrentTime(),
                    5.0,
                    AssetType.Video
                )
            )
        },
        MenuEntity("删除") {
            val currentTime = editorGovernor.getCurrentTime()
            editorGovernor.getAssetByTime(currentTime)?.let {
                editorGovernor.handleAction(
                    Action.DeleteAssetAction(it.id)
                )
            }
        },
        MenuEntity("分割") {
            val currentSegment = timelineViewModel.getCurrentSegment() ?: return@MenuEntity
            val startTime = TimelineUtils.width2time(currentSegment.left, timelineViewModel.getScale())
            val endTime = TimelineUtils.width2time(currentSegment.right, timelineViewModel.getScale())
            val currentTime = timelineViewModel.getCurrentTime()
            if (abs(currentTime - startTime) < MIN_ASSET_DURATION || abs(currentTime - endTime) < MIN_ASSET_DURATION) {
                ToastUtils.showToast("当前位置素材时长太短，不支持分割")
                return@MenuEntity
            }
            editorGovernor.handleAction(
                Action.SplitAssetAction(currentSegment.id, currentTime - startTime)
            )
        },
        MenuEntity("复制") {
            val currentSegment = timelineViewModel.getCurrentSegment() ?: return@MenuEntity
            editorGovernor.handleAction(
                Action.CopyAssetAction(currentSegment.id)
            )
        },
        MenuEntity("变速") {
            val currentSegment = timelineViewModel.getCurrentSegment() ?: return@MenuEntity
            val objects = getObjectsContext().toMutableList()
            objects.add(currentSegment)
            EditorDialog.getEditorDialog(getActivity(), objects, EditorDialogType.Speed).show(getActivity())
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
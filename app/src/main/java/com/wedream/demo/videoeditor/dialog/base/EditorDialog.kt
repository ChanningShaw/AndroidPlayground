package com.wedream.demo.videoeditor.dialog.base

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.ViewAnimatorHelper
import com.wedream.demo.videoeditor.controller.ViewController
import com.wedream.demo.videoeditor.dialog.base.EditorDialogFactory.getEditorDialogModel

class EditorDialog private constructor(
    private val context: Context,
    private val callerContext: MutableList<Any>?,
    private var dialogModel: EditorDialogModel,
    private val type: EditorDialogType = EditorDialogType.Speed, // 弹窗类型
) {

    private var contentView: View? = null
    private var decorView: ViewGroup? = null
    private var editorDialogView: FrameLayout? = null

    companion object {
        private const val TAG = "EditorDialog"

        fun getEditorDialog(
            context: Context,
            callerContext: MutableList<Any>?,
            type: EditorDialogType = EditorDialogType.Speed,
        ): EditorDialog {
            val editorDialogModel = getEditorDialogModel(type)
            return EditorDialog(context, callerContext, editorDialogModel, type)
        }
    }

    fun show(activity: AppCompatActivity) {
        decorView = activity.window.decorView as ViewGroup
        initDialogView()
        editorDialogView?.let {
            dialogModel.controller.create(it)
        }
        // 将自己注入
        callerContext?.add(this)
        dialogModel.controller.bind(callerContext)
        ViewAnimatorHelper.popWindowAnimation(
            editorDialogView,
            editorDialogView,
            decorView,
            true,
        )
    }

    fun dismiss() {
        dialogModel.controller.destroy()
        ViewAnimatorHelper.popWindowAnimation(editorDialogView, editorDialogView, decorView, false)
    }

    private fun initDialogView() {
        editorDialogView = FrameLayout(context).apply {
            val params =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.apply {
                gravity = Gravity.BOTTOM
            }
            layoutParams = params
        }
        contentView = LayoutInflater.from(context).inflate(dialogModel.layout_id, editorDialogView, true)
        decorView?.removeView(editorDialogView)
        decorView?.addView(editorDialogView)
    }
}

data class EditorDialogModel(val layout_id: Int, val controller: ViewController)
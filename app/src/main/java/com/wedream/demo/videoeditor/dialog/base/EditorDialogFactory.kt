package com.wedream.demo.videoeditor.dialog.base

import com.wedream.demo.R
import com.wedream.demo.videoeditor.dialog.controller.SpeedDialogController

object EditorDialogFactory {
    fun getEditorDialogModel(type: EditorDialogType): EditorDialogModel {
        return when (type) {
            EditorDialogType.Speed -> {
                EditorDialogModel(R.layout.dialog_speed, SpeedDialogController())
            }
        }
    }
}
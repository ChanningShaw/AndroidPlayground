package com.wedream.demo.videoeditor.dialog.controller

import com.wedream.demo.inject.Inject
import com.wedream.demo.videoeditor.controller.ViewController
import com.wedream.demo.videoeditor.dialog.base.EditorDialog

open class DialogController : ViewController() {
    @Inject
    lateinit var dialog: EditorDialog
}
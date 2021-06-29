package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.project.ActionEvent

class EditorData {
    var mainTrackModified = false

    var currentSelectedId = -1L
    var events = arrayListOf<ActionEvent>()
}
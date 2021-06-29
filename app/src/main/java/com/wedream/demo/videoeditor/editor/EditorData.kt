package com.wedream.demo.videoeditor.editor

import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.project.ActionEvent

class EditorData {
    var mainTrackModified = false

    var currentSelectedId = Constants.INVALID_ID
    var events = arrayListOf<ActionEvent>()
}
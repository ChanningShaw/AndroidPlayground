package com.wedream.demo.videoeditor.project

enum class ActionType {
    Add, Delete, Modify
}

class ActionEvent(val id: Long, val actionType: ActionType)
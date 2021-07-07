package com.wedream.demo.videoeditor.dialog.controller

import android.view.View
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log

class SpeedDialogController : DialogController() {

    lateinit var confirmBtn: View

    override fun onBind() {
        initViews()
    }
    private fun initViews() {
        confirmBtn = findViewById(R.id.confirm_bt)
        confirmBtn.setOnClickListener {
            dialog.dismiss()
        }
    }
}
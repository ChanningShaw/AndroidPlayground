package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.app.track.setParentTrackNode

class SwitchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch)
        findViewById<View>(R.id.root).setParentTrackNode(this)
    }
}
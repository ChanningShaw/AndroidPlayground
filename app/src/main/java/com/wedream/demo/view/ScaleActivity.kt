package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class ScaleActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        findViewById<View>(R.id.view).setOnClickListener {
            it.scaleX *= 1.1f
        }
        findViewById<FrameLayout>(R.id.container).clipChildren = false
    }
}
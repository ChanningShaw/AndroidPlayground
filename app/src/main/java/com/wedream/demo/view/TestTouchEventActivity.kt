package com.wedream.demo.view

import android.os.Bundle
import android.util.Log
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class TestTouchEventActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_event)
        findViewById<View>(R.id.view)?.let {
            it.setOnClickListener {
                Log.e("xcm", "onCreate: ", )
            }
        }
    }
}
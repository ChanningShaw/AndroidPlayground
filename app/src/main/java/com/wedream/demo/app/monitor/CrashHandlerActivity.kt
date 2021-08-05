package com.wedream.demo.app.monitor

import android.os.Bundle
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import java.lang.RuntimeException


class CrashHandlerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_handler)
        findViewById<View>(R.id.bt1).setOnClickListener {
            throw RuntimeException("test")
        }
    }
}
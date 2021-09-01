package com.wedream.demo.app.monitor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import java.lang.RuntimeException


class ANRHandleActivity : BaseActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anr_handle)
        findViewById<View>(R.id.bt1).setOnTouchListener { v, event ->
            Thread.sleep(10 * 1000L)
            true
        }
        // 注意，在点击事件里sleep不会触发anr
//        findViewById<View>(R.id.bt1).setOnClickListener {
//            Thread.sleep(10 * 1000L)
//        }
    }
}
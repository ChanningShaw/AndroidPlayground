package com.wedream.demo.touch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log

/**
 * 列表中的子view已经消费了事件，为什么列表还能滑动？
 *
 * 按住子view然后滑动列表，子view事件被拦截，收到cancel事件
 *
 */
class EventDispatchActivity : BaseActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_dispatch)
        findViewById<View>(R.id.view_blue)?.setOnTouchListener { v, event ->
            log { "action = ${event.actionMasked}" }
            true
        }
    }
}
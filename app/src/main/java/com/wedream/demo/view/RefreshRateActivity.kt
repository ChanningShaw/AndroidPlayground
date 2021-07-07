package com.wedream.demo.view

import android.os.Bundle
import android.view.MotionEvent
import com.wedream.demo.R
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.util.LogUtils.log


class RefreshRateActivity : DisposableActivity() {

    var count = 0
    var downTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_rate)
        val display = windowManager.defaultDisplay
        val refreshRate = display.refreshRate
        log { "refreshRate = $refreshRate" }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            count = 0
            downTime = System.currentTimeMillis()
        } else if (event.actionMasked == MotionEvent.ACTION_MOVE) {
            count++
        } else if (event.actionMasked == MotionEvent.ACTION_UP) {
            val currentTime = System.currentTimeMillis()
            log { "event rate = ${count / (currentTime - downTime).toFloat() * 1000f}" }
        }
        return super.dispatchTouchEvent(event)
    }
}
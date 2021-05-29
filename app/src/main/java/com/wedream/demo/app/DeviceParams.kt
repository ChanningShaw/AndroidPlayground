package com.wedream.demo.app

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * 用于存放设备相关的参数
 */
object DeviceParams {
    var SCREEN_WIDTH = 0

    fun init(application: MyApplication) {
        val wm = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        SCREEN_WIDTH = dm.widthPixels // 屏幕宽度（像素）
    }
}
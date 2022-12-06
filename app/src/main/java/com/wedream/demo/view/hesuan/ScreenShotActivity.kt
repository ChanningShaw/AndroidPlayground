package com.example.myapplication

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.wedream.demo.R
import java.util.Calendar

/**
 * Created by xiaochunming.1 on 2022/11/26
 * @author xiaochunming.1@bytedance.com
 */
class ScreenShotActivity : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_shot)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val date = c.get(Calendar.DATE)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val monthStr = if (month < 10) "0$month" else "$month"
        val hourStr = if (hour < 10) "0$hour" else "$hour"
        val minuteStr = if (minute < 10) "0$minute" else "$minute"

        findViewById<TextView>(R.id.status_bar_date)?.let {
            val text = if (hour < 10) {
                "上午0${hour}:${minuteStr}"
            } else if (hour < 12) {
                "上午${hour}:${minuteStr}"
            } else if (hour < 22) {
                "下午0${hour - 12}:${minuteStr}"
            } else {
                "下午${hour - 12}:${minuteStr}"
            }
            it.text = text
            it.translationX = 40f
            it.translationY = 28f
        }
        findViewById<TextView>(R.id.cur_time)?.let {
            val text = "$year-$monthStr-$date $hourStr:$minuteStr"
            it.text = text
            it.translationX = 450f
            it.translationY = 655f
        }
        findViewById<TextView>(R.id.detect_time)?.let {
            val text = "$year-$monthStr-$date 04:54"
            it.text = text
            it.translationX = 180f
            it.translationY = 1490f
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemUI()
        // 延伸显示区域到刘海
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes;
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
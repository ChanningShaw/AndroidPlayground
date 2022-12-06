package com.wedream.demo.view.hesuan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ScreenShotActivity
import com.wedream.demo.R
import java.util.Calendar


class HesuanActivity : AppCompatActivity() {

    private var wm: WindowManager? = null
    private val layoutParams = WindowManager.LayoutParams()
    private var floatView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hesuan)
        findViewById<Button>(R.id.screen_shot).setOnClickListener {
            startActivity(Intent(this, ScreenShotActivity::class.java))
        }
        findViewById<Button>(R.id.overlay_company).setOnClickListener {
            showFloatWindowForBytedance()
        }
        findViewById<Button>(R.id.overlay_yuekangma).setOnClickListener {
            showFloatWindowForYuekangma()
        }
    }

    override fun onResume() {
        super.onResume()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        setupLayoutParams()
    }

    private fun setupLayoutParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.gravity = Gravity.LEFT or Gravity.CENTER
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.format = PixelFormat.RGBA_8888
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showFloatWindowForBytedance() {
        if (floatView != null) {
            wm?.removeView(floatView)
            floatView = null
            return
        }
        floatView = LayoutInflater.from(this).inflate(R.layout.hesuan_bytedance, null)
        layoutParams.width = 425
        layoutParams.height = 280
        layoutParams.x = 113
        layoutParams.y = 175
        wm?.addView(floatView, layoutParams)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val date = c.get(Calendar.DATE)
        floatView?.findViewById<TextView>(R.id.time)?.let {
            it.text = "$year-$month-$date 04:54"
        }
        floatView?.setOnTouchListener(FloatingOnTouchListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showFloatWindowForYuekangma() {
        if (floatView != null) {
            wm?.removeView(floatView)
            floatView = null
            return
        }
        floatView = LayoutInflater.from(this).inflate(R.layout.hesuan_yuekangma, null)
        layoutParams.width = 480
        layoutParams.height = 320
        layoutParams.x = 113
        layoutParams.y = 175
        wm?.addView(floatView, layoutParams)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val date = c.get(Calendar.DATE)
        floatView?.findViewById<TextView>(R.id.time)?.let {
            it.text = "$year-$month-$date 04:54"
        }
        floatView?.setOnTouchListener(FloatingOnTouchListener())
    }

    inner class FloatingOnTouchListener : View.OnTouchListener {
        private var x = 0
        private var y = 0
        private var downTime: Long = 0
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downTime = System.currentTimeMillis()
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }

                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.x += movedX
                    layoutParams.y += movedY
                    wm?.updateViewLayout(floatView, layoutParams)
                }

            }
            return false
        }
    }
}


package com.wedream.demo.render

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log

class SubThreadWindowActivity : BaseActivity() {

    private var subWindow: View? = null
    private lateinit var subHandler: Handler
    val thread = HandlerThread("sub-thread")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = TextView(this)
        view.text = "我是主窗口"
        view.setTextColor(Color.WHITE)
        view.setBackgroundResource(R.color.color_blue)
        setContentView(view)
        subWindow = addSubWindow()
        view.setOnClickListener {
            // 这里会crash
//            subWindow?.text = "2"
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addSubWindow(): View {
        thread.start()
        subHandler = Handler(thread.looper)
        val subWindow = FrameLayout(this)
        subHandler.post {
            subWindow.setBackgroundResource(R.color.color_yellow)
            val textView = TextView(this)

            kotlin.run {
                textView.text = "In SubWindow"
                textView.setBackgroundResource(R.color.color_green)
                textView.setTextColor(Color.WHITE)
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                textView.gravity = Gravity.CENTER
                subWindow.addView(textView, params)
            }

            val params = WindowManager.LayoutParams()
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION
            params.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            params.title = "subWindow"

            params.format = PixelFormat.TRANSPARENT
            params.gravity = Gravity.TOP or Gravity.LEFT
            params.x = 0
            params.y = 100
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = 300
            windowManager.addView(subWindow, params)
            textView.setOnTouchListener { v, event ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        (textView.layoutParams as? FrameLayout.LayoutParams)?.let {
                            it.width = 200
                            textView.layoutParams = it
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        textView.translationX = event.rawX
                        log { "translationX = ${textView.translationX}" }
                        textView.invalidate()
                    }
                }
                true
            }
        }
        return subWindow
    }

    override fun onDestroy() {
        subHandler.post {
            windowManager.removeViewImmediate(subWindow)
        }
        thread.quitSafely()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
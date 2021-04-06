package com.wedream.demo.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log


class ScaleActivity : AppCompatActivity() {

    private lateinit var scaleView: ScaleView
    private lateinit var scrollView: HorizontalScrollView
    private lateinit var contentView: FrameLayout
    private var screenWidth = 0
    private var currentScale = 1.0f
    private var laseScale = 1.0f
    private var scalingStartScrollX = 0
    private var increase = false
    private var changed = false

    private val rectList = mutableListOf<Pair<Rect, View>>()

    private val choreographer = Choreographer.getInstance()

    private val frameCallback = Choreographer.FrameCallback {
        if (!changed) {
            return@FrameCallback
        }
        changed = false
        layoutViews(currentScale)
        val pos = scalingStartScrollX * currentScale
        log { "pos = $pos" }
        scrollView.post {
            scrollView.scrollTo(contentView.width, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels // 屏幕宽度（像素）

        setContentView(R.layout.activity_scale)
        scaleView = findViewById(R.id.scaleView)
        scrollView = findViewById(R.id.scrollView)
        contentView = findViewById(R.id.content_view)
        addViews()
        layoutViews(1.0f)
        scaleView.setOnScaleListener(object : ScaleView.OnScaleListener {
            override fun onScaling(scale: Float) {
                log { "scale = $scale" }
                currentScale = scale
                if (!changed) {
                    changed = true
                    choreographer.removeFrameCallback(frameCallback)
                    choreographer.postFrameCallback(frameCallback)
                }
                if (laseScale > currentScale) {
                    if (increase) {
                        log { "monotonicity change to down!" }
                    }
                    increase = false
                } else if (laseScale < currentScale) {
                    if (!increase) {
                        log { "monotonicity change to up!" }
                    }
                    increase = true
                }
                laseScale = currentScale
            }

            override fun onScaleStart(scale: Float) {
                scalingStartScrollX = scrollView.scrollX
                laseScale = scale
            }

            override fun onScaleEnd(scale: Float) {
            }
        })
        contentView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            log { "contentView right = ${right - screenWidth / 2}" }
        }
    }

    private fun addViews() {
        var offset = 0
        val width = 150
        for (i in 1..50) {
            val view = LayoutInflater.from(this).inflate(R.layout.text_simple, null, false) as TextView
            view.text = i.toString()
            when (i % 3) {
                0 -> {
                    view.setBackgroundResource(R.color.color_green)
                }
                1 -> {
                    view.setBackgroundResource(R.color.color_blue)
                }
                else -> {
                    view.setBackgroundResource(R.color.red_dot_color)
                }
            }
            val rect = Rect(offset, 0, offset + width, -2)
            rectList.add(Pair(rect, view))
            val params = FrameLayout.LayoutParams(rect.width(), rect.height())
            contentView.addView(view, params)
            offset += width
        }
    }

    private fun layoutViews(scale: Float) {
        var offset = 0
        for (i in rectList.indices) {
            val p = rectList[i]
            val rect = p.first
            val view = p.second
            val with = (rect.width() * scale).toInt()
            view.layoutParams.let {
                it as ViewGroup.MarginLayoutParams
                it.marginStart = offset
                if (i == rectList.size - 1) {
                    it.marginEnd = screenWidth / 2
                    log { "last right = ${offset + with}" }
                }
                it.width = with
                it.height = rect.height()
                view.layoutParams = it
            }
            offset += with
        }
    }
}
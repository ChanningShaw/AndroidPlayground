package com.wedream.demo.view

import android.annotation.SuppressLint
import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.render.WaveView
import com.wedream.demo.render.WaveViewActivity
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log

class OutlineActivity : AppCompatActivity() {

    lateinit var container: FrameLayout
    var containerWidth = AndroidUtils.dip2pix(200)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outline)
        container = findViewById(R.id.container)
        val waveView = findViewById<WaveView>(R.id.wave_view)
        waveView.layoutParams.width = containerWidth
        val viewOutlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRect(0, 0, containerWidth, view.height)
            }
        }
        waveView.outlineProvider = viewOutlineProvider
        waveView.clipToOutline = true
        container.setOnTouchListener { v, event ->
            log { "event.x = ${event.x}" }
            containerWidth = event.x.toInt()
            val params = container.layoutParams
            params.width = containerWidth
            container.layoutParams = params
            waveView.invalidateOutline()
            false
        }
        waveView.post {
            waveView.setData(WaveViewActivity.benchMark3)
        }
    }
}
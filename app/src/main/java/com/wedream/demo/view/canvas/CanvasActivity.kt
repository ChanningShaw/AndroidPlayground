package com.wedream.demo.view.canvas

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class CanvasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        findViewById<MyCanvasView>(R.id.canvas_view)?.let {
            val view = TextView(it.context)
            view.setBackgroundResource(R.color.color_green)
            view.text = "dddddddddddddddd"
            it.drawView(view, 0, 0, 300, 300)
        }
    }
}
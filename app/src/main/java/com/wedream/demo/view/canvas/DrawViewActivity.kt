package com.wedream.demo.view.canvas

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class DrawViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_view)
        findViewById<MyCanvasView>(R.id.canvas_view)?.let {
            val view = TextView(it.context)
            view.setBackgroundResource(R.color.color_green)
            view.text = "dddddddddddddddd"
            it.drawView(view, 0, 0, 300, 300)
        }
    }
}
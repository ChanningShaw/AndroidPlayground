package com.wedream.demo.view.canvas

import android.os.Bundle
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class DrawViewActivity : BaseActivity() {
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
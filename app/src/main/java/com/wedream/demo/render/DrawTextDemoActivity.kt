package com.wedream.demo.render

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class DrawTextDemoActivity : BaseActivity() {

    var demoView: DrawTextDemoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_text)
        demoView = findViewById(R.id.demo_view)
    }
}
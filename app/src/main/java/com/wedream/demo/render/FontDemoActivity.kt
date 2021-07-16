package com.wedream.demo.render

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity


class FontDemoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font)

        val am = assets
        val tf = Typeface.createFromAsset(am, "fonts/pacifico.ttf")
        findViewById<TextView>(R.id.tv_pacifico).typeface = tf
        findViewById<TextView>(R.id.tv_my_font).typeface = Typeface.createFromAsset(am, "fonts/MyFont-Regular.ttf")
    }
}
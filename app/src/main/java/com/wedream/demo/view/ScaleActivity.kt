package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class ScaleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        findViewById<View>(R.id.view).setOnClickListener {
            it.scaleX *= 1.1f
        }
    }
}
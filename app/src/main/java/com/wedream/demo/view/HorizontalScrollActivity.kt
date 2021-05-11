package com.wedream.demo.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class HorizontalScrollActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll)
        findViewById<HorizontalScrollView>(R.id.scrollView).setOnTouchListener(ClickDetectTouchListener())
    }
}
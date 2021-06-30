package com.wedream.demo.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class HorizontalScrollActivity : AppCompatActivity() {

    private lateinit var scrollView :HorizontalScrollView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll)
        scrollView = findViewById<HorizontalScrollView>(R.id.scrollView)
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollX > 1000) {
                scrollView.scrollTo(1000, 0)
            }
        }
    }
}
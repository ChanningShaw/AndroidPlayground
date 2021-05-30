package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import kotlinx.android.synthetic.main.activity_scale.*

class TranslateActivity : AppCompatActivity() {

    private lateinit var view1: View
    private lateinit var view2: View
    private lateinit var view3: View
    private lateinit var scrollView: HorizontalScrollView
    private lateinit var container1: FrameLayout
    private lateinit var container2: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        view1 = findViewById(R.id.v1)
        view2 = findViewById(R.id.v2)
        view3 = findViewById(R.id.v3)
        scrollView = findViewById(R.id.scrollView)
        container1 = findViewById(R.id.container1)
        container2 = findViewById(R.id.container2)
        view2.translationX = 200f
        view3.translationX = 300f
        val params1 = container1.layoutParams as ViewGroup.MarginLayoutParams
        params1.width = 2000
        container1.layoutParams = params1
        val params2 = container2.layoutParams as ViewGroup.MarginLayoutParams
        params2.width = 2000
        container2.layoutParams = params2
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            view1.translationX = scrollX.toFloat()
        }
    }
}
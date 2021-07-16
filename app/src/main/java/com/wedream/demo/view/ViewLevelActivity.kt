package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity


class ViewLevelActivity : BaseActivity() {

    var frameLayout : FrameLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_level)
        val linearLayout = findViewById<LinearLayout>(R.id.ll)
        frameLayout = FrameLayout(this)
        frameLayout?.setBackgroundResource(R.color.color_blue)
        val params  = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 132)
        linearLayout.addView(frameLayout, params)
        params.marginStart = 200
    }

    override fun onResume() {
        super.onResume()
        val view = View(this)

        val params1 = FrameLayout.LayoutParams(100, 100)
        params1.marginStart = 200
        view.setBackgroundResource(R.color.color_green)
        frameLayout?.addView(view, params1)
    }
}
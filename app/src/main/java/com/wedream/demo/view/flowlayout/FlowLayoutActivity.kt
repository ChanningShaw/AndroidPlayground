package com.wedream.demo.view.flowlayout

import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.dp
import kotlin.math.pow


class FlowLayoutActivity : DisposableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flowLayout = FlowLayout(this)
        flowLayout.setPadding(10.dp, 10.dp, 10.dp, 10.dp)
        setContentView(flowLayout)
        repeat(20) {
            val textView = TextView(this)
            textView.text = it.toDouble().pow(10.0).toString()
            textView.setBackgroundResource(R.color.color_green)
            flowLayout.addView(textView)
        }
    }
}
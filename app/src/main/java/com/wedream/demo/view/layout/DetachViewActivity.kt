package com.wedream.demo.view.layout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.view.multitrack.SegmentView

class DetachViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detach_view)
        val view = findViewById<SegmentView>(R.id.view)
        val parent = findViewById<MyFrameLayout>(R.id.parent)
        val bt1 = findViewById<Button>(R.id.bt1)
        val bt2 = findViewById<Button>(R.id.bt2)
        val bt3 = findViewById<Button>(R.id.bt3)

        bt1.setOnClickListener {
            if (view.parent != null) {
                parent.detachView(view)
                bt1.text = "attach"
            } else {
                parent.attachView(view, 0, view.layoutParams as FrameLayout.LayoutParams)
                view.layout(view.left + 50, 0, view.left + 50 + view.width, view.height)
                bt1.text = "detach"
            }
        }
        bt2.setOnClickListener {
            parent.invalidate()
        }
        bt3.setOnClickListener {
            view.requestLayout()
        }
    }
}
package com.wedream.demo.view.multitrack

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.HorizontalScrollView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.AndroidUtils.dip2pix
import com.wedream.demo.util.LogUtils.log

class TrackActivity : BaseActivity() {

    private var trackContainer: PlaneRecycler? = null
    private var horizontalScrollView: HorizontalScrollView? = null
    private var leftView: View? = null
    private var rightView: View? = null
    private var screenWidth = 0
    private var adapter: MultiTrackAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        trackContainer = findViewById(R.id.track_container)
        horizontalScrollView = findViewById(R.id.horizontal_scrollview)
        leftView = findViewById(R.id.left_view)
        rightView = findViewById(R.id.right_view)
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels // 屏幕宽度（像素）

        trackContainer?.notifyHorizontalScroll(dip2pix(200), +screenWidth - dip2pix(200))
        trackContainer?.setEventListener(object : PlaneRecycler.EventListener {
            override fun onEmptyClick() {
                adapter?.clearSelect()
            }
        })


        val list = mutableListOf<ElementData>()
        for (i in 0..1) {
            var start = 200
            val length = 200
            val margin = 10
            var end = start + length
            for (j in 0..5) {
                list.add(ElementData((i * 10 + j + 100000).toLong(), start, end - start, i))
                start += (length + margin)
                end += (length + margin)
                log { "i = $i ,start = $start, end = $end" }
            }
        }
        adapter = MultiTrackAdapter(this)
        trackContainer?.setAdapter(adapter!!)
        trackContainer?.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val height = trackContainer?.getContentHeight() ?: 0
            log { "height = $height" }
            adapter?.setBounds(Rect(left, top, right, top + height))
        }
        adapter?.setData(list)

        horizontalScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val width = leftView?.width ?: 0
            trackContainer?.notifyHorizontalScroll(scrollX + width, scrollX + screenWidth - width)
        }

        trackContainer?.setOnClickListener {
            log { "trackContainer onClick" }
        }
    }
}
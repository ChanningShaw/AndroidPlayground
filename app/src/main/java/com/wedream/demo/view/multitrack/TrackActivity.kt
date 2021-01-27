package com.wedream.demo.view.multitrack

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.AndroidUtils.dip2pix
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.base.MultiTrackAdapter

class TrackActivity : AppCompatActivity() {

    private var trackContainer: SegmentRecycler? = null
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

        val list = mutableListOf<TrackElementData>()
        var start = 0
        val length = 200
        val margin = 10
        var end = start + length
        for (i in 0..1) {
            list.add(TrackElementData(i.toLong() + 100000, i, start, end))
            start += (length + margin)
            end += (length + margin)
        }
        adapter = MultiTrackAdapter(this)
        trackContainer?.setAdapter(adapter!!)
        adapter?.setData(list)

        horizontalScrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val width = leftView?.width ?: 0
            Log.e("xcm", "scrollX = $scrollX, scrollY = $scrollY, oldScrollX = $oldScrollX, oldScrollY = $oldScrollY")
            trackContainer?.notifyHorizontalScroll(scrollX + width, scrollX + screenWidth - width)
        }

        trackContainer?.setOnClickListener {
            log { "trackContainer onClick" }
        }
    }
}
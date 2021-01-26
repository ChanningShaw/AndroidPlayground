package com.wedream.demo.view.multitrack

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import com.wedream.demo.R
import com.wedream.demo.view.multitrack.base.AbsSegmentRecyclerAdapter
import com.wedream.demo.view.multitrack.base.ITrackContainer
import com.wedream.demo.view.multitrack.base.SegmentData

class SegmentRecycler(context: Context, attrs: AttributeSet?, defStyle: Int) : ScrollView(context, attrs, defStyle), ITrackContainer<SegmentData> {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private lateinit var trackContainerInner: FrameLayout
    private var trackSize = 0
    private var trackHolders = mutableMapOf<Int, AbsSegmentRecyclerAdapter.ViewHolder>()
    private var segmentHolders = mutableMapOf<Long, AbsSegmentRecyclerAdapter.ViewHolder>()
    private var handleHorizontalEvent = false
    private var visibleBoundLeft = 0
    private var visibleBoundRight = 0
    private var segmentAdapter: AbsSegmentRecyclerAdapter<AbsSegmentRecyclerAdapter.ViewHolder>? = null

    companion object {
        const val DEFAULT_TRACK_HEIGHT = 100
        const val DEFAULT_TRACK_MARGIN = 20
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        trackContainerInner = findViewById<FrameLayout>(R.id.track_container_inner)
    }

    override fun newTrack(): Int {
        val y = trackSize * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        val view = TrackView(context)
        view.setBackgroundResource(R.color.color_gray)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DEFAULT_TRACK_HEIGHT)
        params.setMargins(0, y, 0, 0)
        trackContainerInner.addView(view, params)
        trackSize++
        return trackSize - 1
    }

    override fun addSegment(data: SegmentData) {

    }

    fun setAdapter(adapter: AbsSegmentRecyclerAdapter<AbsSegmentRecyclerAdapter.ViewHolder>) {
        this.segmentAdapter = adapter
        this.segmentAdapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    fun notifyHorizontalScroll(left: Int, right: Int) {
        visibleBoundLeft = left
        visibleBoundRight = right
        Log.e("xcm", "visibleBoundLeft = $visibleBoundLeft, visibleBoundRight = $visibleBoundRight")
        updateVisibleItem()
    }

    private fun updateVisibleItem() {
        for (holder in segmentHolders.values) {
            val view = holder.itemView
            view.visibility = (if (showItemVisible(view)) VISIBLE else GONE)
        }
    }

    private fun showItemVisible(view: View): Boolean {
        return !(view.r() < visibleBoundLeft || view.l() > visibleBoundRight)
    }

    private val adapterDataObserver = object : AbsSegmentRecyclerAdapter.AdapterDataObserver() {
        override fun onChanged() {
            updateViews()
        }

        override fun onTrackInserted(trackLevels: List<Int>) {
            insertTrack(trackLevels)
        }

        override fun handleHorizontalTouchEvent(handle: Boolean) {
            handleHorizontalEvent = handle
        }

        override fun onItemChanged(ids: List<Long>) {
            handleItemChanged(ids)
        }
    }

    private fun updateViews() {
        val adapter = segmentAdapter ?: return
        trackContainerInner.removeAllViews()
        insertTrack(adapter.getTrackLevels())
        insertSegment(adapter.getSegmentIds())
    }

    private fun insertTrack(trackLevels: List<Int>) {
        for (level in trackLevels) {
            val adapter = segmentAdapter ?: return
            val holder = adapter.onCreateTrackHolder(trackContainerInner, adapter.getTrackType(level))
            adapter.onBindTrackHolder(holder, level)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, DEFAULT_TRACK_HEIGHT)
            params.setMargins(holder.x, holder.y, 0, 0)
            trackContainerInner.addView(holder.itemView, params)
            trackHolders[level] = holder
            trackSize++
        }
    }

    private fun insertSegment(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = adapter.onCreateSegmentHolder(trackContainerInner, adapter.getSegmentType(id))
            updateHolder(adapter, holder, id)
            val params = FrameLayout.LayoutParams(holder.width, DEFAULT_TRACK_HEIGHT)
            trackContainerInner.addView(holder.itemView, params)
            segmentHolders[id] = holder
        }
    }

    private fun handleItemChanged(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = segmentHolders[id] ?: return
            updateHolder(adapter, holder, id)
        }
    }

    private fun updateHolder(adapter: AbsSegmentRecyclerAdapter<AbsSegmentRecyclerAdapter.ViewHolder>,
                             holder: AbsSegmentRecyclerAdapter.ViewHolder,
                             id: Long) {
        adapter.onBindSegmentHolder(holder, id)
        holder.itemView.x = holder.x.toFloat()
        holder.itemView.y = holder.y.toFloat()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(handleHorizontalEvent)
        return super.onInterceptTouchEvent(ev)
    }

    fun handleHorizontalTouchEvent(value: Boolean) {
        handleHorizontalEvent = value
    }
}

fun View.r() : Float{
    return right + translationX
}

fun View.l() : Float{
    return left + translationX
}
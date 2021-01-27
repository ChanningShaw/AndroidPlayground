package com.wedream.demo.view.multitrack

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.base.AbsPlaneRecyclerAdapter
import com.wedream.demo.view.multitrack.base.ITrackContainer
import com.wedream.demo.view.multitrack.base.ElementData

class SegmentRecycler(context: Context, attrs: AttributeSet?, defStyle: Int) : ScrollView(context, attrs, defStyle), ITrackContainer<ElementData> {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private lateinit var trackContainerInner: FrameLayout
    private var elementHolders = mutableMapOf<Long, AbsPlaneRecyclerAdapter.ViewHolder>()
    private var handleHorizontalEvent = false
    private var visibleBoundLeft = 0
    private var visibleBoundRight = 0
    private var segmentAdapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>? = null

    companion object {
        const val DEFAULT_TRACK_HEIGHT = 100
        const val DEFAULT_TRACK_MARGIN = 20
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        trackContainerInner = findViewById<FrameLayout>(R.id.track_container_inner)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        log { "trackContainerInner.height = ${trackContainerInner.height}" }
    }

    fun setAdapter(adapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>) {
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
        for (holder in elementHolders.values) {
            val view = holder.itemView
            view.visibility = (if (showItemVisible(view)) VISIBLE else GONE)
        }
    }

    private fun showItemVisible(view: View): Boolean {
        return !(view.r() < visibleBoundLeft || view.l() > visibleBoundRight)
    }

    private val adapterDataObserver = object : AbsPlaneRecyclerAdapter.AdapterDataObserver() {
        override fun onChanged() {
            updateViews()
        }

        override fun onItemInserted(ids: List<Long>) {
            insertElements(ids)
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
        insertElements(adapter.getElementIds())
    }

    private fun insertElements(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = adapter.onCreateElementHolder(trackContainerInner, adapter.getElementType(id))
            adapter.onBindElementHolder(holder, id)
            val params = FrameLayout.LayoutParams(holder.width, holder.height)
            params.setMargins(holder.x, holder.y, 0, 0)
            trackContainerInner.addView(holder.itemView, params)
            elementHolders[id] = holder
        }
    }

    private fun handleItemChanged(ids: List<Long>) {
        for (id in ids) {
            val adapter = segmentAdapter ?: return
            val holder = elementHolders[id] ?: return
            updateHolder(adapter, holder, id)
        }
    }

    private fun updateHolder(adapter: AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>,
                             holder: AbsPlaneRecyclerAdapter.ViewHolder,
                             id: Long) {
        adapter.onBindElementHolder(holder, id)
        val params = holder.itemView.layoutParams as MarginLayoutParams
        log { "margins = ${holder.x}, ${holder.y}" }
        params.setMargins(holder.x, holder.y, 0, 0)
        holder.itemView.layoutParams = params
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
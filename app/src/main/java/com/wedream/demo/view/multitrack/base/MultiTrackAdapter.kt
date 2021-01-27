package com.wedream.demo.view.multitrack.base

import android.content.Context
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_HEIGHT
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_MARGIN
import com.wedream.demo.view.multitrack.SegmentView
import com.wedream.demo.view.multitrack.TrackElementData
import com.wedream.demo.view.multitrack.TrackView

class MultiTrackAdapter(val context: Context) : AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>() {

    private val elements = mutableMapOf<Long, TrackElementData>()
    private val levels = mutableSetOf<Int>()
    private var currentSelectId = -1L
    private var currentOperateId = -1L
    private var oldZ = 0f
    private var lastOperateSegment: TrackElementData? = null

    companion object {
        const val FOCUS_Z = 10f

        const val ELEMENT_TYPE_TRACK = 0
        const val ELEMENT_TYPE_SEGMENT = 1
        const val ELEMENT_TYPE_SLIDER = 2
        const val ELEMENT_TYPE_LEFT_DRAGGER = 3
        const val ELEMENT_TYPE_RIGHT_DRAGGER = 4
    }

    fun setData(data: List<TrackElementData>) {
        elements.clear()
        levels.clear()
        for (d in data) {
            elements[d.id] = d
            elements[d.trackLevel.toLong()] = TrackElementData(d.trackLevel.toLong(), d.trackLevel, 0, 0)
            levels.add(d.trackLevel)
        }
        notifyDataSetChanged()
    }

    override fun getElementType(id: Long): Int {
        when (id) {
            -1L -> {
                return ELEMENT_TYPE_SLIDER
            }
            -2L -> {
                return ELEMENT_TYPE_LEFT_DRAGGER
            }
            -3L -> {
                return ELEMENT_TYPE_RIGHT_DRAGGER
            }
            in 0..1000L -> {
                return ELEMENT_TYPE_TRACK
            }
            else -> {
                return ELEMENT_TYPE_SEGMENT
            }
        }
    }

    override fun onCreateElementHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            ELEMENT_TYPE_SLIDER -> {
                return TextTrackHolder(SegmentView(context))
            }
            ELEMENT_TYPE_LEFT_DRAGGER -> {
                return TextTrackHolder(SegmentView(context))
            }
            ELEMENT_TYPE_RIGHT_DRAGGER -> {
                return TextTrackHolder(SegmentView(context))
            }
            ELEMENT_TYPE_TRACK -> {
                return TextTrackHolder(TrackView(context))
            }
            else -> {
                return TextTrackHolder(SegmentView(context).apply {
                    setSegmentEventListener(segmentEventListener)
                })
            }
        }
    }

    override fun onBindElementHolder(holder: ViewHolder, id: Long) {
        val elementData = elements[id] ?: return
        when (getElementType(id)) {
            ELEMENT_TYPE_SLIDER -> {

            }
            ELEMENT_TYPE_LEFT_DRAGGER -> {

            }
            ELEMENT_TYPE_RIGHT_DRAGGER -> {

            }
            ELEMENT_TYPE_TRACK -> {
                onBindTrackHolder(elementData, holder)
            }
            else -> {
                onBindSegmentHolder(elementData, holder)
            }
        }
    }

    private fun onBindSegmentHolder(elementData: TrackElementData, holder: ViewHolder){
        holder.x = elementData.start
        holder.y = elementData.trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        holder.height = DEFAULT_TRACK_HEIGHT
        holder.width = elementData.end - elementData.start
        log { "onBindSegmentHolder :$holder" }
        val itemView = holder.itemView
        itemView.tag = elementData
        when {
            elementData.isSelected -> {
                itemView.z = FOCUS_Z
                itemView.setBackgroundResource(R.color.red_dot_color)
            }
            currentOperateId == elementData.id -> {
                itemView.setBackgroundResource(R.color.marker_text_style_b_color)
                itemView.z = FOCUS_Z
            }
            else -> {
                itemView.z = oldZ
                itemView.setBackgroundResource(R.color.marker_text_style_b_color)
            }
        }
        when {
            checkOverlap(elementData.id) -> {
                itemView.alpha = 0.2f
            }
            elementData.id == currentOperateId -> {
                itemView.alpha = 0.6f
            }
            else -> {
                itemView.alpha = 1.0f
            }
        }
    }

    private fun onBindTrackHolder(elementData: TrackElementData, holder: ViewHolder) {
        holder.width = FrameLayout.LayoutParams.MATCH_PARENT
        holder.height = DEFAULT_TRACK_HEIGHT
        holder.x = 0
        holder.y = elementData.trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        log { "onBindTrackHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.color_gray)
    }

    private var segmentEventListener = object : SegmentView.SegmentEventListener {
        override fun onActionDown(view: SegmentView) {
            val segmentData = view.tag as TrackElementData
            lastOperateSegment = segmentData.copy()
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: SegmentView, deltaX: Float, deltaY: Float) {
            val segmentData = view.tag as TrackElementData
            if (deltaY < -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN)) {
                if (segmentData.trackLevel == 0) {
                    segmentData.updateRange(deltaX.toInt(), deltaX.toInt())
                } else {
                    // 移到上一个轨道
                    segmentData.trackLevel -= 1
                    segmentData.updateRange(deltaX.toInt(), deltaX.toInt())
                }
            } else if (deltaY >= -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) && deltaY <= DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) {
                segmentData.updateRange(deltaX.toInt(), deltaX.toInt())
            } else {
                if (segmentData.trackLevel == levels.size - 1) {
                    // 向下新增一个轨道
                    val newTrack = segmentData.trackLevel + 1
                    elements[newTrack.toLong()] = TrackElementData(newTrack.toLong(), newTrack, 0, 0)
                    levels.add(newTrack)
                    notifyItemInserted(newTrack.toLong())
                    segmentData.trackLevel = newTrack
                    segmentData.updateRange(deltaX.toInt(), deltaX.toInt())
                } else {
                    // 直接移到下一个轨道
                    segmentData.trackLevel += 1
                    segmentData.updateRange(deltaX.toInt(), deltaX.toInt())
                }
            }
            notifyItemChanged(segmentData.id)
        }

        override fun onActionUp(view: SegmentView, deltaX: Float, deltaY: Float) {
            val data = (view.tag as TrackElementData)
            if (data.id == currentOperateId) {
                currentOperateId = -1L
                view.alpha = 1.0f
                view.z = oldZ
            }
            handleHorizontalTouchEvent(false)
            if (checkOverlap(data.id, deltaX, deltaY)) {
                // 位置有重叠，不能放置，做回弹动画
                lastOperateSegment?.let {
                    doRollback(data, it)
                    lastOperateSegment = null
                }
            }
        }

        override fun onLongPress(view: SegmentView) {
            val data = (view.tag as TrackElementData)
            currentOperateId = data.id
            view.alpha = 0.8f
            oldZ = view.z
            view.z = FOCUS_Z
        }

        override fun onClick(view: SegmentView) {
            elements.forEach {
                if (it.key == currentSelectId) {
                    it.value.isSelected = false
                    notifyItemChanged(it.key)
                }
            }
            val data = (view.tag as TrackElementData)
            data.isSelected = true
            currentSelectId = data.id
            oldZ = view.z
            notifyItemChanged(currentSelectId)
        }
    }

    private fun doRollback(current: TrackElementData, old: TrackElementData) {
        current.set(old)
        notifyItemChanged(current.id)
    }

    /**
     * 重叠检查
     */
    private fun checkOverlap(segmentId: Long, deltaX: Float = 0f, deltaY: Float = 0f): Boolean {
        val segmentData = elements[segmentId] ?: return false
        val rect = getSegmentBounds(segmentData, deltaX, deltaY)
        for (data in elements.values) {
            if (data.id != segmentId && rect.intersect(getSegmentBounds(data))) {
                return true
            }
        }
        return false
    }

    private fun getSegmentBounds(segmentData: TrackElementData, deltaX: Float = 0.0f, deltaY: Float = 0.0f): RectF {
        val left = segmentData.start + deltaX
        val top = segmentData.trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN) + deltaY
        val right = segmentData.end + deltaX
        val bottom = top + (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        return RectF(left, top, right, bottom)
    }

    override fun getElementIds(): List<Long> {
        return elements.keys.toList()
    }

    class TextTrackHolder(itemView: View) : AbsPlaneRecyclerAdapter.ViewHolder(itemView)
}

inline fun <T, R> Iterable<T>.mapIf(predict: (T) -> Boolean, transform: (T) -> R): List<R> {
    val list = mutableListOf<R>()
    for (item in this)
        if (predict(item)) list.add(transform(item))
    return list
}

inline fun <T, R> Iterable<T>.mapDistinct(transform: (T) -> R): List<R> {
    val map = hashSetOf<R>()
    for (item in this)
        map.add(transform(item))
    return map.toList()
}
package com.wedream.demo.view.multitrack

import android.content.Context
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import com.wedream.demo.R
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_HEIGHT
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_MARGIN
import com.wedream.demo.view.multitrack.base.AbsSegmentRecyclerAdapter
import com.wedream.demo.view.multitrack.base.SegmentData

class TextSegmentAdapter(val context: Context) : AbsSegmentRecyclerAdapter<AbsSegmentRecyclerAdapter.ViewHolder>() {

    private val segments = mutableListOf<SegmentData>()
    private val levels = mutableSetOf<Int>()
    private var currentSelectId = -1L
    private var currentOperateId = -1L
    private var oldZ = 0f

    companion object {
        const val FOCUS_Z = 10f
    }

    fun setData(data: List<SegmentData>) {
        this.segments.clear()
        this.segments.addAll(data)
        levels.clear()
        for (s in segments) {
            levels.add(s.trackLevel)
        }
        notifyDataSetChanged()
    }

    override fun onCreateTrackHolder(parent: ViewGroup, trackType: Int): ViewHolder {
        return TextTrackHolder(TrackView(context))
    }

    override fun onCreateSegmentHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return TextTrackHolder(SegmentView(context))
    }

    override fun onBindTrackHolder(holder: ViewHolder, trackLevel: Int) {
        holder.x = 0
        holder.y = trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        holder.itemView.setBackgroundResource(R.color.color_gray)
    }

    override fun onBindSegmentHolder(holder: ViewHolder, segmentId: Long) {
        val segmentData = segments.find { it.id == segmentId } ?: return
        holder.x = segmentData.start
        holder.y = segmentData.trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        holder.width = segmentData.end - segmentData.start
        val itemView = holder.itemView
        itemView.tag = segmentData
        if (segmentData.isSelected) {
            itemView.z = FOCUS_Z
            itemView.setBackgroundResource(R.color.red_dot_color)
        } else if (currentOperateId == segmentData.id) {
            itemView.setBackgroundResource(R.color.marker_text_style_b_color)
            itemView.z = FOCUS_Z
        } else {
            itemView.z = oldZ
            itemView.setBackgroundResource(R.color.marker_text_style_b_color)
        }
        if (itemView is SegmentView) {
            itemView.setSegmentEventListener(segmentEventListener)
        }
    }

    private var segmentEventListener = object : SegmentView.SegmentEventListener {
        override fun onActionDown(view: SegmentView) {
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: SegmentView, deltaX: Float, deltaY: Float) {
            val segmentData = view.tag as SegmentData
            if (checkOverlap(segmentData.id, deltaX, deltaY)) {
                view.alpha = 0.3f
            } else if (segmentData.id == currentOperateId) {
                view.alpha = 0.8f
            }
            if (deltaY < -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN)) {
                if (segmentData.trackLevel == 0) {
                    segmentData.update(0, deltaX.toInt(), deltaX.toInt())
                } else {
                    // 移到上一个轨道
                    segmentData.update(-1, deltaX.toInt(), deltaX.toInt())
                }
            } else if (deltaY >= -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) && deltaY <= DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) {
                segmentData.update(0, deltaX.toInt(), deltaX.toInt())
            } else {
                if (segmentData.trackLevel == levels.size - 1) {
                    // 向下新增一个轨道
                    levels.add(segmentData.trackLevel + 1)
                    notifyTrackInsert(segmentData.trackLevel + 1)
                    segmentData.update(1, deltaX.toInt(), deltaX.toInt())
                } else {
                    // 直接移到下一个轨道
                    segmentData.update(1, deltaX.toInt(), deltaX.toInt())
                }
            }
            notifyItemChanged(segmentData.id)
        }

        override fun onActionUp(view: SegmentView, deltaX: Float, deltaY: Float) {
            val data = (view.tag as SegmentData)
            if (data.id == currentOperateId) {
                currentOperateId = -1L
                view.alpha = 1.0f
                view.z = oldZ
            }
            handleHorizontalTouchEvent(false)
        }

        override fun onLongPress(view: SegmentView) {
            val data = (view.tag as SegmentData)
            currentOperateId = data.id
            view.alpha = 0.8f
            oldZ = view.z
            view.z = FOCUS_Z
        }

        override fun onClick(view: SegmentView) {
            segments.forEach {
                if (it.id == currentSelectId) {
                    it.isSelected = false
                    notifyItemChanged(it.id)
                }
            }
            val data = (view.tag as SegmentData)
            data.isSelected = true
            currentSelectId = data.id
            oldZ = view.z
            notifyItemChanged(currentSelectId)
        }
    }

    /**
     * 重叠检查
     */
    private fun checkOverlap(segmentId: Long, deltaX: Float = 0f, deltaY: Float = 0f): Boolean {
        val segmentData = segments.find { it.id == segmentId } ?: return false
        val rect = getSegmentBounds(segmentData, deltaX, deltaY)
        for (data in segments) {
            if (data.id != segmentId && rect.intersect(getSegmentBounds(data, 0f, 0f))) {
                return true
            }
        }
        return false
    }

    private fun getSegmentBounds(segmentData: SegmentData, deltaX: Float, deltaY: Float): RectF {
        val left = segmentData.start + deltaX
        val top = segmentData.trackLevel * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN) + deltaY
        val right = segmentData.end + deltaX
        val bottom = top + (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        return RectF(left, top, right, bottom)
    }

    override fun getTrackLevels(): List<Int> {
        return levels.toList()
    }

    override fun getSegmentIds(): List<Long> {
        return segments.map { it.id }
    }

    class TextTrackHolder(itemView: View) : AbsSegmentRecyclerAdapter.ViewHolder(itemView)
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

//fun RectF.isOverLapWith(other: RectF): Boolean {
//    return !(left > other.right || top > other.bottom || right < other.left || bottom < other.top)
//}
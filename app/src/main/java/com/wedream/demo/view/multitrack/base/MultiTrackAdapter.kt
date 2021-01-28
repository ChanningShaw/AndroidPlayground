package com.wedream.demo.view.multitrack.base

import android.content.Context
import android.graphics.RectF
import android.util.Range
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.*
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_SLIDER_MARGIN
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_SLIDER_WIDTH
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_HEIGHT
import com.wedream.demo.view.multitrack.SegmentRecycler.Companion.DEFAULT_TRACK_MARGIN

class MultiTrackAdapter(val context: Context) : AbsPlaneRecyclerAdapter<AbsPlaneRecyclerAdapter.ViewHolder>() {

    private val elements = mutableMapOf<Long, TrackElementData>()
    private val levels = mutableSetOf<Int>()
    private var currentSelectId = -1L
    private var currentOperateId = -1L
    private var lastOperateSegment: TrackElementData? = null

    companion object {
        const val FOCUS_Z = 10f

        const val ELEMENT_TYPE_TRACK = 0
        const val ELEMENT_TYPE_SEGMENT = 1
        const val ELEMENT_TYPE_SLIDER = 2
        const val ELEMENT_TYPE_LEFT_DRAGGER = 3
        const val ELEMENT_TYPE_RIGHT_DRAGGER = 4

        const val ID_SLIDER = -1L
        const val ID_LEFT_DRAGGER = -2L
        const val ID_RIGHT_DRAGGER = -3L
    }

    fun setData(data: List<TrackElementData>) {
        elements.clear()
        levels.clear()
        for (d in data) {
            elements[d.id] = d
            elements[d.trackLevel.toLong()] = TrackElementData(d.trackLevel.toLong(), 0, FrameLayout.LayoutParams.MATCH_PARENT, d.trackLevel)
            levels.add(d.trackLevel)
        }
        notifyDataSetChanged()
    }

    override fun getElementType(id: Long): Int {
        when (id) {
            ID_SLIDER -> {
                return ELEMENT_TYPE_SLIDER
            }
            ID_LEFT_DRAGGER -> {
                return ELEMENT_TYPE_LEFT_DRAGGER
            }
            ID_RIGHT_DRAGGER -> {
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
                return TextTrackHolder(SliderView(context).apply {
                    setSegmentEventListener(sliderEventListener)
                })
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
                elements[currentSelectId]?.let {
                    onBindSliderHolder(elementData, holder)
                }
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

    private fun onBindSegmentHolder(elementData: TrackElementData, holder: ViewHolder) {
        holder.x = elementData.left
        holder.y = elementData.top
        holder.height = elementData.height
        holder.width = elementData.width
        val itemView = holder.itemView
        itemView.tag = elementData
        when {
            elementData.isSelect() -> {
                itemView.z = FOCUS_Z
                itemView.setBackgroundResource(R.color.red_dot_color)
            }
            currentOperateId == elementData.id -> {
                itemView.setBackgroundResource(R.color.marker_text_style_b_color)
                itemView.z = FOCUS_Z
            }
            else -> {
                itemView.z = 0.0f
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
        holder.width = elementData.width
        holder.height = elementData.height
        holder.x = elementData.left
        holder.y = elementData.top
        log { "onBindTrackHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.color_gray)
    }

    private fun onBindSliderHolder(elementData: TrackElementData, holder: ViewHolder) {
        holder.width = elementData.width
        holder.height = elementData.height
        holder.x = elementData.left
        holder.y = elementData.top
        log { "onBindSliderHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.red_dot_color)
        holder.itemView.tag = elementData
    }

    private var segmentEventListener = object : ElementView.ElementEventListener {
        override fun onActionDown(view: ElementView) {
            val segmentData = view.tag as TrackElementData
            lastOperateSegment = segmentData.copy()
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: ElementView, deltaX: Float, deltaY: Float) {
            if (!view.isLongPressed()) {
                return
            }
            val elementData = view.tag as TrackElementData
            if (deltaY < -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN)) {
                if (elementData.trackLevel == 0) {
                    elementData.horizontalMoveBy(deltaX.toInt())
                } else {
                    // 移到上一个轨道
                    elementData.trackLevel -= 1
                    elementData.horizontalMoveBy(deltaX.toInt())
                }
            } else if (deltaY >= -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) && deltaY <= DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) {
                elementData.horizontalMoveBy(deltaX.toInt())
            } else {
                if (elementData.trackLevel == levels.size - 1) {
                    // 向下新增一个轨道
                    val newTrack = elementData.trackLevel + 1
                    elements[newTrack.toLong()] = TrackElementData(newTrack.toLong(), 0, FrameLayout.LayoutParams.MATCH_PARENT, newTrack)
                    levels.add(newTrack)
                    notifyItemInserted(newTrack.toLong())
                    elementData.trackLevel = newTrack
                    elementData.horizontalMoveBy(deltaX.toInt())
                } else {
                    // 直接移到下一个轨道
                    elementData.trackLevel += 1
                    elementData.horizontalMoveBy(deltaX.toInt())
                }
            }
            notifyItemChanged(elementData.id)
        }

        override fun onActionUp(view: ElementView, deltaX: Float, deltaY: Float) {
            val data = (view.tag as TrackElementData)
            if (data.id == currentOperateId) {
                currentOperateId = -1L
                view.alpha = 1.0f
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

        override fun onLongPress(view: ElementView) {
            val data = (view.tag as TrackElementData)
            currentOperateId = data.id
            view.alpha = 0.8f
            unSelect()
            notifyItemChanged(data.id)
        }

        override fun onClick(view: ElementView) {
            val data = (view.tag as TrackElementData)
            select(data)
        }
    }

    private val sliderEventListener = object : ElementView.ElementEventListener {
        override fun onActionDown(view: ElementView) {
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: ElementView, deltaX: Float, deltaY: Float) {
            // TODO 避免强转
            val data = (view.tag as SliderData)
            elements[data.targetSegmentId]?.let {
                // 重叠检测
                if (checkRemainRelativePosition(it.id, deltaX)) {
                    data.horizontalMoveBy(deltaX.toInt())
                    notifyItemChanged(data.id)
                    it.horizontalMoveBy(deltaX.toInt())
                    notifyItemChanged(it.id)
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Float, deltaY: Float) {
            handleHorizontalTouchEvent(false)
        }

        override fun onLongPress(view: ElementView) {
        }

        override fun onClick(view: ElementView) {
        }
    }

    private fun select(data: TrackElementData) {
        unSelect()
        currentSelectId = data.id
        // 显示拖把和耳朵
        data.setSelect(true)
        notifyItemChanged(data.id)
        elements[ID_SLIDER] = SliderData(ID_SLIDER, data.left - (DEFAULT_SLIDER_MARGIN + DEFAULT_SLIDER_WIDTH),
            DEFAULT_SLIDER_WIDTH, data.trackLevel).apply {
            targetSegmentId = data.id
        }
        notifyItemInserted(ID_SLIDER)
    }

    private fun unSelect() {
        // 先把之前的反选
        if (currentSelectId != -1L) {
            elements[currentSelectId]?.setSelect(false)
            notifyItemChanged(currentSelectId)
            currentSelectId = -1L
            // 隐藏耳朵
            elements.remove(ID_SLIDER)?.let {
                notifyItemRemoved(it.id)
            }
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
            if (getElementType(data.id) != ELEMENT_TYPE_SEGMENT) continue
            if (data.id != segmentId && rect.intersect(getSegmentBounds(data))) {
                return true
            }
        }
        return false
    }

    /**
     * 检查移动后同一个轨道的两个segment是否还保持同样的相对位置
     */
    private fun checkRemainRelativePosition(segmentId: Long, deltaX: Float = 0f): Boolean {
        val segmentData = elements[segmentId] ?: return false
        val originRange = getHorizontalRange(segmentData)
        val newRange = getHorizontalRange(segmentData, deltaX)
        for (data in elements.values) {
            if (getElementType(data.id) != ELEMENT_TYPE_SEGMENT
                || data.trackLevel != segmentData.trackLevel
                || data.id == segmentId
            ) {
                continue
            }
            val range = getHorizontalRange(data)
            if (checkRangedRelativePosition(originRange, range) != checkRangedRelativePosition(newRange, range)) {
                return false
            }
        }
        return true
    }

    /**
     * 返回两个range的位置相关关系
     *
     * 如果 range1 在 range2 的左边， 返回-1
     * 如果 range1 在 range2 的右边， 返回1
     * 如果 range1 和 range2 相交，返回0
     */
    private fun checkRangedRelativePosition(range1: Range<Float>, range2: Range<Float>): Int {
        return when {
            range1.upper < range2.lower -> {
                -1
            }
            range1.lower > range2.upper -> {
                1
            }
            else -> {
                0
            }
        }
    }

    private fun getSegmentBounds(segmentData: TrackElementData, deltaX: Float = 0.0f, deltaY: Float = 0.0f): RectF {
        val left = segmentData.left + deltaX
        val top = segmentData.top + deltaY
        val right = left + segmentData.width
        val bottom = top + segmentData.height
        return RectF(left, top, right, bottom)
    }

    private fun getHorizontalRange(segmentData: TrackElementData, deltaX: Float = 0f): Range<Float> {
        return Range(segmentData.left + deltaX, segmentData.right() + deltaX)
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
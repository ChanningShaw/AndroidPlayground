package com.wedream.demo.view.multitrack.base

import android.content.Context
import android.graphics.RectF
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
            elements[d.getId()] = d
            elements[d.getTrackLevel().toLong()] = TrackElementData(d.getTrackLevel().toLong(), d.getTrackLevel(), 0, 0)
            levels.add(d.getTrackLevel())
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

    private fun onBindSegmentHolder(elementData: TrackElementData, holder: ViewHolder){
        holder.x = elementData.getStart()
        holder.y = elementData.getTrackLevel() * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        holder.height = DEFAULT_TRACK_HEIGHT
        holder.width = elementData.length()
        val itemView = holder.itemView
        itemView.tag = elementData
        when {
            elementData.isSelect() -> {
                itemView.z = FOCUS_Z
                itemView.setBackgroundResource(R.color.red_dot_color)
            }
            currentOperateId == elementData.getId() -> {
                itemView.setBackgroundResource(R.color.marker_text_style_b_color)
                itemView.z = FOCUS_Z
            }
            else -> {
                itemView.z = 0.0f
                itemView.setBackgroundResource(R.color.marker_text_style_b_color)
            }
        }
        when {
            checkOverlap(elementData.getId()) -> {
                itemView.alpha = 0.2f
            }
            elementData.getId() == currentOperateId -> {
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
        holder.y = elementData.getTrackLevel() * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
        log { "onBindTrackHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.color_gray)
    }

    private fun onBindSliderHolder(elementData: TrackElementData, holder: ViewHolder) {
        holder.width = DEFAULT_SLIDER_WIDTH
        holder.height = DEFAULT_TRACK_HEIGHT
        holder.x = elementData.getStart()
        holder.y = elementData.getTrackLevel() * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN)
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
                if (elementData.getTrackLevel() == 0) {
                    elementData.horizontalMoveBy(deltaX.toInt())
                } else {
                    // 移到上一个轨道
                    elementData.setTrackLevel(elementData.getTrackLevel() - 1)
                    elementData.horizontalMoveBy(deltaX.toInt())
                }
            } else if (deltaY >= -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) && deltaY <= DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) {
                elementData.horizontalMoveBy(deltaX.toInt())
            } else {
                if (elementData.getTrackLevel() == levels.size - 1) {
                    // 向下新增一个轨道
                    val newTrack = elementData.getTrackLevel() + 1
                    elements[newTrack.toLong()] = TrackElementData(newTrack.toLong(), newTrack, 0, 0)
                    levels.add(newTrack)
                    notifyItemInserted(newTrack.toLong())
                    elementData.setTrackLevel(newTrack)
                    elementData.horizontalMoveBy(deltaX.toInt())
                } else {
                    // 直接移到下一个轨道
                    elementData.setTrackLevel(elementData.getTrackLevel() + 1)
                    elementData.horizontalMoveBy(deltaX.toInt())
                }
            }
            notifyItemChanged(elementData.getId())
        }

        override fun onActionUp(view: ElementView, deltaX: Float, deltaY: Float) {
            val data = (view.tag as TrackElementData)
            if (data.getId() == currentOperateId) {
                currentOperateId = -1L
                view.alpha = 1.0f
            }
            handleHorizontalTouchEvent(false)
            if (checkOverlap(data.getId(), deltaX, deltaY)) {
                // 位置有重叠，不能放置，做回弹动画
                lastOperateSegment?.let {
                    doRollback(data, it)
                    lastOperateSegment = null
                }
            }
        }

        override fun onLongPress(view: ElementView) {
            val data = (view.tag as TrackElementData)
            currentOperateId = data.getId()
            view.alpha = 0.8f
            unSelect()
            notifyItemChanged(data.getId())
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
            elements[data.getTargetSegmentId()]?.let {
                // 重叠检测
                if (!checkOverlap(it.getId(), deltaX, 0f)) {
                    data.horizontalMoveBy(deltaX.toInt())
                    notifyItemChanged(data.getId())
                    it.horizontalMoveBy(deltaX.toInt())
                    notifyItemChanged(it.getId())
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
        currentSelectId = data.getId()
        // 显示拖把和耳朵
        data.setSelect(true)
        notifyItemChanged(data.getId())
        elements[ID_SLIDER] = SliderData(ID_SLIDER, data.getTrackLevel(),
            data.getStart() - (DEFAULT_SLIDER_MARGIN + DEFAULT_SLIDER_WIDTH), data.getStart() - DEFAULT_SLIDER_MARGIN).apply {
            setTargetSegmentId(data.getId())
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
                notifyItemRemoved(it.getId())
            }
        }
    }

    private fun doRollback(current: TrackElementData, old: TrackElementData) {
        current.set(old)
        notifyItemChanged(current.getId())
    }

    /**
     * 重叠检查
     */
    private fun checkOverlap(segmentId: Long, deltaX: Float = 0f, deltaY: Float = 0f): Boolean {
        val segmentData = elements[segmentId] ?: return false
        val rect = getSegmentBounds(segmentData, deltaX, deltaY)
        for (data in elements.values) {
            if (getElementType(data.getId()) != ELEMENT_TYPE_SEGMENT) continue
            if (data.getId() != segmentId && rect.intersect(getSegmentBounds(data))) {
                return true
            }
        }
        return false
    }

    private fun getSegmentBounds(segmentData: TrackElementData, deltaX: Float = 0.0f, deltaY: Float = 0.0f): RectF {
        val left = segmentData.getStart() + deltaX
        val top = segmentData.getTrackLevel() * (DEFAULT_TRACK_HEIGHT + DEFAULT_TRACK_MARGIN) + deltaY
        val right = segmentData.getEnd() + deltaX
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
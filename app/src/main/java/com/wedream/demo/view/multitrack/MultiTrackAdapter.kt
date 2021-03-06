package com.wedream.demo.view.multitrack

import android.content.Context
import android.graphics.Rect
import android.util.Range
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.base.AbsPlaneRecyclerAdapter
import com.wedream.demo.view.multitrack.base.ElementView

class MultiTrackAdapter(val context: Context) : AbsPlaneRecyclerAdapter<PlaneRecycler.ViewHolder>() {

    private val dataList = mutableListOf<ElementData>()
    private val elements = mutableMapOf<Long, ElementData>()
    private var minLevel = Int.MAX_VALUE
    private var maxLevel = Int.MIN_VALUE
    private var currentSelectId = -1L
    private var currentOperateId = -1L
    private var lastOperateSegment: ElementData? = null
    private var bounds = Rect()

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

        const val DEFAULT_TRACK_HEIGHT = 100
        const val DEFAULT_TRACK_MARGIN = 20
        const val DEFAULT_SLIDER_WIDTH = 100
        const val DEFAULT_SLIDER_MARGIN = 100
        const val DEFAULT_DRAGGER_WIDTH = 50

        const val MIN_ELEMENT_WITH = 10
    }

    fun setData(data: List<ElementData>) {
        dataList.clear()
        dataList.addAll(data)
        updateDataSet()
    }

    private fun updateDataSet() {
        elements.clear()

        for (d in dataList) {
            elements[d.id] = d
            if (d.trackLevel < minLevel) {
                minLevel = d.trackLevel
            } else if (d.trackLevel > maxLevel) {
                maxLevel = d.trackLevel
            }
        }

        for (i in minLevel..maxLevel) {
            addTrack(i)
        }

        notifyDataSetChanged()
    }

    private fun addTrack(level: Int) {
        elements[level.toLong()] = ElementData(level.toLong(), 0, FrameLayout.LayoutParams.MATCH_PARENT, level)
        if (level < minLevel) {
            minLevel = level
        } else if (level > maxLevel) {
            maxLevel = level
        }
    }

    private fun topAddTrack(): Int {
        // 在顶部添加新轨，原来所有轨道层级增加1， 需要全量刷新
        for (e in dataList) {
            e.trackLevel += 1
        }
        updateDataSet()
        addTrack(0)
        notifyDataSetChanged()
        return 0
    }

    fun clearSelect() {
        unSelect()
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

    override fun onCreateElementHolder(parent: ViewGroup, viewType: Int): PlaneRecycler.ViewHolder {
        when (viewType) {
            ELEMENT_TYPE_SLIDER -> {
                return TextTrackHolder(SliderView(context).apply {
                    setElementEventListener(sliderEventListener)
                })
            }
            ELEMENT_TYPE_LEFT_DRAGGER -> {
                return TextTrackHolder(DraggerView(context).apply {
                    setElementEventListener(draggerEventListener)
                })
            }
            ELEMENT_TYPE_RIGHT_DRAGGER -> {
                return TextTrackHolder(DraggerView(context).apply {
                    setElementEventListener(draggerEventListener)
                })
            }
            ELEMENT_TYPE_TRACK -> {
                return TextTrackHolder(TrackView(context))
            }
            else -> {
                return TextTrackHolder(SegmentView(context).apply {
                    setElementEventListener(segmentEventListener)
                })
            }
        }
    }

    override fun onBindElementHolder(holder: PlaneRecycler.ViewHolder, id: Long) {
        val elementData = elements[id] ?: return
        when (getElementType(id)) {
            ELEMENT_TYPE_SLIDER -> {
                elements[currentSelectId]?.let {
                    onBindSliderHolder(elementData, holder)
                }
            }
            ELEMENT_TYPE_LEFT_DRAGGER -> {
                onBindDraggerHolder(elementData, holder)
            }
            ELEMENT_TYPE_RIGHT_DRAGGER -> {
                onBindDraggerHolder(elementData, holder)
            }
            ELEMENT_TYPE_TRACK -> {
                onBindTrackHolder(elementData, holder)
            }
            else -> {
                onBindSegmentHolder(elementData, holder)
            }
        }
    }

    private fun onBindSegmentHolder(elementData: ElementData, holder: PlaneRecycler.ViewHolder) {
        elementData.bindBorder(holder.itemBorder)
        val itemView = holder.itemView
        itemView.tag = elementData
        when {
            elementData.isSelect() -> {
                itemView.z = FOCUS_Z
            }
            currentOperateId == elementData.id -> {
                itemView.z = FOCUS_Z
            }
            else -> {
                itemView.z = 0.0f
            }
        }
        when {
//            checkOverlap(elementData.id) -> {
//                itemView.alpha = 0.2f
//            }
            elementData.id == currentOperateId -> {
                itemView.alpha = 0.6f
            }
            else -> {
                itemView.alpha = 1.0f
            }
        }
    }

    private fun onBindTrackHolder(elementData: ElementData, holder: PlaneRecycler.ViewHolder) {
        elementData.bindBorder(holder.itemBorder)
        log { "onBindTrackHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.color_gray)
    }

    private fun onBindSliderHolder(elementData: ElementData, holder: PlaneRecycler.ViewHolder) {
        elementData.bindBorder(holder.itemBorder)
        log { "onBindSliderHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.red_dot_color)
        holder.itemView.tag = elementData
    }

    private fun onBindDraggerHolder(elementData: ElementData, holder: PlaneRecycler.ViewHolder) {
        elementData.bindBorder(holder.itemBorder)
        log { "onBindDraggerHolder :$holder" }
        holder.itemView.setBackgroundResource(R.color.red_dot_color)
        holder.itemView.tag = elementData
    }

    private var segmentEventListener = object : ElementView.ElementEventListener {

        // 一次移动只能添加一条轨道
        private var hasAddedTrack = false

        override fun onActionDown(view: ElementView) {
            val segmentData = view.tag as ElementData
            lastOperateSegment = segmentData.copy()
            handleHorizontalTouchEvent(true)
            hasAddedTrack = false
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (!view.isLongPressed()) {
                return
            }
            val elementData = view.tag as ElementData
            if (deltaY < -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN)) {
                if (elementData.trackLevel == minLevel) {
                    if (!hasAddedTrack) {
                        hasAddedTrack = true
                        // 向上新增一个轨道
                        val newTrack = topAddTrack()
                        elementData.trackLevel = newTrack
                    }
                    moveElement(elementData, deltaX)
                } else {
                    // 移到上一个轨道
                    elementData.trackLevel -= 1
                    moveElement(elementData, deltaX)
                }
            } else if (deltaY >= -(DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) && deltaY <= DEFAULT_TRACK_HEIGHT / 2 + DEFAULT_TRACK_MARGIN) {
                moveElement(elementData, deltaX)
            } else {
                if (elementData.trackLevel == maxLevel) {
                    if (!hasAddedTrack) {
                        hasAddedTrack = true
                        // 向下新增一个轨道
                        val newTrack = elementData.trackLevel + 1
                        addTrack(newTrack)
                        notifyItemInserted(newTrack.toLong())
                        elementData.trackLevel = newTrack
                    }
                    moveElement(elementData, deltaX)
                } else {
                    // 直接移到下一个轨道
                    elementData.trackLevel += 1
                    moveElement(elementData, deltaX)
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            val data = (view.tag as ElementData)
            val offsetX = clampOffsetX(data, deltaX)
            val offsetY = clampOffsetY(data, deltaY)
            if (data.id == currentOperateId) {
                currentOperateId = -1L
                view.alpha = 1.0f
            }
            if (checkOverlap(data.id, offsetX, offsetY)) {
                // 位置有重叠，不能放置，做回弹动画
                lastOperateSegment?.let {
                    doRollback(data, it)
                    lastOperateSegment = null
                }
            }
            handleHorizontalTouchEvent(false)
        }

        override fun onLongPress(view: ElementView) {
            val data = (view.tag as ElementData)
            currentOperateId = data.id
            view.alpha = 0.8f
            unSelect()
            notifyItemChanged(data.id)
        }

        override fun onClick(view: ElementView) {
            val data = (view.tag as ElementData)
            select(data)
        }
    }

    private val sliderEventListener = object : ElementView.ElementEventListener {
        override fun onActionDown(view: ElementView) {
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            // TODO 避免强转
            val data = (view.tag as AttachElementData)
            val offset = clampOffsetX(data, deltaX)
            elements[data.targetId]?.let {
                // 重叠检测
                if (checkRemainRelativePosition(it.id, offset)) {
                    data.horizontalMoveBy(offset)
                    notifyItemMoved(data.id)
                    moveElement(it, offset)
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            handleHorizontalTouchEvent(false)
        }

        override fun onLongPress(view: ElementView) {
        }

        override fun onClick(view: ElementView) {
        }
    }

    private val draggerEventListener = object : ElementView.ElementEventListener {
        override fun onActionDown(view: ElementView) {
            handleHorizontalTouchEvent(true)
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            val data = (view.tag as DraggerElementData)
            elements[data.targetId]?.let {
                // 最小长度检测
                if ((data.isLeft && it.width - deltaX <= MIN_ELEMENT_WITH)
                    || (!data.isLeft && it.width + deltaX <= MIN_ELEMENT_WITH)) {
                    return
                }
                // 重叠检测
                val offset = clampOffsetX(data, deltaX)
                if (checkRemainRelativePosition(it.id, offset)) {
                    data.horizontalMoveBy(offset)
                    notifyItemMoved(data.id)
                    if (data.isLeft) {
                        it.left += offset
                        it.width -= offset
                        // 拖把要跟着一起动
                        elements[ID_SLIDER]?.let {
                            it.horizontalMoveBy(offset)
                            notifyItemMoved(ID_SLIDER)
                        }
                    } else {
                        it.width += offset
                    }
                    notifyItemMoved(it.id)
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            handleHorizontalTouchEvent(false)
        }

        override fun onLongPress(view: ElementView) {
        }

        override fun onClick(view: ElementView) {
        }
    }

    private fun moveElement(data: ElementData, deltaX: Int) {
        val offset = clampOffsetX(data, deltaX)
        data.horizontalMoveBy(offset)
        // 耳朵跟着移到
        elements[ID_LEFT_DRAGGER]?.let {
            it.horizontalMoveBy(offset)
            notifyItemMoved(it.id)
        }
        elements[ID_RIGHT_DRAGGER]?.let {
            it.horizontalMoveBy(offset)
            notifyItemMoved(it.id)
        }
        notifyItemMoved(data.id)
    }

    private fun clampOffsetX(data: ElementData, deltaX: Int): Int {
        var offset = deltaX
        if (data.left + deltaX <= bounds.left) {
            offset = bounds.left - data.left
        }
        if (data.right() + deltaX >= bounds.right) {
            offset = bounds.right - data.width - data.left
        }
        return offset
    }

    private fun clampOffsetY(data: ElementData, deltaY: Int): Int {
        var offset = deltaY
        if (data.top + deltaY <= bounds.top) {
            offset = bounds.top - data.top
        }
        if (data.bottom() + deltaY >= bounds.bottom) {
            offset = bounds.bottom - data.height - data.top
        }
        return offset
    }

    private fun select(data: ElementData) {
        unSelect()
        currentSelectId = data.id
        // 显示拖把和耳朵
        data.setSelect(true)
        notifyItemChanged(data.id)
        elements[ID_SLIDER] = AttachElementData(ID_SLIDER, data.left - (DEFAULT_SLIDER_MARGIN + DEFAULT_SLIDER_WIDTH),
            DEFAULT_SLIDER_WIDTH, data.trackLevel).apply {
            targetId = data.id
        }
        elements[ID_LEFT_DRAGGER] = DraggerElementData(ID_LEFT_DRAGGER, data.left - DEFAULT_DRAGGER_WIDTH,
            DEFAULT_DRAGGER_WIDTH, data.trackLevel, true).apply {
            targetId = data.id
        }
        elements[ID_RIGHT_DRAGGER] = DraggerElementData(ID_RIGHT_DRAGGER, data.right(),
            DEFAULT_DRAGGER_WIDTH, data.trackLevel, false).apply {
            targetId = data.id
        }
        notifyItemInserted(ID_SLIDER)
        notifyItemInserted(ID_LEFT_DRAGGER)
        notifyItemInserted(ID_RIGHT_DRAGGER)
    }

    private fun unSelect() {
        // 先把之前的反选
        if (currentSelectId != -1L) {
            elements[currentSelectId]?.setSelect(false)
            notifyItemChanged(currentSelectId)
            currentSelectId = -1L
            removeAttachData()
        }
    }

    private fun removeAttachData() {
        elements.remove(ID_SLIDER)?.let {
            notifyItemRemoved(it.id)
        }
        elements.remove(ID_LEFT_DRAGGER)?.let {
            notifyItemRemoved(it.id)
        }
        elements.remove(ID_RIGHT_DRAGGER)?.let {
            notifyItemRemoved(it.id)
        }
    }

    private fun doRollback(current: ElementData, old: ElementData) {
        current.set(old)
        notifyItemMoved(current.id)
    }

    /**
     * 重叠检查
     */
    private fun checkOverlap(segmentId: Long, deltaX: Int = 0, deltaY: Int = 0): Boolean {
        val elementData = elements[segmentId] ?: return false
        val rect = getElementBounds(elementData, deltaX, deltaY)
        for (data in elements.values) {
            if (getElementType(data.id) != ELEMENT_TYPE_SEGMENT) continue
            if (data.id != segmentId && rect.overlap(getElementBounds(data))) {
                return true
            }
        }
        return false
    }

    /**
     * 检查移动后同一个轨道的两个segment是否还保持同样的相对位置
     */
    private fun checkRemainRelativePosition(segmentId: Long, deltaX: Int = 0): Boolean {
        val segmentData = elements[segmentId] ?: return false
        val originRange = getHorizontalRange(segmentData)
        val newRange = getHorizontalRange(segmentData, deltaX)
        for (data in elements.values) {
            if (getElementType(data.id) != ELEMENT_TYPE_SEGMENT
                || data.trackLevel != segmentData.trackLevel
                || data.id == segmentId) {
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
    private fun checkRangedRelativePosition(range1: Range<Int>, range2: Range<Int>): Int {
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

    private fun getElementBounds(elementData: ElementData, deltaX: Int = 0, deltaY: Int = 0): Rect {
        val left = elementData.left + deltaX
        val top = elementData.top + deltaY
        val right = left + elementData.width
        val bottom = top + elementData.height
        return Rect(left, top, right, bottom)
    }

    private fun getHorizontalRange(segmentData: ElementData, deltaX: Int = 0): Range<Int> {
        return Range(segmentData.left + deltaX, segmentData.right() + deltaX)
    }

    override fun getElementIds(): List<Long> {
        return elements.keys.toList()
    }

    override fun getViewBorder(id: Long, parent: ViewGroup): PlaneRecycler.ViewBorder {
        return PlaneRecycler.ViewBorder().apply {
            elements[id]?.let {
                it.bindBorder(this)
            }
            this.setupWithParent(parent)
        }
    }

    fun setBounds(bound: Rect) {
        this.bounds.set(bound)
    }

    class TextTrackHolder(itemView: View) : PlaneRecycler.ViewHolder(itemView)
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

fun ElementData.bindBorder(border: PlaneRecycler.ViewBorder) {
    border.x = left
    border.y = top
    border.width = width
    border.height = height
}
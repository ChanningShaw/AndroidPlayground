package com.wedream.demo.view.multitrack

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.base.AbsPlaneRecyclerAdapter
import com.wedream.demo.view.multitrack.base.ElementData
import com.wedream.demo.view.multitrack.base.ITrackContainer
import java.util.*


class PlaneRecycler(context: Context, attrs: AttributeSet?, defStyle: Int) : ScrollView(context, attrs, defStyle), ITrackContainer<ElementData> {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private lateinit var trackContainerInner: FrameLayout

    private var allBorders = mutableMapOf<Long, ViewBorder>()
    private var allHolders = mutableMapOf<Long, ViewHolder>()

    // 按照viewType缓存的holder
    private var cacheHolders = hashMapOf<Int, LinkedList<ViewHolder>>()
    private var visibleHolders = hashMapOf<Long, ViewHolder>()

    private var handleHorizontalEvent = false
    private var recyclerAdapter: AbsPlaneRecyclerAdapter<ViewHolder>? = null
    private var listener: EventListener? = null

    private var choreographer = Choreographer.getInstance()
    private var dataSetChanged = false

    private var visibleRegion = Rect()

    init {
        isFillViewport = false
    }

    private var frameCallback = Choreographer.FrameCallback {
        if (dataSetChanged) {
            dataSetChanged = false
            updateViews()
            return@FrameCallback
        }
    }

    fun setAdapter(adapter: AbsPlaneRecyclerAdapter<ViewHolder>) {
        this.recyclerAdapter = adapter
        this.recyclerAdapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    fun notifyHorizontalScroll(left: Int, right: Int) {
        updateVisibleRegion(left, visibleRegion.top, right, visibleRegion.bottom)
    }

    private fun updateVisibleRegion(left: Int, top: Int, right: Int, bottom: Int) {
        visibleRegion.set(left, top, right, bottom)
        log { "visibleRegion = $visibleRegion" }
        reLayout()
    }

    private fun reSendFrameCallBack() {
        choreographer.removeFrameCallback(frameCallback)
        choreographer.postFrameCallback(frameCallback)
    }

    private val adapterDataObserver = object : AbsPlaneRecyclerAdapter.AdapterDataObserver() {
        override fun onChanged() {
            if (isShown) {
                if (!dataSetChanged) {
                    dataSetChanged = true
                    reSendFrameCallBack()
                }
            } else {
                post {
                    updateViews()
                }
            }
        }

        override fun onItemChanged(ids: List<Long>) {
            handleItemChanged(ids)
        }

        override fun onItemMoved(ids: List<Long>) {
            handleItemMoved(ids)
        }

        override fun onItemInserted(ids: List<Long>) {
            insertElements(ids)
        }

        override fun onItemRemoved(ids: List<Long>) {
            handleItemRemoved(ids)
        }

        override fun handleHorizontalTouchEvent(handle: Boolean) {
            handleHorizontalEvent = handle
        }
    }

    private fun updateViews() {
        log { "updateViews()" }
        val adapter = recyclerAdapter ?: return
        trackContainerInner.removeAllViews()
        insertElements(adapter.getElementIds())
    }

    private fun insertElements(ids: List<Long>, fullUpdate: Boolean = true, checkVisible: Boolean = true) {
        val adapter = recyclerAdapter ?: return
        var minHeight = 0
        for (id in ids) {
            val border = adapter.getViewBorder(id, trackContainerInner)
            allBorders[id] = border
            // 计算累计的高度
            if (border.bottom() > minHeight) {
                minHeight = border.bottom()
            }
            if (checkVisible && !checkViewVisible(border)) {
                continue
            }
            val viewType = adapter.getElementType(id)
            val holder = getHolderFromCache(viewType) ?: adapter.onCreateElementHolder(trackContainerInner, viewType)
            updateHolder(adapter, holder, id)
            trackContainerInner.addView(holder.itemView)
            visibleHolders[id] = holder
            allHolders[id] = holder
        }
        val view = View(context)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 1)
        params.topMargin = minHeight - 1
        trackContainerInner.addView(view, params)
    }

    private fun handleItemRemoved(ids: List<Long>) {
        val adapter = recyclerAdapter ?: return
        for (id in ids) {
            allBorders.remove(id)
            allHolders.remove(id)
            recycleItem(adapter, id)
        }
    }

    private fun handleItemChanged(ids: List<Long>) {
        val becomeVisibleList = mutableListOf<Long>()
        val adapter = recyclerAdapter ?: return
        for (id in ids) {
            val holder = visibleHolders[id]
            if (holder != null) {
                if (checkViewVisible(holder.itemBorder)) {
                    updateHolder(adapter, holder, id)
                } else {
                    recycleItem(adapter, id)
                }
            } else {
                // 不可见或者之前没添加的item发生了变化
                val border = adapter.getViewBorder(id, trackContainerInner)
                allBorders[id] = border
                if (checkViewVisible(border)) {
                    // 如果view还没添加，现在添加
                    if (visibleHolders[id] == null) {
                        becomeVisibleList.add(id)
                    }
                }
            }
        }
        insertElements(becomeVisibleList)
    }

    private fun handleItemMoved(ids: List<Long>) {
        handleItemChanged(ids)
        ids.lastOrNull()?.let {
            allHolders[it]?.let { holder ->
                scrollIfNeed(holder.itemBorder)
            }
        }
        handleHorizontalEvent = false
    }

    private fun getHolderFromCache(viewType: Int): ViewHolder? {
        val cache = cacheHolders[viewType] ?: LinkedList<ViewHolder>().apply {
            cacheHolders[viewType] = this
        }
        return cache.poll()
    }

    private fun scrollIfNeed(border: ViewBorder) {
        if (border.y < scrollY) {
            scrollTo(0, border.y)
        } else if (border.bottom() > scrollY + height) {
            scrollTo(0, border.bottom())
        }
    }

    /**
     * 检查一个view是否需要画出来
     */
    private fun checkViewVisible(border: ViewBorder): Boolean {
        val result = visibleRegion.overlap(border.toRect())
        log { "border = $border, visibleRegion = $visibleRegion, visible = $result" }
        return result
    }

    private fun updateHolder(adapter: AbsPlaneRecyclerAdapter<ViewHolder>,
                             holder: ViewHolder,
                             id: Long) {
        adapter.onBindElementHolder(holder, id)
        val params = holder.itemView.layoutParams as MarginLayoutParams?
            ?: FrameLayout.LayoutParams(holder.itemBorder.width, holder.itemBorder.height)
        params.setMargins(holder.itemBorder.x, holder.itemBorder.y, 0, 0)
        params.width = holder.itemBorder.width
        params.height = holder.itemBorder.height
        holder.itemView.layoutParams = params
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(handleHorizontalEvent)
        return super.onInterceptTouchEvent(ev)
    }

    // 如果该方法被回调，说明子view没有处理事件
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        listener?.onEmptyClick()
        return super.onTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        visibleRegion.top = scrollY
        visibleRegion.bottom = scrollY + height
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        trackContainerInner = findViewById<FrameLayout>(R.id.track_container_inner)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        updateVisibleRegion(visibleRegion.left, t, visibleRegion.right, t + height)
    }

    private fun reLayout() {
        val start = System.currentTimeMillis()
        val adapter = recyclerAdapter ?: return
        val becomeVisibleList = mutableListOf<Long>()

        for (entry in allBorders) {
            val border = entry.value
            border.setupWithParent(trackContainerInner)
            if (checkViewVisible(border)) {
                // 可见
                // 如果view还没添加，现在添加
                if (visibleHolders[entry.key] == null) {
                    becomeVisibleList.add(entry.key)
                }
            } else {
                recycleItem(adapter, entry.key)
            }
        }

        insertElements(becomeVisibleList, false)
        log { "reLayout took ${System.currentTimeMillis() - start}" }
    }

    private fun recycleItem(adapter: AbsPlaneRecyclerAdapter<ViewHolder>, id: Long) {
        visibleHolders.remove(id)?.let {
            val viewType = adapter.getElementType(id)
            val cache = cacheHolders[viewType] ?: LinkedList<ViewHolder>().apply {
                cacheHolders[viewType] = this
            }
            cache.add(it)
            trackContainerInner.removeView(it.itemView)
        }
    }

    fun handleHorizontalTouchEvent(value: Boolean) {
        handleHorizontalEvent = value
    }

    fun setEventListener(listener: EventListener) {
        this.listener = listener
    }

    fun getContentHeight(): Int {
        var height = 0
        for (i in 0 until childCount) {
            if (getChildAt(i).bottom > height) {
                height = getChildAt(i).bottom
            }
        }
        return height
    }

    interface EventListener {
        fun onEmptyClick()
    }

    class ViewBorder {
        var x = 0
        var y = 0
        var width = 0
        var height = 0

        fun right(): Int {
            return x + width
        }

        fun bottom(): Int {
            return y + height
        }

        fun toRect(): Rect {
            return Rect(x, y, right(), bottom())
        }

        fun setupWithParent(parent: View) {
            if (width == FrameLayout.LayoutParams.MATCH_PARENT) {
                width = parent.width - x
            }
            if (height == FrameLayout.LayoutParams.MATCH_PARENT) {
                height = parent.height - y
            }
        }

        override fun toString(): String {
            return "x = $x, y = $y, width = $width, height = $height"
        }
    }

    abstract class ViewHolder(val itemView: View){
        val itemBorder = ViewBorder()
    }
}

fun View.r(): Float {
    return right + translationX
}

fun View.l() : Float{
    return left + translationX
}

/**
 * 判断两个矩形是否有重叠
 */
fun Rect.overlap(rect: Rect): Boolean {
    return !(right <= rect.left ||
            bottom <= rect.top ||
            left >= rect.right ||
            top >= rect.bottom)
}
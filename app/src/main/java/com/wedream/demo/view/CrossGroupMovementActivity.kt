package com.wedream.demo.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.multitrack.base.ElementView
import com.wedream.demo.view.multitrack.overlap

class CrossGroupMovementActivity : AppCompatActivity() {

    lateinit var container: FrameLayout
    private lateinit var group1: FrameLayout
    lateinit var group2: FrameLayout

    private val groupRectMap = mutableMapOf<View, ViewInfo>()
    private val group1RectMap = mutableMapOf<Int, ViewInfo>()
    private val group2RectMap = mutableMapOf<Int, ViewInfo>()

    private var movingStart = false
    private var canPlace = false
    private var originRect = Rect()
    private var newGroupTipsView: View? = null
    // 正在操作的group
    private var operatingGroup: ViewGroup? = null

    private var elementEventListener = object : ElementView.ElementEventListener {

        override fun onActionDown(view: ElementView) {
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                val info = groupRectMap[view] ?: return
                info.rect.offset(deltaX, deltaY)
                handleAdsorption(info)
                log { "onMove rect = ${info.rect}" }
                layoutView(view, info.rect)
                canPlace = !checkOverlap(view, info)
                if (!canPlace) {
                    view.alpha = 0.5f
                } else {
                    view.alpha = 1.0f
                }
            }
        }

        override fun onActionUp(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                movingStart = false
                view.z -= 10
                (view.parent as ViewGroup).z -= 10
                if (!canPlace) {
                    view.alpha = 1.0f
                    groupRectMap[view]?.rect?.set(originRect)
                    layoutView(view, originRect)
                } else {
                    // 如果可以放置，先找出移到了哪个容器里面
                    val info = groupRectMap[view] ?: return
//                    log { "rect = ${info.rect}" }
                    val id = view.tag as Int
                    val rect1 = Rect(group1.left, group1.top, group1.right, group1.bottom)
                    val globalRect = info.getGlobalRect()
                    if (rect1.contains(globalRect)) {
                        log { "在 group1 中" }
                        if (!group1RectMap.containsKey(id)) {
                            group2RectMap.remove(id)?.let {
                                group1RectMap[id] = it
                                it.rect.set(globalRect)
                                it.rect.offset(0, -group1.top)
                                it.offset = group1.top
                                fullUpdate()
                            }
                        }
                    }
                    val rect2 = Rect(group2.left, group2.top, group2.right, group2.bottom)
                    if (rect2.contains(globalRect)) {
                        log { "在 group2 中" }
                        if (!group2RectMap.containsKey(id)) {
                            group1RectMap.remove(id)?.let {
                                group2RectMap[id] = it
                                it.rect.set(globalRect)
                                it.rect.offset(0, -group2.top)
                                it.offset = group2.top
                                fullUpdate()
                            }
                        }
                    }
                }
            }
        }

        override fun onLongPress(view: ElementView) {
            if (!movingStart) {
                operatingGroup = view.parent as ViewGroup
                movingStart = true
                view.z += 10
                (view.parent as ViewGroup).z += 10
                originRect.set(groupRectMap[view]?.rect!!)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_group_move)
        container = findViewById(R.id.container)
        group1 = findViewById(R.id.group1)
        group2 = findViewById(R.id.group2)
        group1.tag = 1
        group2.tag = 2
        group1.post {
            initRects()
        }
    }

    private fun initRects() {
        var offset = 0
        val width = 150
        for (i in 1..5) {
            val rect = Rect(offset, 0, offset + width, group1.height)
            group1RectMap[i] = ViewInfo(rect, group1.top)
            offset += width
        }
        offset = 0
        for (i in 6..10) {
            val rect = Rect(offset, 0, offset + width, group2.height)
            group2RectMap[i] = ViewInfo(rect, group2.top)
            offset += width
        }
        fullUpdate()
    }

    private fun fullUpdate() {
        groupRectMap.clear()
        addViews(group1, group1RectMap)
        addViews(group2, group2RectMap)
    }

    private fun addViews(group: ViewGroup, map: MutableMap<Int, ViewInfo>) {
        group.removeAllViews()
        for (e in map) {
            val view = SliderView(this)
            view.setElementEventListener(elementEventListener)
            when (e.key % 3) {
                0 -> {
                    view.setBackgroundResource(R.color.color_green)
                }
                1 -> {
                    view.setBackgroundResource(R.color.color_blue)
                }
                else -> {
                    view.setBackgroundResource(R.color.red_dot_color)
                }
            }
            groupRectMap[view] = e.value
            view.tag = e.key
            val params = FrameLayout.LayoutParams(e.value.rect.width(), e.value.rect.height())
            params.topMargin = e.value.rect.top
            params.marginStart = e.value.rect.left
            group.addView(view, params)
        }
    }

    private fun checkOverlap(view: View, info: ViewInfo): Boolean {
        val globalRect = info.getGlobalRect()
        for (e in groupRectMap) {
            if (e.key == view) {
                continue
            }
            val r = e.value.getGlobalRect()
            if (globalRect.overlap(r)) {
                return true
            }
        }
        return false
    }

    private fun handleAdsorption(info: ViewInfo) {
        val rect = Rect(info.rect)
        // 转换成全局坐标
        rect.offset(0, info.offset)
        restrictWithin(rect, container)
        adsorbContainerBorder(rect, group1)
        adsorbContainerBorder(rect, group2)
        // 转回容器内坐标
        rect.offset(0, -info.offset)
        info.rect.set(rect)
    }

    private fun adsorbContainerBorder(rect: Rect, viewGroup: ViewGroup) {
        val centerX = rect.centerX()
        val centerY = rect.centerY()
        val groupRect = Rect(viewGroup.left, viewGroup.top, viewGroup.right, viewGroup.bottom)
        if (groupRect.contains(centerX, centerY)) {
            // 在这个容器里面
            if (rect.left < groupRect.left && rect.right > groupRect.left) {
                rect.offset(groupRect.left - rect.left, 0)
            }
//            if (rect.top < groupRect.top && rect.bottom > groupRect.top) {
//                rect.offset(0, groupRect.top - rect.top)
//            }
            if (rect.left < groupRect.right && rect.right > groupRect.right) {
                rect.offset(groupRect.right - rect.right, 0)
            }
//            if (rect.top < groupRect.bottom && rect.bottom > groupRect.bottom) {
//                rect.offset(0, groupRect.bottom - rect.bottom)
//            }
            log { "centerY = $centerY, groupRect = $groupRect" }
            operatingGroup?.let {
                if (centerY > groupRect.top && centerY < groupRect.top + groupRect.height() * 0.15) {
                    showNewGroupTipsView(groupRect.top - it.top - 20 / 2)
                } else if (centerY < groupRect.bottom && centerY > groupRect.top + groupRect.height() * 0.85) {
                    showNewGroupTipsView(groupRect.top - it.top + groupRect.height() - 20 / 2)
                } else {
                    hideNewGroupTipsView(viewGroup)
                }
            }
        } else {
            hideNewGroupTipsView(viewGroup)
        }
    }

    private fun restrictWithin(rect: Rect, viewGroup: ViewGroup) {
        val groupRect = Rect(viewGroup.left, viewGroup.top, viewGroup.right, viewGroup.bottom)
        // 在这个容器里面
        if (rect.left < groupRect.left) {
            rect.offset(groupRect.left - rect.left, 0)
        }
        if (rect.top < groupRect.top) {
            rect.offset(0, groupRect.top - rect.top)
        }
        if (rect.right > groupRect.right) {
            rect.offset(groupRect.right - rect.right, 0)
        }
        if (rect.bottom > groupRect.bottom) {
            rect.offset(0, groupRect.bottom - rect.bottom)
        }
    }

    private fun showNewGroupTipsView(offset: Int) {
        if (newGroupTipsView?.isAttachedToWindow == true) {
            return
        }
        val view = newGroupTipsView ?: View(this).apply {
            newGroupTipsView = this
        }
        view.setBackgroundResource(R.color.color_yellow)
        view.z = 10f
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 20)
        params.topMargin = offset
        operatingGroup?.addView(view, params)
    }

    private fun hideNewGroupTipsView(targetGroup: ViewGroup) {
        log { "hideNewGroupTipsView top group = ${targetGroup.tag}" }
        targetGroup.removeView(newGroupTipsView)
    }

    private fun layoutView(view: View, rect: Rect) {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.marginStart = rect.left
        params.topMargin = rect.top
        view.layoutParams = params
    }

    class ViewInfo(val rect: Rect, var offset: Int) {
        fun getGlobalRect(): Rect {
            val r = Rect(rect)
            r.offset(0, offset)
            return r
        }
    }
}
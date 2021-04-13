package com.wedream.demo.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.multitrack.SliderView
import com.wedream.demo.view.multitrack.base.ElementView
import com.wedream.demo.view.multitrack.overlap
import kotlin.math.round

class CrossGroupMovementActivity : AppCompatActivity() {

    lateinit var container: FrameLayout

    private val totalRectMap = mutableMapOf<View, ViewInfo>()
    private val groups = mutableListOf<MutableMap<Int, ViewInfo>>()
    private val totalGroupMap = mutableMapOf<Int, ViewGroup>()

    private var movingStart = false
    private var canPlace = false
    private var originRect = Rect()
    private var newGroupTipsView: View? = null

    // 正在操作的group
    private var operatingGroup: ViewGroup? = null
    private var operatingView: View? = null

    private val GROUP_HEIGHT = AndroidUtils.dip2pix(100)

    private var elementEventListener = object : ElementView.ElementEventListener {

        override fun onActionDown(view: ElementView) {
            operatingView = view
        }

        override fun onMove(view: ElementView, deltaX: Int, deltaY: Int) {
            if (movingStart) {
                val info = totalRectMap[view] ?: return
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
                if (newGroupTipsView?.isAttachedToWindow == true) {
                    operatingGroup?.let {
                        hideNewGroupTipsView(it)
                    }
                    totalRectMap[operatingView]?.let {
                        log { "newGroup at ${it.rect}" }
                        checkNewGroupPos(it.getGlobalRect())
                    }
                } else if (!canPlace) {
                    view.alpha = 1.0f
                    totalRectMap[view]?.rect?.set(originRect)
                    layoutView(view, originRect)
                } else {
                    // 如果可以放置，先找出移到了哪个容器里面
                    val info = totalRectMap[view] ?: return
//                    log { "rect = ${info.rect}" }
                    val id = view.tag as Int
                    for (i in groups.indices) {
                        val group = totalGroupMap[i] ?: continue
                        val rect = Rect(group.left, group.top, group.right, group.bottom)
                        val globalRect = info.getGlobalRect()
                        if (rect.contains(globalRect)) {
                            log { "在 group1 中" }
                            if (groups[i].containsKey(id)) {
                                // 在其他groups中移除
                                for (j in groups.indices) {
                                    if (groups[j].containsKey(id)) {
                                        groups[j].remove(id)?.let {
                                            groups[i][id] = it
                                            it.rect.set(globalRect)
                                            it.rect.offset(0, -group.top)
                                            it.offset = group.top
                                            fullUpdate()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            operatingView = null
        }

        override fun onLongPress(view: ElementView) {
            if (!movingStart) {
                operatingGroup = view.parent as ViewGroup
                movingStart = true
                view.z += 10
                (view.parent as ViewGroup).z += 10
                originRect.set(totalRectMap[view]?.rect!!)
            }
        }

        override fun onClick(view: ElementView) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cross_group_move)
        container = findViewById(R.id.container)
        container.post {
            initRects()
        }
    }

    private fun initRects() {
        var offset = 0
        val width = 150
        val m1 = mutableMapOf<Int, ViewInfo>()
        for (i in 1..5) {
            val rect = Rect(offset, 0, offset + width, GROUP_HEIGHT)
            m1[i] = ViewInfo(rect, 0)
            offset += width
        }
        groups.add(m1)

        val m2 = mutableMapOf<Int, ViewInfo>()
        offset = 0
        for (i in 6..10) {
            val rect = Rect(offset, 0, offset + width, GROUP_HEIGHT)
            m2[i] = ViewInfo(rect, GROUP_HEIGHT)
            offset += width
        }
        groups.add(m2)
        fullUpdate()
    }

    private fun fullUpdate() {
        totalRectMap.clear()
        var offset = 0
        for ((i, m) in groups.withIndex()) {
            val group = FrameLayout(this)
            group.clipChildren = false
            group.setBackgroundResource(R.drawable.simple_border)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, GROUP_HEIGHT)
            params.topMargin = offset
            container.addView(group, params)
            totalGroupMap[i] = group
            addViews(group, m)
            offset += GROUP_HEIGHT
        }
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
            totalRectMap[view] = e.value
            view.tag = e.key
            val params = FrameLayout.LayoutParams(e.value.rect.width(), e.value.rect.height())
            params.topMargin = e.value.rect.top
            params.marginStart = e.value.rect.left
            group.addView(view, params)
        }
    }

    private fun checkOverlap(view: View, info: ViewInfo): Boolean {
        val globalRect = info.getGlobalRect()
        for (e in totalRectMap) {
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
        for (e in totalGroupMap) {
            adsorbContainerBorder(rect, e.value)
        }
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

    private fun checkNewGroupPos(rect: Rect): Int {
        val centerY = rect.centerY()
        val height = rect.height()
        val index = round(centerY * 1.0 / height)
        log { "index = $index" }
        return index.toInt()
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
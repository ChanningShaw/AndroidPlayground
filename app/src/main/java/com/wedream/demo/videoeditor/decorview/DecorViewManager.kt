package com.wedream.demo.videoeditor.decorview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.videoeditor.timeline.utils.TimelineUtils
import com.wedream.demo.videoeditor.timeline.widget.AbsTouchListener

class DecorViewManager(
    private val editorGovernor: EditorGovernor,
    private val timelineViewModel: TimelineViewModel,
    private val context: Context
) {
    private val leftDecorView = View(context)
    private val rightDecorView = View(context)

    private var lastSelectedId = Constants.INVALID_ID

    private val decorTouchListener = object : AbsTouchListener(context, false) {

        private var originStart = 0.0
        private var originEnd = 0.0

        override fun onActionDown(v: View, x: Int, y: Int) {
            editorGovernor.getAsset(lastSelectedId)?.let {
                originStart = it.getClipStart()
                originEnd = it.getClipEnd()
            }
            v.parent?.requestDisallowInterceptTouchEvent(true)
        }

        override fun onMoveStart(v: View, deltaX: Int, deltaY: Int) {
        }

        override fun onMoving(v: View, deltaX: Int, deltaY: Int) {
            log { "deltaX = $deltaX" }
            val isLeft = v === leftDecorView
            editorGovernor.getAsset(lastSelectedId)?.let {
                if (isLeft) {
                    it.setClipStart(originStart + TimelineUtils.width2time(deltaX, timelineViewModel.getScale()))
                } else {
                    it.setClipEnd(originEnd + TimelineUtils.width2time(deltaX, timelineViewModel.getScale()))
                }
            }
        }

        override fun onMoveStop(v: View, deltaX: Int, deltaY: Int) {
        }

        override fun onActionUp(v: View, deltaX: Int, deltaY: Int) {
            v.parent?.requestDisallowInterceptTouchEvent(false)
        }

        override fun onActionCancel(v: View) {
        }

        override fun onLongPress(v: View) {
        }

        override fun onClick(v: View) {
        }
    }

    init {
        initDecor()
    }

    private fun initDecor() {
        leftDecorView.setBackgroundResource(R.color.color_white)
        rightDecorView.setBackgroundResource(R.color.color_white)
        leftDecorView.layoutParams = FrameLayout.LayoutParams(50, FrameLayout.LayoutParams.MATCH_PARENT)
        rightDecorView.layoutParams = FrameLayout.LayoutParams(50, FrameLayout.LayoutParams.MATCH_PARENT)
        leftDecorView.setOnTouchListener(decorTouchListener)
        rightDecorView.setOnTouchListener(decorTouchListener)
    }

    fun handleSelect(selectId: Long, parent: ViewGroup) {
        if (selectId != Constants.INVALID_ID) {
            if (selectId != lastSelectedId) {
                showDecor(selectId, parent)
            } else {
                updateDecor(selectId)
            }
        } else if (selectId == Constants.INVALID_ID) {
            removeDecor()
        }
        lastSelectedId = selectId
    }

    private fun showDecor(id: Long, parent: ViewGroup) {
        removeDecor()
        parent.addView(leftDecorView)
        parent.addView(rightDecorView)
        updateDecor(id)
    }

    private fun updateDecor(id: Long) {
        timelineViewModel.getSegment(id)?.let {
            leftDecorView.translationX = DeviceParams.SCREEN_WIDTH * 0.5f + it.left.toFloat() - 50
            rightDecorView.translationX = DeviceParams.SCREEN_WIDTH * 0.5f + it.right.toFloat()
        }
    }

    private fun removeDecor() {
        if (leftDecorView.parent != null) {
            (leftDecorView.parent as ViewGroup).removeView(leftDecorView)
        }
        if (rightDecorView.parent != null) {
            (rightDecorView.parent as ViewGroup).removeView(rightDecorView)
        }
    }
}
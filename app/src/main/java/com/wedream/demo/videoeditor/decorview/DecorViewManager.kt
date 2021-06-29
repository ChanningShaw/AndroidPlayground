package com.wedream.demo.videoeditor.decorview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.videoeditor.const.Constants
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class DecorViewManager(
    private val timelineViewModel: TimelineViewModel,
    private val context: Context
) {
    private val leftDecorView = View(context)
    private val rightDecorView = View(context)

    private var lastSelectedId = Constants.INVALID_ID

    init {
        initDecor()
    }

    private fun initDecor() {
        leftDecorView.setBackgroundResource(R.color.color_white)
        rightDecorView.setBackgroundResource(R.color.color_white)
        leftDecorView.layoutParams = FrameLayout.LayoutParams(50, FrameLayout.LayoutParams.MATCH_PARENT)
        rightDecorView.layoutParams = FrameLayout.LayoutParams(50, FrameLayout.LayoutParams.MATCH_PARENT)
    }

    fun handleSelect(selectId: Long, parent: ViewGroup) {
        if (selectId != Constants.INVALID_ID) {
            lastSelectedId = selectId
            showDecor(parent)
        } else if (lastSelectedId != Constants.INVALID_ID) {
            removeDecor()
        }
    }

    private fun showDecor(parent: ViewGroup) {
        timelineViewModel.getSegment(lastSelectedId)?.let {
            removeDecor()
            parent.addView(leftDecorView)
            parent.addView(rightDecorView)
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
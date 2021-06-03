package com.wedream.demo.videoeditor.timeline.widget

import android.content.Context
import android.view.View
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.timeline.data.Segment

class SegmentTouchListener(context: Context) : AbsTouchListener(context, true) {
    override fun onActionDown(v: View, x: Int, y: Int) {
    }

    override fun onMoveStart(v: View, deltaX: Int, deltaY: Int) {
    }

    override fun onMoving(v: View, deltaX: Int, deltaY: Int) {
    }

    override fun onMoveStop(v: View, deltaX: Int, deltaY: Int) {
    }

    override fun onActionUp(v: View, deltaX: Int, deltaY: Int) {
    }

    override fun onActionCancel(v: View) {
    }

    override fun onLongPress(v: View) {
    }

    override fun onClick(v: View) {
        val segment = v.getTag(R.id.view_tag_segment) as? Segment ?: return
        log { "$segment onClick" }
    }
}
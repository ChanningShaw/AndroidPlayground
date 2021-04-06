package com.wedream.demo.view.newtips

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.wedream.demo.R
import com.wedream.demo.app.MySharePreference
import com.wedream.demo.util.AndroidUtils.getVersionCode

/**
 * 用于添加新功能提示的view，目前支持两种提示方式：红点和new图标
 */
class NewTipsView(context: Context,
                  var spKey: String,
                  var type: TipType) : View(context) {
    companion object {
        private const val DEFAULT_DOT_RADIUS = 10
    }

    private var sp = MySharePreference.default()
    private var added = false
    val paint = Paint()
    var targetView: View? = null
    private var currentVersion: Int = getVersionCode()

    fun setKey(key: String) {
        spKey = key
        visibility = if (sp.getInt(spKey, Int.MAX_VALUE) <= currentVersion) {
            GONE
        } else {
            VISIBLE
        }
    }

    fun bind(target: View, marginTop: Int, marginRight: Int, parentKey: String? = null) {
        if (added) {
            return
        }
        val parent = target.parent
        if (parent is RelativeLayout) {
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params.topMargin = marginTop
            params.rightMargin = marginRight
            parent.addView(this, params)
        } else if (parent is FrameLayout) {
            val params = FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            params.gravity = Gravity.END or Gravity.TOP
            params.topMargin = marginTop
            params.rightMargin = marginRight
            parent.addView(this, params)
        }
        targetView = target
        added = true
        NewTipsTree.addNode(parentKey, this)
    }

    fun unbind(){
        if (!added) {
            return
        }
        added = false
        update()
    }

    fun update(updateTree: Boolean = true) {
        sp.putInt(spKey, currentVersion)
        if (updateTree) {
            NewTipsTree.update()
        }
    }

    fun unBindAndResetKey() {
        update()
        sp.putInt(spKey, Int.MAX_VALUE)
    }

    fun visible(): Boolean {
        return sp.getInt(spKey, 0) < currentVersion
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.isAntiAlias = true
        when (type) {
            TipType.TYPE_DOT -> {
                paint.color = context.resources.getColor(R.color.red_dot_color)
                paint.style = Paint.Style.FILL
                paint.strokeWidth = 10f
                canvas?.drawCircle(DEFAULT_DOT_RADIUS.toFloat(), DEFAULT_DOT_RADIUS.toFloat(), DEFAULT_DOT_RADIUS.toFloat(), paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (type == TipType.TYPE_DOT) {
            setMeasuredDimension(DEFAULT_DOT_RADIUS * 2, DEFAULT_DOT_RADIUS * 2)
        }
    }

    enum class TipType {
        TYPE_DOT, TYPE_BRAND
    }

    override fun toString(): String {
        return spKey
    }
}

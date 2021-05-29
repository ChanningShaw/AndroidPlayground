package com.wedream.demo.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.util.LogUtils.log


class ScaleViewActivity : AppCompatActivity() {

    private lateinit var scaleView: ScaleView
    private lateinit var scrollView: HorizontalScrollView
    private lateinit var contentView: FrameLayout
    private var currentScale = 1.0f
    private var laseScale = 1.0f
    private var scalingStartScrollX = 0
    private var increase = false
    private var changed = false

    private val rectList = mutableListOf<Pair<Rect, View>>()

    private val choreographer = Choreographer.getInstance()

    private val frameCallback = Choreographer.FrameCallback {
        if (!changed) {
            return@FrameCallback
        }
        changed = false
        layoutViews(currentScale)
        val pos = scalingStartScrollX * currentScale
        log { "pos = $pos" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scale_view)
        scaleView = findViewById(R.id.scaleView)
        scrollView = findViewById(R.id.scrollView)
        contentView = findViewById(R.id.content_view)
        addViews()
        layoutViews(1.0f)
        scaleView.setOnScaleListener(object : ScaleView.OnScaleListener {
            override fun onScaling(scale: Float) {
                log { "scale = $scale" }
                currentScale = scale
                if (!changed) {
                    changed = true
                    choreographer.removeFrameCallback(frameCallback)
                    choreographer.postFrameCallback(frameCallback)
                }
                if (laseScale > currentScale) {
                    if (increase) {
                        log { "monotonicity change to down!" }
                    }
                    increase = false
                } else if (laseScale < currentScale) {
                    if (!increase) {
                        log { "monotonicity change to up!" }
                    }
                    increase = true
                }
                laseScale = currentScale
            }

            override fun onScaleStart(scale: Float) {
                scalingStartScrollX = scrollView.scrollX
                laseScale = scale
            }

            override fun onScaleEnd(scale: Float) {
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addViews() {
        var offset = 0
        val width = 150
        for (i in 1..50) {
            val view =
                LayoutInflater.from(this).inflate(R.layout.text_simple, null, false) as TextView
            view.setOnTouchListener(
                MyTouchListener(
                    view,
                    scrollView,
                    scrollView,
                    DeviceParams.SCREEN_WIDTH
                )
            )
            view.text = i.toString()
            when (i % 3) {
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
            val rect = Rect(offset, 0, offset + width, -2)
            rectList.add(Pair(rect, view))
            val params = FrameLayout.LayoutParams(rect.width(), rect.height())
            contentView.addView(view, params)
            offset += width
        }
    }

    private fun layoutViews(scale: Float) {
        var offset = 0
        for (i in rectList.indices) {
            val p = rectList[i]
            val rect = p.first
            val view = p.second
            val with = (rect.width() * scale).toInt()
            view.layoutParams.let {
                it as ViewGroup.MarginLayoutParams
                it.marginStart = offset
                if (i == rectList.size - 1) {
                    it.marginEnd = DeviceParams.SCREEN_WIDTH / 2
                }
                it.width = with
                it.height = rect.height()
                view.layoutParams = it
            }
            offset += with
        }
    }

    class MyTouchListener(
        val target: View,
        val parent: ViewGroup,
        val scrollView: HorizontalScrollView,
        val screenWidth: Int
    ) : View.OnTouchListener, View.OnLayoutChangeListener {

        var downX = 0f
        var lastX = 0f
        var enterModeX = 0f
        var directionRemain = true
        var dragMode = NORMAL_MODE

        companion object {
            const val NORMAL_MODE = 0
            const val INERTIA_SHRINK_MODE = 1
            const val INERTIA_EXPAND_MODE = 2

            const val OFFSET = 50
        }

        private val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (!directionRemain) {
                    log { "break directionRemain" }
                    return
                }
                val offset = if (dragMode == INERTIA_SHRINK_MODE) -OFFSET else OFFSET
                val params = target.layoutParams
                params.width += offset
                scrollView.scrollBy(offset, 0)
                target.layoutParams = params
                log { "INERTIA : mode = $dragMode" }
                sendEmptyMessageDelayed(0, 50)
            }
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    log { "ACTION_DOWN" }
                    parent.requestDisallowInterceptTouchEvent(true)
                    v.addOnLayoutChangeListener(this)
                    downX = event.rawX
                    lastX = downX
                    return true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    log { "ACTION_UP" }
                    parent.requestDisallowInterceptTouchEvent(false)
                    v.removeOnLayoutChangeListener(this)
                    handler.removeMessages(0)
                    dragMode = NORMAL_MODE
                    directionRemain = true
                    // 重置margin值
                    val params = target.layoutParams
                    params as ViewGroup.MarginLayoutParams
                    params.rightMargin = screenWidth / 2
                    target.layoutParams = params
                    target.post {
                        scrollView.smoothScrollTo(target.right, 0)
                    }
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.rawX
                    val deltaX = (x - lastX).toInt()
                    if (dragMode == INERTIA_SHRINK_MODE) {
                        directionRemain = x < enterModeX + 15
                        if (!directionRemain) {
                            handleDragMove(v, deltaX)
                        }
                    } else if (dragMode == INERTIA_EXPAND_MODE) {
                        directionRemain = x > enterModeX - 15
                        if (!directionRemain) {
                            handleDragMove(v, deltaX)
                        }
                    } else {
                        handleDragMove(v, deltaX)
                    }
                    lastX = x
                    return true
                }
                else -> {
                    return false
                }
            }
        }

        private fun handleDragMove(view: View, deltaX: Int) {
            handler.removeMessages(0)
            val params = view.layoutParams
            if (deltaX > 0) {
                log { "EXPAND" }
                params.width += deltaX
            } else {
                log { "SHRINK" }
                params as ViewGroup.MarginLayoutParams
                params.rightMargin -= deltaX
                params.width += deltaX
            }
            view.layoutParams = params
        }

        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            val location1 = IntArray(2)
            v.getLocationInWindow(location1) //获取在当前窗口内的绝对坐标
            val absRight = location1[0] + right - left
            log { "absRight = $absRight" }
            when {
                absRight <= screenWidth * 0.25 -> {
                    if (dragMode != INERTIA_SHRINK_MODE) {
                        enterModeX = lastX
                        directionRemain = true
                        handler.sendEmptyMessage(0)
                        dragMode = INERTIA_SHRINK_MODE
                        log { "dragMode change to $dragMode" }
                    }
                }
                absRight >= screenWidth * 0.75 -> {
                    if (dragMode != INERTIA_EXPAND_MODE) {
                        enterModeX = lastX
                        directionRemain = true
                        handler.sendEmptyMessage(0)
                        dragMode = INERTIA_EXPAND_MODE
                        log { "dragMode change to $dragMode" }
                    }
                }
                else -> {
                    if (dragMode != NORMAL_MODE) {
                        dragMode = NORMAL_MODE
                        directionRemain = false
                        enterModeX = 0f
                        handler.removeMessages(0)
                        log { "dragMode change to $dragMode" }
                    }
                }
            }

        }
    }
}
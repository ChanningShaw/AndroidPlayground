package com.wedream.demo.algo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import com.wedream.demo.R
import com.wedream.demo.planegeometry.reset
import com.wedream.demo.algo.playground.VisualizationView
import com.wedream.demo.algo.action.*
import com.wedream.demo.algo.action.AlgorithmAction.Companion.DEFAULT_DELAY_TIME

class SortVisualizationView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    VisualizationView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var data = emptyArray<Int>()
    private var tempData = emptyArray<Int>()
    private var elements = arrayListOf<RectF>()
    private var tempElements = arrayListOf<RectF>()
    private var paintDefault = Paint()
    private var paintPivot = Paint()
    private var paintSelected1 = Paint()
    private var paintSelected2 = Paint()
    private var textPaint = Paint()
    private var pos1 = Pair(0, 0)
    private var pos2 = Pair(0, 0)
    private var text = ""
    private var pivot = -1

    companion object {
        const val EL_GAP = 5
        const val DIVIDE_GAP = 20
        const val UPPER_INDEX = 0
        const val LOWER_INDEX = 1
    }

    init {
        paintDefault.style = Paint.Style.FILL_AND_STROKE
        paintDefault.color = context.resources.getColor(R.color.color_green)
        paintSelected1.color = context.resources.getColor(R.color.color_blue)
        paintSelected2.color = context.resources.getColor(R.color.color_violet)
        textPaint.color = Color.BLACK
        paintPivot.color = Color.YELLOW
        textPaint.textSize = 60f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { c ->
            for (i in elements.indices) {
                when (Pair(UPPER_INDEX, i)) {
                    pos1 -> {
                        c.drawRect(elements[i], paintSelected1)
                    }
                    pos2 -> {
                        c.drawRect(elements[i], paintSelected2)
                    }
                    else -> {
                        if (i == pivot) {
                            c.drawRect(elements[i], paintPivot)
                        } else {
                            c.drawRect(elements[i], paintDefault)
                        }
                    }
                }
            }
            for (i in tempElements.indices) {
                when (Pair(LOWER_INDEX, i)) {
                    pos1 -> {
                        c.drawRect(tempElements[i], paintSelected1)
                    }
                    pos2 -> {
                        c.drawRect(tempElements[i], paintSelected2)
                    }
                    else -> {
                        c.drawRect(tempElements[i], paintDefault)
                    }
                }
            }
            if (text.isNotEmpty()) {
                val textWidth = textPaint.measureText(text)
                c.drawText(text, (width - textWidth) * 0.5f, getElementHeight() * 0.5f, textPaint)
            }
        }
    }

    /**
     * 获取竖条的宽度，不包括间隔
     */
    private fun getElementWidth(): Float {
        return (width.toFloat() - (data.size + 1) * EL_GAP) / data.size
    }

    private fun getElementHeight(): Float {
        return (height - DIVIDE_GAP) * 0.5f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            invalidate()
        }
        return true
    }

    fun reset(data: Array<Int>) {
        this.data = data
        pos1 = Pair(-1, -1)
        pos2 = Pair(-1, -1)
        pivot = -1
        text = ""
        initElements()
        invalidate()
    }

    private fun initElements() {
        elements.clear()
        val viewHeight = getElementHeight()
        val w = getElementWidth()
        val max = data.maxOrNull() ?: return
        var left = 0f
        for (i in data.indices) {
            val e = data[i]
            val top = viewHeight - e * 1.0f / max * viewHeight
            elements.add(RectF(left, top, left + w, viewHeight))
            left += (w + EL_GAP)
        }
    }

    private fun resetTempElements() {
        tempElements.clear()
        val viewHeight = getElementHeight()
        val w = getElementWidth()
        val max = data.maxOrNull() ?: return
        var left = 0f
        for (i in tempData.indices) {
            val e = tempData[i]
            val top = height - e * 1.0f / max * viewHeight
            tempElements.add(RectF(left, top, left + w, height.toFloat()))
            left += (w + EL_GAP)
        }
    }

    override fun onAction(action: AlgorithmAction) {
        when (action) {
            is SwapAction -> {
                pos1 = Pair(UPPER_INDEX, action.p1)
                pos2 = Pair(UPPER_INDEX, action.p2)
                initElements()
                val rect1 = elements[action.p1]
                val rect2 = elements[action.p2]
                animatorRectHorizontal(rect1, RectF(rect2))
                animatorRectHorizontal(rect2, RectF(rect1))
            }
            is CopyAction -> {
                initElements()
                when (action) {
                    is ImportCopyAction -> {
                        pos1 = Pair(LOWER_INDEX, action.from)
                        tempData = action.data
                        resetTempElements()
                        val originRect = tempElements[action.from]
                        elements[action.to].reset()
                        animateRect(originRect, moveToPos(originRect, action.to, false))
                    }
                    is ExportCopyAction -> {
                        pos1 = Pair(UPPER_INDEX, action.from)
                        tempData = action.data
                        resetTempElements()
                        val originRect = elements[action.from]
                        tempElements[action.to].reset()
                        animateRect(originRect, moveToPos(originRect, action.to, true))
                    }
                    else -> {
                        resetTempElements()
                        pos1 = Pair(UPPER_INDEX, action.from)
                        val fromRect = elements[action.from]
                        val toRect = elements[action.to]
                        toRect.reset()
                        animateRect(fromRect, moveToPos(fromRect, action.to, false))
                    }
                }
            }
            is AlgorithmAction.MessageAction -> {
                text = action.msg
                invalidate()
            }
            is AlgorithmAction.FinishAction -> {
                pos1 = Pair(-1, -1)
                pos2 = Pair(-1, -1)
            }
            is PivotAction -> {
                pivot = action.i
            }
        }
        invalidate()
    }


    private fun moveToPos(rect: RectF, to: Int, isTemp: Boolean): RectF {
        val w = getElementWidth()
        val l = (w + EL_GAP) * to
        val viewHeight = getElementHeight()
        return if (isTemp) {
            RectF(l, height - rect.height(), l + w, height.toFloat())
        } else {
            RectF(l, viewHeight - rect.height(), l + w, viewHeight)
        }
    }

    private fun animatorRectHorizontal(originRect: RectF, targetRect: RectF) {
        val moveAnimator = ValueAnimator.ofFloat(1f)
        moveAnimator.duration = DEFAULT_DELAY_TIME - 200
        val left = originRect.left
        val right = originRect.right
        moveAnimator.addUpdateListener {
            val progress = it.animatedFraction
            originRect.left = left + (targetRect.left - left) * progress
            originRect.right = right + (targetRect.right - right) * progress
            invalidate()
        }
        moveAnimator.start()
    }

    private fun animateRect(originRect: RectF, targetRect: RectF) {
        val moveAnimator = ValueAnimator.ofFloat(1f)
        moveAnimator.duration = DEFAULT_DELAY_TIME - 200
        val left = originRect.left
        val top = originRect.top
        val right = originRect.right
        val bottom = originRect.bottom
        val rect = RectF(targetRect)
        targetRect.reset()
        moveAnimator.addUpdateListener {
            val progress = it.animatedFraction
            originRect.left = left + (rect.left - left) * progress
            originRect.top = top + (rect.top - top) * progress
            originRect.right = right + (rect.right - right) * progress
            originRect.bottom = bottom + (rect.bottom - bottom) * progress
            invalidate()
        }
        moveAnimator.start()
    }
}
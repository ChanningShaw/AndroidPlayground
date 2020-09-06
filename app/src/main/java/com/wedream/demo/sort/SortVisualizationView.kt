package com.wedream.demo.sort

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wedream.demo.planegeometry.reset
import com.wedream.demo.sort.SortAlgorithm.SLEEP_TIME
import com.wedream.demo.util.ArrayUtils
import com.wedream.demo.util.LogUtils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.math.log2

class SortVisualizationView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var data = emptyArray<Int>()
    private var elements = arrayListOf<RectF>()
    private var paintDefault = Paint()
    private var paintSelected1 = Paint()
    private var paintSelected2 = Paint()
    private var textPaint = Paint()
    private var pos1 = -1
    private var pos2 = -1
    private var tempRect = RectF(0f, 0f, 0f, 0f)
    private var moveToTemp = false
    private var text = ""

    private var algo = SortAlgorithm.Type.Bubble


    companion object {
        const val EL_GAP = 5
        const val DIVIDE_GAP = 20
        const val EL_SIZE = 10
    }

    init {
        paintDefault.style = Paint.Style.FILL_AND_STROKE
        paintDefault.color = Color.parseColor("#3CB371")
        paintSelected1.color = Color.parseColor("#436EEE")
        paintSelected2.color = Color.parseColor("#551A8B")
        textPaint.color = Color.BLACK
        textPaint.textSize = 60f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { c ->
            for (i in elements.indices) {
                when (i) {
                    pos1 -> {
                        c.drawRect(elements[i], paintSelected1)
                    }
                    pos2 -> {
                        c.drawRect(elements[i], paintSelected2)
                    }
                    else -> {
                        c.drawRect(elements[i], paintDefault)
                    }
                }
            }
            if (!moveToTemp) {
                c.drawRect(tempRect, paintDefault)
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

    fun reset() {
        data = ArrayUtils.randomArray(EL_SIZE)
        pos1 = -1
        pos2 = -1
        moveToTemp = false
        initElements()
        invalidate()
    }

    private fun initElements() {
        elements.clear()
        val viewHeight = getElementHeight()
        val w = getElementWidth()
        val max = data.max() ?: return
        var left = 0f
        for (i in data.indices) {
            val e = data[i]
            val top = viewHeight - e * 1.0f / max * viewHeight
            elements.add(RectF(left, top, left + w, viewHeight))
            left += (w + EL_GAP)
        }
    }

    fun startSort() {
        val flow = AlgorithmRunner().startSort(data, algo)
        GlobalScope.launch(Dispatchers.Main) {
            flow.collect {
                when (it) {
                    is AlgorithmAction.SwapAction -> {
                        pos1 = it.p1
                        pos2 = it.p2
                        initElements()
                        val rect1 = elements[it.p1]
                        val rect2 = elements[it.p2]
                        animatorRectHorizontal(rect1, RectF(rect2))
                        animatorRectHorizontal(rect2, RectF(rect1))
                        invalidate()
                    }
                    is AlgorithmAction.CopyAction -> {
                        pos1 = it.from
                        moveToTemp = false
                        initElements()
                        if (it.from < 0 || it.to < 0) {
                            if (it.from < 0) {
                                val targetRect = elements[it.to]
                                animatorRect(tempRect, moveToPos(tempRect, it.to))
                                targetRect.reset()
                            } else {
                                val originRect = elements[it.from]
                                val targetRect = moveToTemp(originRect)
                                animatorRect(originRect, targetRect)
                                tempRect.set(targetRect)
                                moveToTemp = true
                            }
                        } else {
                            val fromRect = elements[it.from]
                            val toRect = elements[it.to]
                            toRect.reset()
                            animatorRect(fromRect, moveToPos(fromRect, it.to))
                        }
                        invalidate()
                    }
                    is AlgorithmAction.MessageAction -> {
                        text = it.msg
                        invalidate()
                    }
                    is AlgorithmAction.FinishAction -> {
                        pos1 = -1
                        pos2 = -1
                        moveToTemp = false
                        invalidate()
                    }
                }
            }
        }
    }

    fun setAlgorithm(algo: SortAlgorithm.Type) {
        this.algo = algo
    }

    private fun moveToTemp(rect: RectF): RectF {
        val w = getElementWidth()
        val l = width * 0.5f - w * 0.5f
        return RectF(l, height - rect.height(), l + w, height.toFloat())
    }

    private fun moveToPos(rect: RectF, to: Int): RectF {
        val w = getElementWidth()
        val l = (w + EL_GAP) * to
        val viewHeight = getElementHeight()
        return RectF(l, viewHeight - rect.height(), l + w, viewHeight)
    }

    private fun animatorRectHorizontal(originRect: RectF, targetRect: RectF) {
        val moveAnimator = ValueAnimator.ofFloat(1f)
        moveAnimator.duration = SLEEP_TIME - 200
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

    private fun animatorRect(originRect: RectF, targetRect: RectF) {
        val moveAnimator = ValueAnimator.ofFloat(1f)
        moveAnimator.duration = SLEEP_TIME - 200
        val left = originRect.left
        val top = originRect.top
        val right = originRect.right
        val bottom = originRect.bottom
        moveAnimator.addUpdateListener {
            val progress = it.animatedFraction
            originRect.left = left + (targetRect.left - left) * progress
            originRect.top = top + (targetRect.top - top) * progress
            originRect.right = right + (targetRect.right - right) * progress
            originRect.bottom = bottom + (targetRect.bottom - bottom) * progress
            invalidate()
        }
        moveAnimator.start()
    }
}
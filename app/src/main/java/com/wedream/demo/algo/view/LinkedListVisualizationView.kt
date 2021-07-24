package com.wedream.demo.algo.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.wedream.demo.R
import com.wedream.demo.algo.action.AlgorithmAction
import com.wedream.demo.algo.playground.VisualizationView
import com.wedream.demo.algo.action.AlgorithmAction.Companion.DEFAULT_DELAY_TIME
import com.wedream.demo.algo.action.DeleteAction
import com.wedream.demo.algo.action.MoveAction
import com.wedream.demo.algo.playground.forEach
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.drawCircleWithText
import com.wedream.demo.util.drawVector

class LinkedListVisualizationView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    VisualizationView(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    private var linkedList = LinkedList<Int>()

    private var paint = Paint()
    private var selectPaint = Paint()
    private var deletePaint = Paint()
    private var numPaint = Paint()
    private var textPaint = Paint()
    private var arrowPaint = Paint()

    private var currentSelectedNode: LinkedList.Node<Int>? = null
    private var deletedNode: LinkedList.Node<Int>? = null
    private var text = ""

    init {
        paint.color = context.resources.getColor(R.color.color_green)
        deletePaint.color = context.resources.getColor(R.color.color_green)
        selectPaint.color = context.resources.getColor(R.color.color_violet)
        numPaint.color = Color.WHITE
        numPaint.textSize = 40f
        textPaint.color = Color.BLACK
        textPaint.textSize = 50f
        arrowPaint.color = context.resources.getColor(R.color.color_blue)
        arrowPaint.strokeWidth = 5f
    }

    companion object {
        const val RADIUS = 35f
    }

    fun setData(structure: LinkedList<Int>) {
        linkedList = structure
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val size = linkedList.getSize()
            val gap = (width - size * RADIUS * 2) / (size + 1)
            val y = height * 0.5f
            var x = gap + RADIUS
            linkedList.forEach { index: Int, node: LinkedList.Node<Int> ->
                when (node) {
                    deletedNode -> {
                        textPaint.alpha = deletePaint.alpha
                        it.drawCircleWithText(
                            x, y, RADIUS, deletePaint, node.value.toString(), textPaint
                        )
                    }
                    currentSelectedNode -> {
                        textPaint.alpha = 255
                        it.drawCircleWithText(
                            x, y, RADIUS, selectPaint, node.value.toString(), textPaint
                        )
                    }
                    else -> {
                        textPaint.alpha = 255
                        it.drawCircleWithText(
                            x, y, RADIUS, paint, node.value.toString(), textPaint
                        )
                    }
                }

                if (index != size - 1) {
                    it.drawVector(x + RADIUS, y, x + RADIUS + gap, y, arrowPaint)
                }
                x += (gap + RADIUS * 2)
            }
            val tw = textPaint.measureText(text)
            it.drawText(text, width * 0.5f - tw * 0.5f, 50f, textPaint)
        }
    }

    override fun onAction(action: AlgorithmAction) {
        when (action) {
            is MoveAction -> {
                currentSelectedNode = action.node
            }
            is AlgorithmAction.FinishAction -> {
                currentSelectedNode = null
            }
            is AlgorithmAction.MessageAction -> {
                text = action.msg
            }
            is DeleteAction -> {
                val moveAnimator = ValueAnimator.ofFloat(1f)
                moveAnimator.duration = DEFAULT_DELAY_TIME - 100
                moveAnimator.addUpdateListener {
                    deletePaint.alpha = (255 * (1f - it.animatedFraction)).toInt()
                    invalidate()
                }
                moveAnimator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        deletePaint.alpha = 255
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                deletedNode = action.node
                moveAnimator.start()
            }
        }
        invalidate()
    }
}
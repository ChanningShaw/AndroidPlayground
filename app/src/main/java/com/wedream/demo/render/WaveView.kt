package com.wedream.demo.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class WaveView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var points = arrayOf<Float>()
    private var data = arrayOf<Int>()
    private var paint = Paint()
    private var topPath = Path()
    private var bottomPath = Path()
    private var type: Type = Type.Linear

    init {
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#9F70DA")
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val centerY = height * 0.5f
            topPath.reset()
            bottomPath.reset()
            topPath.moveTo(0f, centerY)
            bottomPath.moveTo(0f, centerY)
            val w = width.toFloat()
            for (i in points.indices) {
                val x = (i + 1f) / (points.size + 1f) * w
                topPath.lineTo(x, centerY - points[i])
                bottomPath.lineTo(x, centerY + points[i])
            }
            topPath.lineTo(w, centerY)
            bottomPath.lineTo(w, centerY)
            topPath.close()
            bottomPath.close()
            it.drawPath(topPath, paint)
            it.drawPath(bottomPath, paint)
        }
    }


    fun setData(data: Array<Int>) {
        this.data = data
        reGeneratePoints()
    }

    fun setType(type: Type) {
        this.type = type
        reGeneratePoints()
    }

    private fun reGeneratePoints() {
        points = Array(data.size) { 0.0f }
        val maxValue = data.max()?.toFloat() ?: return
        if (maxValue <= 0) {
            return
        }
        when (type) {
            Type.Linear -> {
                val ratio = height * 0.5f / maxValue
                for (i in data.indices) {
                    points[i] = data[i] * ratio
                    if (points[i] < 1f) {
                        points[i] = 1f
                    }
                }
            }
            Type.Square -> {
                val ratio = height * 0.5f / (maxValue * maxValue)
                for (i in data.indices) {
                    points[i] = data[i] * data[i] * ratio
                    if (points[i] < 1f) {
                        points[i] = 1f
                    }
                }
            }
            Type.RankLinear -> {
                val set = hashSetOf<Int>()
                set.addAll(data)
                val arr = Array(set.size) { 0 }
                set.toArray(arr)
                arr.sort()
                val ratio = height * 0.5f / ((arr.size - 1))

                for (i in data.indices) {
                    var j = arr.size - 1
                    while (j >= 0) {
                        if (data[i] == arr[j]) {
                            points[i] = j * ratio
                            if (points[i] < 1f) {
                                points[i] = 1f
                            }
                            break
                        }
                        j--
                    }
                }
            }
            Type.RankSquare -> {
                val set = hashSetOf<Int>()
                set.addAll(data)
                val arr = Array(set.size) { 0 }
                set.toArray(arr)
                arr.sort()
                val ratio = height * 0.5f / ((arr.size - 1) * (arr.size - 1))

                for (i in data.indices) {
                    var j = arr.size - 1
                    while (j >= 0) {
                        if (data[i] == arr[j]) {
                            points[i] = j * j * ratio
                            if (points[i] < 1f) {
                                points[i] = 1f
                            }
                            break
                        }
                        j--
                    }
                }
            }
            Type.UpCubic -> {
                val h = height * 0.5f
                for (i in data.indices) {
                    val x = data[i] / maxValue
                    points[i] = (4 * x * x * x - 6 * x * x + 3 * x) * h
                    if (points[i] < 1f) {
                        points[i] = 1f
                    }
                }
            }
            Type.DownCubic -> {
                val h = height * 0.5f
                for (i in data.indices) {
                    val x = data[i] / maxValue
                    points[i] = (-2 * x * x * x + 3 * x * x) * h
                    if (points[i] < 1f) {
                        points[i] = 1f
                    }
                }
            }
        }
        invalidate()
    }

    enum class Type {
        Linear, Square, RankLinear, RankSquare, UpCubic, DownCubic
    }
}
package com.wedream.demo.render

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.wedream.demo.util.LogUtils.log

class WaveView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var points = arrayOf<Float>()
    private var data = arrayOf<Int>()
    private var paint = Paint()
    private var topPath = Path()
    private var start = 0
    private var end = 0
    private var type: Type = Type.Linear
    private var itemWidth = 5.0f

    init {
        paint.isAntiAlias = true
        paint.color = Color.parseColor("#9F70DA")
    }

    override fun onDraw(canvas: Canvas) {
        if (points.isEmpty()) return
        val centerY = height * 0.5f
        topPath.reset()
        for (i in points.indices) {
            val x = i * itemWidth
            topPath.lineTo(x, centerY - points[i])
        }
        for (i in points.indices) {
            val x = (points.lastIndex - i) * itemWidth
            topPath.lineTo(x, centerY + points[points.lastIndex - i])
        }
        topPath.close()
        canvas.drawPath(topPath, paint)
    }

    fun setItemWidth(width : Float) {
        itemWidth = width
        invalidate()
    }

    fun setData(data: Array<Int>, start: Int = 0, end: Int = data.lastIndex) {
        this.data = data
        this.start = start
        this.end = end
        post {
            reGeneratePoints()
        }
    }

    fun setType(type: Type) {
        this.type = type
        reGeneratePoints()
    }

    private fun reGeneratePoints() {
        points = Array(end - start + 1) { 0.0f }
        val maxValue = data.maxOrNull() ?: 0
        if (maxValue <= 0) {
            return
        }
        when (type) {
            Type.Linear -> {
                val ratio = height * 0.5f / maxValue
                for (i in start..end) {
                    points[i - start] = data[i] * ratio
                    if (points[i - start] < 1f) {
                        points[i - start] = 1f
                    }
                }
            }
            Type.Square -> {
                val ratio = height * 0.5f / (maxValue * maxValue)
                for (i in start..end) {
                    points[i - start] = data[i] * data[i] * ratio
                    if (points[i - start] < 1f) {
                        points[i - start] = 1f
                    }
                }
            }
            Type.RankLinear -> {
                val set = hashSetOf<Int>()
                for (i in start..end) {
                    set.add(data[i])
                }
                val arr = Array(end - start + 1) { 0 }
                set.toArray(arr)
                arr.sort()
                val ratio = height * 0.5f / (arr.size - 1)

                for (i in start..end) {
                    var j = arr.size - 1
                    while (j >= 0) {
                        if (data[i] == arr[j]) {
                            points[i - start] = j * ratio
                            if (points[i - start] < 1f) {
                                points[i - start] = 1f
                            }
                            break
                        }
                        j--
                    }
                }
            }
            Type.RankSquare -> {
                val set = hashSetOf<Int>()
                for (i in start..end) {
                    set.add(data[i])
                }
                val arr = Array(set.size) { 0 }
                set.toArray(arr)
                arr.sort()
                val ratio = height * 0.5f / ((arr.size - 1) * (arr.size - 1))

                for (i in start..end) {
                    var j = arr.size - 1
                    while (j >= 0) {
                        if (data[i] == arr[j]) {
                            points[i - start] = j * j * ratio
                            if (points[i - start] < 1f) {
                                points[i - start] = 1f
                            }
                            break
                        }
                        j--
                    }
                }
            }
            Type.UpCubic -> {
                val h = height * 0.5f
                for (i in start..end) {
                    val x = data[i] / maxValue
                    points[i - start] = (4 * x * x * x - 6 * x * x + 3 * x) * h
                    if (points[i - start] < 1f) {
                        points[i - start] = 1f
                    }
                }
            }
            Type.DownCubic -> {
                val h = height * 0.5f
                for (i in start..end) {
                    val x = data[i] / maxValue
                    points[i - start] = (-2 * x * x * x + 3 * x * x) * h
                    if (points[i - start] < 1f) {
                        points[i - start] = 1f
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
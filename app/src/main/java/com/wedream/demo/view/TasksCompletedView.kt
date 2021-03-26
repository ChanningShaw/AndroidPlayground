package com.wedream.demo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.wedream.demo.R

class TasksCompletedView(context: Context, attrs: AttributeSet) : View(context, attrs) {
  // 画圆环的画笔
  private var mRingPaint: Paint? = null

  // 画圆环的画笔背景色
  private var mRingPaintBg: Paint? = null

  // 圆环颜色
  private var mRingColor = 0

  // 圆环背景颜色
  private var mRingBgColor = 0

  // 半径
  private var mRadius = 0f

  // 圆环半径
  private var mRingRadius = 0f

  // 圆环宽度
  private var mStrokeWidth = 0f

  // 当前进度
  private var mProgress = 0f
  private var userCenter = true
  private var ringPaintStyle = Paint.Style.FILL
  private val oval1 = RectF()
  private val oval = RectF()

  init {
    // 获取自定义的属性
    initAttrs(context, attrs)
    initVariable()
  }

  // 属性
  private fun initAttrs(context: Context, attrs: AttributeSet) {
    val typeArray = context.theme.obtainStyledAttributes(attrs,
      R.styleable.TasksCompletedView, 0, 0)
    mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80f)
    mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10f)
    mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, -0x1)
    mRingBgColor = typeArray.getColor(R.styleable.TasksCompletedView_ringBgColor, -0x1)
    mRingRadius = mRadius + mStrokeWidth / 2
    typeArray.recycle()
  }

  // 初始化画笔
  private fun initVariable() {

    // 外圆弧背景
    mRingPaintBg = Paint()
    mRingPaintBg!!.isAntiAlias = true
    mRingPaintBg!!.color = mRingBgColor
    mRingPaintBg!!.style = Paint.Style.STROKE
    mRingPaintBg!!.strokeWidth = mStrokeWidth


    // 外圆弧
    mRingPaint = Paint()
    mRingPaint!!.isAntiAlias = true
    mRingPaint!!.color = mRingColor
    mRingPaint!!.style = Paint.Style.STROKE
    mRingPaint!!.strokeWidth = mStrokeWidth

    // 中间字
    // 画字体的画笔
    val mTextPaint = Paint()
    mTextPaint.isAntiAlias = true
    mTextPaint.style = Paint.Style.FILL
    mTextPaint.color = mRingColor
    mTextPaint.textSize = mRadius / 2
  }

  // 画图
  override fun onDraw(canvas: Canvas) {
    // 圆心x坐标
    val mXCenter = width / 2
    // 圆心y坐标
    val mYCenter = height / 2

    // 外圆弧背景
    oval1.left = mXCenter - mRingRadius
    oval1.top = mYCenter - mRingRadius
    oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius)
    oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius)
    canvas.drawArc(oval1, 0f, 360f, true, mRingPaintBg!!) // 圆弧所在的椭圆对象、圆弧的起始角度、圆弧的角度、是否显示半径连线

    // 外圆弧
    if (mProgress > 0) {
      oval.left = mXCenter - mRingRadius
      oval.top = mYCenter - mRingRadius
      oval.right = mRingRadius * 2 + (mXCenter - mRingRadius)
      oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius)
      mRingPaint!!.style = ringPaintStyle
      // 总进度
      val mTotalProgress = 100
      canvas.drawArc(oval, -90f, mProgress / mTotalProgress * 360, userCenter, mRingPaint!!) //
    }
  }

  // 设置进度
  fun setProgress(progress: Float) {
    mProgress = progress
    postInvalidate() // 重绘
  }

  fun setProgressStyle(style: ProgressStyle) {
    if (style == ProgressStyle.STROKE) {
      ringPaintStyle = Paint.Style.STROKE
      userCenter = false
    } else {
      ringPaintStyle = Paint.Style.FILL
      userCenter = true
    }
  }

  enum class ProgressStyle {
    STROKE, FILL
  }
}

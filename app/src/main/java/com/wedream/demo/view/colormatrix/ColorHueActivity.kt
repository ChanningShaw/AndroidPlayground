package com.wedream.demo.view.colormatrix

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity


class ColorHueActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var imageView: ImageView
    private lateinit var seekBarHue: SeekBar
    private lateinit var seekBarSaturation: SeekBar
    private lateinit var seekBarLightness: SeekBar
    private var colorMatrix = ColorMatrix()
    private var mHueMatrix = ColorMatrix()
    private var mSaturationMatrix = ColorMatrix()
    private var mLightnessMatrix = ColorMatrix()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_hue)
        imageView = findViewById<View>(R.id.imageView) as ImageView
        seekBarHue = findViewById<View>(R.id.bar_hue) as SeekBar
        seekBarSaturation = findViewById<View>(R.id.bar_saturation) as SeekBar
        seekBarLightness = findViewById<View>(R.id.bar_lightness) as SeekBar
        seekBarHue.setOnSeekBarChangeListener(this)
        seekBarSaturation.setOnSeekBarChangeListener(this)
        seekBarLightness.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val mHueValue = (seekBarHue.progress - 128f) * 1.0f / 128f * 180
        val mSaturationValue = seekBarSaturation.progress / 128f
        val mLightnessValue = seekBarLightness.progress / 128f

        //设置色相
        mHueMatrix.reset()
        mHueMatrix.setRotate(0, mHueValue)
        mHueMatrix.setRotate(1, mHueValue)
        mHueMatrix.setRotate(2, mHueValue)

        //设置饱和度
        mSaturationMatrix.reset()
        mSaturationMatrix.setSaturation(mSaturationValue)

        //亮度
        mLightnessMatrix.reset()
        mLightnessMatrix.setScale(mLightnessValue, mLightnessValue, mLightnessValue, 1f)
        colorMatrix.reset() // 效果叠加
        colorMatrix.postConcat(mLightnessMatrix)
        colorMatrix.postConcat(mSaturationMatrix)
        colorMatrix.postConcat(mHueMatrix)
        imageView.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
package com.wedream.demo.view.colormatrix

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R


class ColorMatrixActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var imageView: ImageView
    private lateinit var seekBarR: SeekBar
    private lateinit var seekBarG: SeekBar
    private lateinit var seekBarB: SeekBar
    private lateinit var seekBarA: SeekBar
    private var colorMatrix: ColorMatrix? = null
    private lateinit var colorView: View
    private lateinit var colorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_matrix)
        colorMatrix = ColorMatrix()
        colorMatrix?.setScale(calculate(128), calculate(128), calculate(128), calculate(128))
        imageView = findViewById<View>(R.id.imageView) as ImageView
        imageView.colorFilter = ColorMatrixColorFilter(colorMatrix!!)
        seekBarR = findViewById<View>(R.id.bar_R) as SeekBar
        seekBarG = findViewById<View>(R.id.bar_G) as SeekBar
        seekBarB = findViewById<View>(R.id.bar_B) as SeekBar
        seekBarA = findViewById<View>(R.id.bar_A) as SeekBar
        colorView = findViewById(R.id.color_view)
        colorText = findViewById<View>(R.id.color_text) as TextView
        seekBarR.setOnSeekBarChangeListener(this)
        seekBarG.setOnSeekBarChangeListener(this)
        seekBarB.setOnSeekBarChangeListener(this)
        seekBarA.setOnSeekBarChangeListener(this)
    }

    private fun calculate(progress: Int): Float {
        return progress / 128f
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        colorMatrix?.setScale(
            calculate(seekBarR.progress), calculate(seekBarG.progress),
            calculate(seekBarB.progress), calculate(seekBarA.progress)
        )
        colorText.text = ("颜色值：#" + Integer.toHexString(seekBarA.progress)
                + Integer.toHexString(seekBarR.progress)
                + Integer.toHexString(seekBarG.progress)
                + Integer.toHexString(seekBarB.progress))

        colorView.setBackgroundColor(
            Color.argb(
                seekBarA.progress,
                seekBarR.progress,
                seekBarG.progress,
                seekBarB.progress
            )
        )
        imageView.colorFilter = ColorMatrixColorFilter(colorMatrix!!)
    }


    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
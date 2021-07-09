package com.wedream.demo.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.util.AndroidUtils
import com.wedream.demo.util.LogUtils.log


class CanvasBitmapActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap

    private var paint = Paint()

    init {
        paint.color = Color.GREEN
        paint.strokeWidth = 10f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_bitmap)
        val view = findViewById<ImageView>(R.id.imageView)
        bitmap = Bitmap.createBitmap(
            DeviceParams.SCREEN_WIDTH,
            AndroidUtils.dip2pix(100),
            Bitmap.Config.RGB_565
        )
        view.setOnTouchListener { v, event ->
            drawIntoBitmap(event.x)
            view.invalidate()
            false
        }
        view.setImageBitmap(bitmap)
    }

    private fun drawIntoBitmap(x: Float): Bitmap {
        //利用bitmap生成画布
        val canvas = Canvas(bitmap)
        log { "draw ${x}" }
        canvas.drawLine(x, 0f, x, canvas.height.toFloat(), paint)
        return bitmap
    }

    override fun onDestroy() {
        super.onDestroy()
        bitmap.recycle()
    }
}
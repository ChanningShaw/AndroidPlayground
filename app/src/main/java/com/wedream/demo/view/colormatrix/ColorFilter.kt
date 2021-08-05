package com.wedream.demo.view.colormatrix

import android.graphics.*
import android.widget.ImageView
import com.wedream.demo.util.destroy

/**
 * 我收集的颜色滤镜
 * Created by Martin on 2016/8/1 0001.
 */
object ColorFilter {
    /**
     * 为imageView设置颜色滤镜
     *
     * @param imageView
     * @param colormatrix
     */
    fun imageViewColorFilter(imageView: ImageView, colormatrix: FloatArray?) {
        setColorMatrixColorFilter(imageView, ColorMatrixColorFilter(ColorMatrix(colormatrix)))
    }

    /**
     * 为imageView设置颜色偏向滤镜
     *
     * @param imageView
     * @param color
     */
    fun imageViewColorFilter(imageView: ImageView, color: Int) {
        val colorMatrix = ColorMatrix()
        colorMatrix.setScale(
            Color.alpha(color).toFloat(),
            Color.red(color).toFloat(),
            Color.green(color).toFloat(),
            Color.blue(color).toFloat()
        )
        setColorMatrixColorFilter(imageView, ColorMatrixColorFilter(colorMatrix))
    }

    /**
     * 生成对应颜色偏向滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param color
     * @return
     */
    fun bitmapColorFilter(bitmap: Bitmap, color: Int): Bitmap {
        val colorMatrix = ColorMatrix()
        colorMatrix.setScale(
            Color.alpha(color).toFloat(),
            Color.red(color).toFloat(),
            Color.green(color).toFloat(),
            Color.blue(color).toFloat()
        )
        return setColorMatrixColorFilter(bitmap, ColorMatrixColorFilter(colorMatrix), true)
    }

    /**
     * 生成对应颜色滤镜的图片，并回收原图
     *
     * @param bitmap
     * @param colormatrix
     * @return
     */
    fun bitmapColorFilter(bitmap: Bitmap, colormatrix: FloatArray?): Bitmap {
        return setColorMatrix(bitmap, colormatrix, true)
    }

    /**
     * 生成对应颜色滤镜的图片
     *
     * @param bitmap
     * @param colormatrix
     * @param isRecycle
     * @return
     */
    fun setColorMatrix(bitmap: Bitmap, colormatrix: FloatArray?, isRecycle: Boolean): Bitmap {
        return setColorMatrixColorFilter(bitmap, ColorMatrixColorFilter(ColorMatrix(colormatrix)), isRecycle)
    }

    fun setColorMatrixColorFilter(imageView: ImageView, matrixColorFilter: ColorMatrixColorFilter?) {
        imageView.colorFilter = matrixColorFilter
    }

    fun setColorMatrixColorFilter(
        bitmap: Bitmap,
        matrixColorFilter: ColorMatrixColorFilter?,
        isRecycle: Boolean
    ): Bitmap {
        val resource = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.colorFilter = matrixColorFilter
        val canvas = Canvas(resource)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        if (isRecycle) bitmap.destroy()
        return resource
    }

    // 黑白
    val colormatrix_heibai = floatArrayOf(
        0.8f, 1.6f, 0.2f, 0f,
        -163.9f, 0.8f, 1.6f, 0.2f, 0f, -163.9f, 0.8f, 1.6f, 0.2f, 0f,
        -163.9f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 怀旧
    val colormatrix_huajiu = floatArrayOf(
        0.2f, 0.5f, 0.1f, 0f,
        40.8f, 0.2f, 0.5f, 0.1f, 0f, 40.8f, 0.2f, 0.5f, 0.1f, 0f, 40.8f, 0f, 0f, 0f, 1f, 0f
    )

    // 哥特
    val colormatrix_gete = floatArrayOf(
        1.9f, -0.3f, -0.2f, 0f,
        -87.0f, -0.2f, 1.7f, -0.1f, 0f, -87.0f, -0.1f, -0.6f, 2.0f, 0f,
        -87.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 淡雅
    val colormatrix_danya = floatArrayOf(
        0.6f, 0.3f, 0.1f, 0f,
        73.3f, 0.2f, 0.7f, 0.1f, 0f, 73.3f, 0.2f, 0.3f, 0.4f, 0f, 73.3f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 蓝调
    val colormatrix_landiao = floatArrayOf(
        2.1f, -1.4f, 0.6f,
        0.0f, -71.0f, -0.3f, 2.0f, -0.3f, 0.0f, -71.0f, -1.1f, -0.2f, 2.6f,
        0.0f, -71.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f
    )

    // 光晕
    val colormatrix_guangyun =
        floatArrayOf(0.9f, 0f, 0f, 0f, 64.9f, 0f, 0.9f, 0f, 0f, 64.9f, 0f, 0f, 0.9f, 0f, 64.9f, 0f, 0f, 0f, 1.0f, 0f)

    // 梦幻
    val colormatrix_menghuan = floatArrayOf(
        0.8f, 0.3f, 0.1f,
        0.0f, 46.5f, 0.1f, 0.9f, 0.0f, 0.0f, 46.5f, 0.1f, 0.3f, 0.7f, 0.0f,
        46.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f
    )

    // 酒红
    val colormatrix_jiuhong = floatArrayOf(
        1.2f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.9f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 胶片
    val colormatrix_fanse = floatArrayOf(
        -1.0f, 0.0f, 0.0f, 0.0f,
        255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f,
        255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f
    )

    // 湖光掠影
    val colormatrix_huguang = floatArrayOf(
        0.8f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9f, 0.0f, 0.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 褐片
    val colormatrix_hepian = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 复古
    val colormatrix_fugu = floatArrayOf(
        0.9f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.8f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 泛黄
    val colormatrix_huan_huang = floatArrayOf(
        1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f,
        0.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 传统
    val colormatrix_chuan_tong = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0f,
        -10f, 0.0f, 1.0f, 0.0f, 0f, -10f, 0.0f, 0.0f, 1.0f, 0f, -10f, 0f, 0f, 0f, 1f, 0f
    )

    // 胶片2
    val colormatrix_jiao_pian = floatArrayOf(
        0.71f, 0.2f, 0.0f,
        0.0f, 60.0f, 0.0f, 0.94f, 0.0f, 0.0f, 60.0f, 0.0f, 0.0f, 0.62f,
        0.0f, 60.0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 锐色
    val colormatrix_ruise = floatArrayOf(
        4.8f, -1.0f, -0.1f, 0f,
        -388.4f, -0.5f, 4.4f, -0.1f, 0f, -388.4f, -0.5f, -1.0f, 5.2f, 0f,
        -388.4f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 清宁
    val colormatrix_qingning = floatArrayOf(
        0.9f, 0f, 0f, 0f, 0f, 0f,
        1.1f, 0f, 0f, 0f, 0f, 0f, 0.9f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0f
    )

    // 浪漫
    val colormatrix_langman =
        floatArrayOf(0.9f, 0f, 0f, 0f, 63.0f, 0f, 0.9f, 0f, 0f, 63.0f, 0f, 0f, 0.9f, 0f, 63.0f, 0f, 0f, 0f, 1.0f, 0f)

    // 夜色
    val colormatrix_yese = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f,
        -66.6f, 0.0f, 1.1f, 0.0f, 0.0f, -66.6f, 0.0f, 0.0f, 1.0f, 0.0f,
        -66.6f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f
    )
}
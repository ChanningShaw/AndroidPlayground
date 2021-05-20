package com.wedream.demo.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object BitmapUtils {
    private const val Tag = "BitmapUtils"
    fun postMatrix(matrix: Matrix?, bitmap: Bitmap, isRecycle: Boolean): Bitmap {
        val resource = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        if (isRecycle) destroyBitmap(bitmap)
        return resource
    }
    /**
     * 真实偏移,图片宽高会根据图片的偏移距离重新生成宽高
     *
     * @param bitmap,目标图片
     * @param preX,偏移X轴百分比
     * @param preY,偏移Y轴百分比
     * @param isRecycle，是否回收目标图
     * @return
     */
    /**
     * 真实偏移,图片宽高会根据图片的偏移距离重新生成宽高,回收原始图片
     *
     * @param bitmap,目标图片
     * @param preX,偏移X轴百分比
     * @param preY,偏移Y轴百分比
     * @return
     */
    @JvmOverloads
    fun translate(bitmap: Bitmap, preX: Float, preY: Float, isRecycle: Boolean = true): Bitmap {
        val matrix = Matrix()
        val width = bitmap.width
        val height = bitmap.height
        val disX = (width * preX).toInt()
        val disY = (height * preY).toInt()
        matrix.postTranslate(disX.toFloat(), disY.toFloat())
        val resource = Bitmap.createBitmap(bitmap.width - disX, bitmap.height - disY, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resource)
        //        canvas.drawColor(Color.BLACK);//模擬空白
        canvas.concat(matrix)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap(bitmap, -disX.toFloat(), -disY.toFloat(), paint) //从负偏移点开始画，那么如果进行了translate操作，那么图片不会留白
        if (isRecycle) destroyBitmap(bitmap)
        return resource
    }
    /**
     * 真实缩放,图片宽高会根据图片的缩放比例重新生成宽高
     *
     * @param bitmap,目标
     * @param scale，缩放比例
     * @param isRecycle，是否回收目标图
     * @return
     */
    /**
     * 真实缩放,图片宽高会根据图片的缩放比例重新生成宽高,回收原始图片
     *
     * @param bitmap
     * @param scale
     * @return
     */
    @JvmOverloads
    fun scale(bitmap: Bitmap, scale: Float, isRecycle: Boolean = true): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val reWidth = (width * scale).toInt()
        val reHeight = (height * scale).toInt()
        matrix.postScale(scale, scale)
        val resource = Bitmap.createBitmap(reWidth, reHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resource)
        //        canvas.drawColor(Color.BLUE);//模擬空白
        canvas.concat(matrix)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap(
            bitmap,
            ((reWidth - width) * 1.0 / 2).toFloat(),
            ((reHeight - height) * 1.0 / 2).toFloat(),
            paint
        )
        if (isRecycle) destroyBitmap(bitmap)
        return resource
    }
    /**
     * 真实旋转,图片宽高会根据图片的旋转角度重新生成宽高
     *
     * @param bitmap,目标
     * @param degree，旋转角度
     * @param isRecycle，是否回收目标图
     * @return
     */
    /**
     * 真实旋转,图片宽高会根据图片的旋转角度重新生成宽高,回收原始图片
     *
     * @param bitmap
     * @param degree
     * @return
     */
    @JvmOverloads
    fun rotate(bitmap: Bitmap, degree: Float, isRecycle: Boolean = true): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val reWidth: Int
        val reHeight: Int
        //       以弧度为计算方式则新图size为（width*cos(a)+height*sin(a), height*cos(a)+width*sin(a)）
        val angle = degree * Math.PI / 180 //生成degree对应的弧度
        val a = abs(sin(angle))
        val b = abs(cos(angle))
        reWidth = (width * b + height * a).toInt()
        reHeight = (height * b + width * a).toInt()
        Log.i(Tag, "width: $width   reWidth   :$reWidth")
        Log.i(Tag, "height: $height   reHeight   :$reHeight")
        val resource = Bitmap.createBitmap(reWidth, reHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resource)
        //        canvas.drawColor(Color.BLACK);//模擬空白
        matrix.postRotate(degree, (reWidth / 2).toFloat(), (reHeight / 2).toFloat())
        canvas.concat(matrix)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap(
            bitmap,
            ((reWidth - width) * 1.0 / 2).toFloat(),
            ((reHeight - height) * 1.0 / 2).toFloat(),
            paint
        )
        if (isRecycle) destroyBitmap(bitmap)
        return resource
    }

    /**
     * 清除bitmap对象
     *
     * @param bitmap 目标对象
     */
    fun destroyBitmap(bitmap: Bitmap) {
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}
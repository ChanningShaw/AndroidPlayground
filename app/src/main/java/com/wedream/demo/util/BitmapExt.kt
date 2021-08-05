package com.wedream.demo.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.wedream.demo.util.BitmapExt.Tag
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object BitmapExt {
    const val Tag = "BitmapUtils"
}

fun Bitmap.postMatrix(matrix: Matrix?, isRecycle: Boolean): Bitmap {
    val resource = Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    if (isRecycle) destroy()
    return resource
}

/**
 * 真实偏移,图片宽高会根据图片的偏移距离重新生成宽高
 *
 * @param preX,偏移X轴百分比
 * @param preY,偏移Y轴百分比
 * @param isRecycle，是否回收目标图
 * @return
 */
@JvmOverloads
fun Bitmap.translate(preX: Float, preY: Float, isRecycle: Boolean = true): Bitmap {
    val matrix = Matrix()
    val disX = (width * preX).toInt()
    val disY = (height * preY).toInt()
    matrix.postTranslate(disX.toFloat(), disY.toFloat())
    val resource = Bitmap.createBitmap(width - disX, height - disY, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(resource)
    //        canvas.drawColor(Color.BLACK);//模擬空白
    canvas.concat(matrix)
    val paint = Paint()
    paint.isAntiAlias = true
    canvas.drawBitmap(this, -disX.toFloat(), -disY.toFloat(), paint) //从负偏移点开始画，那么如果进行了translate操作，那么图片不会留白
    if (isRecycle) destroy()
    return resource
}

/**
 * 真实缩放,图片宽高会根据图片的缩放比例重新生成宽高
 *
 * @param scale，缩放比例
 * @param isRecycle，是否回收目标图
 * @return
 */
@JvmOverloads
fun Bitmap.scale(scale: Float, isRecycle: Boolean = true): Bitmap {
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
        this,
        ((reWidth - width) * 1.0 / 2).toFloat(),
        ((reHeight - height) * 1.0 / 2).toFloat(),
        paint
    )
    if (isRecycle) destroy()
    return resource
}

/**
 * 真实旋转,图片宽高会根据图片的旋转角度重新生成宽高,回收原始图片
 *
 * @param degree
 * @return
 */
@JvmOverloads
fun Bitmap.rotate(degree: Float, isRecycle: Boolean = true): Bitmap {
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
        this,
        ((reWidth - width) * 1.0 / 2).toFloat(),
        ((reHeight - height) * 1.0 / 2).toFloat(),
        paint
    )
    if (isRecycle) destroy()
    return resource
}

fun Bitmap.destroy() {
    if (!isRecycled) {
        recycle()
    }
}

fun Bitmap.save(dir: File): File {
    val name = System.currentTimeMillis().toString() + ".jpg"
    val file = File(dir, name)
    val fos = FileOutputStream(file)
    this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.flush()
    fos.close()
    return file
}
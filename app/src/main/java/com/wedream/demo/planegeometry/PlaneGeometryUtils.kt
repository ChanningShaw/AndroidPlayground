package com.wedream.demo.planegeometry

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import com.wedream.demo.planegeometry.shape.Circle
import com.wedream.demo.planegeometry.shape.Line
import com.wedream.demo.planegeometry.shape.Rect
import com.wedream.demo.util.BinaryLinearEquation
import kotlin.math.*

/**
 * 一些平面几何工具方法
 *
 * @author xiaochunming
 */
/**
 * 判断一个点是否在矩形之内
 *
 * @param rotation 矩阵的旋转角度，顺时针为正，逆时针为负，角度为单位
 */
fun RectF.contains(x: Float, y: Float, rotation: Float = 0f): Boolean {
    if (rotation == 0f) {
        return x in left..right && y in top..bottom;
    }
    val centerX = centerX()
    val centerY = centerY()
    val dis = PlaneGeometryUtils.twoPointDistance(x, y, centerX(), centerY())
    val currentAngle = atan2(centerY - y, centerX - x)
    val newAngle = rotation / 180 * Math.PI - currentAngle
    val newX = centerX + dis * cos(newAngle)
    val newY = centerY + dis * sin(newAngle)
    return (newY >= centerY - height() / 2) && (newY <= centerY + height() / 2)
            && (newX >= centerX - width() / 2) && (newX <= centerX + width() / 2)
}

/**
 * 判断是否包含另外一个矩形rect
 *
 * @param rotation 本矩阵的旋转角度，顺时针为正，逆时针为负，角度为单位
 */
fun RectF.contains(rect: RectF, rotation: Float): Boolean {
    return contains(rect.left, rect.top, rotation)
            && contains(rect.right, rect.top, rotation)
            && contains(rect.left, rect.bottom, rotation)
            && contains(rect.right, rect.bottom, rotation)
}

/**
 * 取与另外一个矩形的交集部分
 */
fun RectF.and(rect: RectF): RectF {
    val maxLeft = max(left, rect.left)
    val maxTop = max(top, rect.top)
    val minRight = min(right, rect.right)
    val minBottom = min(bottom, rect.bottom)
    if (maxLeft < minRight && maxTop < minBottom) {
        return RectF(maxLeft, maxTop, minRight, minBottom)
    }
    return RectF()
}

/**
 * 让矩形变小
 */
fun RectF.collapse(value: Float) {
    this.left += value
    this.top += value
    this.right -= value
    this.bottom -= value
}

fun RectF.ratio(): Float {
    return width() / height()
}

/**
 *  以ratio的比例填充矩形
 */
fun RectF.fitRect(ratio: Float): RectF {
    return if (ratio() < ratio) {
        // 横向填充
        val width = width()
        val height = width / ratio
        RectF(left, centerY() - height / 2, right, centerY() + height / 2)
    } else {
        val height = height()
        val width = height * ratio
        RectF(centerX() - width / 2, top, centerX() + width / 2, bottom)
    }
}

fun RectF.moveTo(x: Float, y: Float): RectF {
    val deltaX = x - centerX()
    val deltaY = y - centerY()
    return RectF(left + deltaX, top + deltaY, right + deltaX, bottom + deltaY)
}

fun RectF.scale(scale: Float): RectF {
    return RectF(centerX() - width() * scale / 2, centerY() - height() * scale / 2, centerX() + width() * scale / 2, centerY() + height() * scale / 2)
}

operator fun PointF.minus(p: PointF): PointF {
    x -= p.x
    y -= p.y
    return this
}


fun PointF.distanceTo(x1: Float, y1: Float): Float {
    return sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1))
}

object PlaneGeometryUtils {
    fun twoPointDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    fun twoPointDistance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y))
    }

    fun rectOfCenterPoint(x: Float, y: Float, radius: Float): RectF {
        return RectF(x - radius, y - radius, x + radius, y + radius)
    }

    /**
     * 将points里面的点转换到以anchorX,anchorY为中心地点的坐标系上
     *
     * @param rotate 新旧坐标系相差的旋转角度
     */
    fun convertAxis(anchorX: Float, anchorY: Float, rotate: Float, points: FloatArray) {
        val matrix = Matrix()
        matrix.postTranslate(-anchorX, -anchorY)
        matrix.postRotate(-rotate)
        matrix.mapPoints(points)
    }

    fun getCrossPoints(line1: Line, line2: Line): List<PointF> {
        return if (line1.isParallelWith(line2)) {
            emptyList()
        } else {
            var temp = line1.getCoefficients()
            val a1 = temp[0]
            val b1 = temp[1]
            val c1 = temp[2]
            temp = line2.getCoefficients()
            val a2 = temp[0]
            val b2 = temp[1]
            val c2 = temp[2]
            val y = (a1 * c2 - a2 * c1) / (a2 * b1 - a1 * b2)
            val x = if (a1 != 0.0f) {
                -(c1 + b1 * y) / a1
            } else {
                -(c2 + b2 * y) / a2
            }
            listOf(PointF(x, y))
        }
    }

    fun getCrossPoints(line: Line, circle: Circle): List<PointF> {
        val k = line.getSlope()
        val triple = circle.getCoefficients()
        val a = triple[0]
        val b = triple[1]
        val c = triple[2]
        val result = mutableListOf<PointF>()
        if (k == Float.POSITIVE_INFINITY) {
            val x = line.getX(0f)
            val solutions = BinaryLinearEquation.solve(1f, b, x * x + x * a + c)
            for (s in solutions) {
                result.add(PointF(x, s))
            }
            return result
        } else {
            // 消去y，解x
            // y = kx + d
            val d = line.getY(0f)
            val solutions = BinaryLinearEquation.solve(k * k + 1, 2 * k * d + a + b * k, d * d + b * d + c)
            for (x in solutions) {
                result.add(PointF(x, x * k + d))
            }
            return result
        }
    }

    fun getCrossPoints(line: Line, rect: Rect): List<PointF> {
        val list = mutableListOf<PointF>()
        rect.getSegments().forEach { list.addAll(it.crossPointWith(line)) }
        return list
    }

    /**
     * TODO 两圆的交点这里有很多情况
     */
    fun getCrossPoints(c1: Circle, c2: Circle): List<PointF> {
        val list = mutableListOf<PointF>()
        var coefficients = c1.getCoefficients()
        val a1 = coefficients[0]
        val b1 = coefficients[1]
        val c1 = coefficients[2]

        coefficients = c2.getCoefficients()
        val a2 = coefficients[0]
        val b2 = coefficients[1]
        val c2 = coefficients[2]
        if (b1 == b2) {
            val x = -(c2 - c1) / (a2 - a1)
            val solutions = BinaryLinearEquation.solve(1f, b1, x * x + a1 * x + c1)
            for (y in solutions) {
                list.add(PointF(x, y))
            }
        } else {
            val k = (a1 - a2) / (b2 - b1)
            val b = (c1 - c2) / (b2 - b1)
            val solutions = BinaryLinearEquation.solve(k * k + 1, 2 * k * b + a1 + b1 * k, b * b + b1 * b + c1)
            for (x in solutions) {
                list.add(PointF(x, k * x + b))
            }
        }
        return list
    }

    fun isOverlapWith(line: Line, circle: Circle): Boolean {
        return line.distanceTo(circle.getCenter()) <= circle.getRadius()
    }

    fun isOverlapWith(c1: Circle, c2: Circle): Boolean {
        return twoPointDistance(c1.getCenter(), c2.getCenter()) <= c1.getRadius() + c2.getRadius()
    }
}

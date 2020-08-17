package com.wedream.demo.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log

class Segment(private var x1: Float,
              private var y1: Float,
              private var x2: Float,
              private var y2: Float) : Line(x1, y1, x2, y2) {

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawLine(x1, y1, x2, y2, paint)
    }

    override fun isClicked(p: PointF): Boolean {
        return super.isClicked(p) && contains(p)
    }

    override fun crossPointWith(shape: Shape): List<PointF> {
        Log.e("xcm", "$this crossPointWith $shape")
        return super.crossPointWith(shape).filter { p -> has(p) }
    }

    private fun contains(p: PointF): Boolean {
        return p.x in x1..x2 || p.x in x2..x1 || p.y in y1..y2 || p.y in y2..y1
    }

    override fun has(p: PointF): Boolean {
        if (getType() == Type.Horizontal) {
            return p.x in x1..x2 || p.x in x2..x1
        } else if (getType() == Type.Vertical){
            return p.y in y1..y2 || p.y in y2..y1
        }
        val x = getX(p.y)
        val y = getY(p.x)
        return (x in x1..x2 && y in y1..y2) || (x in x2..x1 && y in y2..y1)
    }

    override fun toString(): String {
        return "Segment: $x1, $y1 - $x2, $y2"
    }
}
package com.wing.tree.bruni.lineDrawingAlgorithm

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.Math.abs

class Bresenham(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var point1: PointF? = null
    private val line = mutableListOf<Pair<Float, Float>>()
    private val paint = Paint().apply {
        color = Color.MAGENTA
        strokeWidth = 3.0f
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                point1 = PointF(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                point1 = null
            }
            MotionEvent.ACTION_MOVE -> {
                point1?.let {
                    bresenham(it, PointF(event.x, event.y))
                    //line2(it.x.toInt(), it.y.toInt(), event.x.toInt(), event.y.toInt(), 0)
                    //plotLine(it.x, it.y, event.x, event.y)
                    invalidate()
                }
            }
            else -> {}
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        line.forEach {
            canvas?.drawPoint(it.first, it.second, paint)
        }
    }

    private fun bresenham(point1: PointF, point2: PointF) {
        line.clear()
        var x = point1.x
        var y = point1.y

        val dx = point2.x - point1.x
        val dy = point2.y - point1.y

        val c1 = 2.0f * dy
        val c2 = 2.0f * (dy - dx)
        var p = 2.0f * dy - dx

      //  var s = 0.0f
        while(x <= point2.x) {
            //s++
            line.add(x to y)

            x++

            if (p < 0.0F) {
                p += c1
            } else {
                y++
                p += c2
            }
        }
    }

    private fun plotLine(x0: Float, y0: Float, x1: Float, y1: Float) {
        line.clear()

        var x0 = x0
        var y0 = y0
    val dx = abs(x1 - x0)
    val sx = if (x0 < x1) 1.0F else -1.0F
    val dy = -abs(y1 - y0)
    val sy = if (y0 < y1) 1.0F else -1.0F
    var error = dx + dy

        while (true) {
            line.add(x0 to y0)

            if (x0 == x1 && y0 == y1) break

            val e2 = 2.0F * error

            if (e2 >= dy) {
                if (x0 == x1) break
                error += dy
                x0 += sx
            }

            if (e2 <= dx) {
                if (y0 == y1) break
                error += dx
                y0 += sy
            }
        }
    }

    fun line2(x: Int, y: Int, x2: Int, y2: Int, color: Int) {
        line.clear()
        var x = x
        var y = y
        val w = x2 - x
        val h = y2 - y
        var dx1 = 0
        var dy1 = 0
        var dx2 = 0
        var dy2 = 0
        if (w < 0) dx1 = -1 else if (w > 0) dx1 = 1
        if (h < 0) dy1 = -1 else if (h > 0) dy1 = 1
        if (w < 0) dx2 = -1 else if (w > 0) dx2 = 1
        var longest: Int = Math.abs(w)
        var shortest: Int = Math.abs(h)
        if (longest <= shortest) {
            longest = Math.abs(h)
            shortest = Math.abs(w)
            if (h < 0) dy2 = -1 else if (h > 0) dy2 = 1
            dx2 = 0
        }
        var numerator = longest shr 1
        for (i in 0..longest) {
            line.add(x.toFloat() to y.toFloat())
            numerator += shortest
            if (numerator >= longest) {
                numerator -= longest
                x += dx1
                y += dy1
            } else {
                x += dx2
                y += dy2
            }
        }
    }
}
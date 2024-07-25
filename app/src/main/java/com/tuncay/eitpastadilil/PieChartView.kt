package com.tuncay.eitpastadilil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var sliceCount: Int = 1

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 7f
    }

    fun setSliceCount(count: Int) {
        sliceCount = count
        invalidate()
    }

    fun getSliceCount(): Int {
        return sliceCount
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val width = width.toFloat() - paddingLeft - paddingRight
        val height = height.toFloat() - paddingTop - paddingBottom
        val radius = Math.min(width, height) / 2f
        val centerX = paddingLeft + width / 2
        val centerY = paddingTop + height / 2

        canvas.drawCircle(centerX, centerY, radius, paint)

        for (i in 0 until sliceCount) {
            if (sliceCount != 1) {
                val angle = 2 * Math.PI * i / sliceCount
                val startX = centerX + radius * Math.cos(angle).toFloat()
                val startY = centerY + radius * Math.sin(angle).toFloat()
                canvas.drawLine(centerX, centerY, startX, startY, paint)
            }
        }
    }
}

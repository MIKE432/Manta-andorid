package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.apusart.manta.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class DataPoint() {
    var mX: Float = 0f
    var mY: Float = 0f
    constructor(x: Date, y: Float): this() {
    }

    constructor(x: Float, y: Float): this() {
        mX = x
        mY = y
    }

}

class DataPoints(val context: Context, private var mHeight: Float, private var mWidth: Float) {

    private val mDataPoints = ArrayList<DataPoint>()
    var verticalPadding: Float = 15f
    var horizontalPadding: Float = 15f

    fun addPoint(dataPoint: DataPoint) {
        mDataPoints.add(dataPoint)
    }

    fun setDim(height: Float, width: Float): DataPoints {
        mHeight = height
        mWidth = width
        return this
    }

    fun mapToDrawablePoints(): List<DrawablePoint> {
        mDataPoints.sortBy { dp -> dp.mY }
        val maxWidth = mDataPoints.maxBy { it.mX }?.mX
        val minWidth = mDataPoints.minBy { it.mX }?.mX
        val w = maxWidth!! - minWidth!!
        val maxHeight = mDataPoints.minBy { it.mY }?.mY
        val minHeight = mDataPoints.maxBy { it.mY }?.mY
        val h = minHeight!! - maxHeight!!
        return mDataPoints.map {

            var x = ((it.mX - minWidth) /w) * mWidth
            var y = ((it.mY - maxHeight)/h) * mHeight

            if(x.isNaN()) x = 0.5f*mWidth
            if(y.isNaN()) y = 0.5f*mHeight

            return@map DefaultPoint(context, x, y)
        }
    }
}

abstract class DrawablePoint(val context: Context, val x: Float, val y: Float) {

    val dotPainter = Paint().apply {
        color = context.resources.getColor(R.color.golden_rod)
        strokeWidth = 10f
    }

    var top: Float = 0f
    var bottom: Float = 0f
    var start: Float = 0f
    var end: Float = 0f

    var squareDim: Float = 20f
        set(newVal) {
            field = newVal
            calculateCorners(newVal)
        }

    private fun calculateCorners(f: Float) {
        start = x - 0.5f*f
        bottom = y - 0.5f*f
        end = x + 0.5f*f
        top = y + 0.5f*f
    }

    init {
        squareDim = 20f
    }

    abstract fun draw(canvas: Canvas?)
}

class DefaultPoint(context: Context, x: Float, y: Float): DrawablePoint(context, x, y) {

    init {
        dotPainter.color = context.resources.getColor(R.color.battleship_grey)
        dotPainter.strokeWidth = 5f
        dotPainter.style = Paint.Style.FILL

    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(start,top, end, bottom, dotPainter)
    }
}

class SpecialPoint(context: Context, x: Float, y: Float): DrawablePoint(context, x, y) {

    init {
        dotPainter.color = context.resources.getColor(R.color.black)
        dotPainter.strokeWidth = 2f
        dotPainter.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas?) {
        canvas?.drawRect(start, top, end, bottom, dotPainter)
    }

}

class Graph(context: Context, attrs: AttributeSet): View(context, attrs) {

    val mMainAxisPaint = Paint().apply {
        color = resources.getColor(R.color.black)
        strokeWidth = 4f
    }

    val mSecondaryAxisPaint = Paint().apply {
        color = resources.getColor(R.color.black)
        strokeWidth = 3f
    }

    private var mDrawablePoints = listOf<DrawablePoint>()
    private val mDataPoints = DataPoints(context, height.toFloat(), width.toFloat())
    private var mHeight = 0f
    private var mWidth = 0f

    fun addData(dataPoint: DataPoint) {
        mDataPoints.addPoint(dataPoint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mDrawablePoints.forEach {
            it.draw(canvas)
        }
        var x = 0
        mDrawablePoints = mDrawablePoints.sortedBy { it.x }
        while(x < mDrawablePoints.size - 1) {
            drawLine(canvas, mDrawablePoints[x].x, mDrawablePoints[x].y, mDrawablePoints[x + 1].x, mDrawablePoints[x++ + 1].y)
        }
    }

    private fun drawMainAxis(canvas: Canvas?) {
        canvas?.apply {
            drawLine(50f, 50f, 50f, height - 50f, mMainAxisPaint)
        }
    }

    private fun drawLine(canvas: Canvas?, startX: Float, startY: Float, stopX: Float, stopY: Float) {
        canvas?.drawLine(startX, startY, stopX, stopY, mMainAxisPaint)
    }

    fun setDim(height: Float, width: Float): Graph {
        mWidth = width
        mHeight = height
        return this
    }

    fun applyData() {
        mDrawablePoints = mDataPoints.setDim(mHeight, mWidth).mapToDrawablePoints()
        invalidate()
    }



    fun ping() {
        Toast.makeText(context, "${height}, ${width}",Toast.LENGTH_SHORT).show()
    }
}
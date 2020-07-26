package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.apusart.manta.R
import kotlinx.android.synthetic.main.graph.view.*
import java.util.*
import kotlin.collections.ArrayList
import com.apusart.manta.api.models.Result
import kotlin.math.*

class DataPoint(val result: Result) {
    var mX: Float = 0f
    var mY: Float = 0f
    constructor(x: Date, y: Float, result: Result): this(result) {
    }

    constructor(x: Float, y: Float, result: Result): this(result) {
        mX = x
        mY = y
    }
}

class DataPoints(val context: Context, private var mHeight: Float, private var mWidth: Float) {

    private val mDataPoints = ArrayList<DataPoint>()
    var verticalPadding: Float = 40f
    var horizontalPadding: Float = 40f

    fun addPoint(dataPoint: DataPoint) {
        mDataPoints.add(dataPoint)
    }

    fun removeAll() {
        mDataPoints.removeAll(mDataPoints)
    }

    fun setDim(height: Float, width: Float): DataPoints {
        mHeight = height
        mWidth = width
        return this
    }

    fun mapToDrawablePoints(): List<Point> {
        mDataPoints.sortBy { dp -> dp.mY }
        val maxWidth = mDataPoints.maxBy { it.mX }?.mX
        val minWidth = mDataPoints.minBy { it.mX }?.mX
        val w = maxWidth!! - minWidth!!
        val maxHeight = mDataPoints.minBy { it.mY }?.mY
        val minHeight = mDataPoints.maxBy { it.mY }?.mY
        val h = minHeight!! - maxHeight!!
        return mDataPoints.map {

            var x = (((it.mX - minWidth) /w) * (mWidth - (horizontalPadding * 2))) + horizontalPadding
            var y = (((it.mY - maxHeight)/h) * (mHeight - (verticalPadding * 2))) + verticalPadding

            if(x.isNaN()) x = 0.5f * mWidth
            if(y.isNaN()) y = 0.5f * mHeight

            val ret = Point(context)
            ret.params.leftMargin = x.toInt()
            ret.params.topMargin = y.toInt()
            ret.result = it.result
            return@map ret
        }
    }
}

class Point(context: Context): View(context) {

    companion object {
        val radius = 7.5f
    }

    val params =  RelativeLayout.LayoutParams((radius * 2).toInt(), (radius * 2).toInt())
    lateinit var result: Result
    init {
        val gradientDrawable = GradientDrawable()
        val strokeWidth = 14
        val color = Color.parseColor("#000000")
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setStroke(strokeWidth, color)
        background = gradientDrawable

    }


}

class Graph(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var mDrawablePoints = listOf<Point>()
    private val mDataPoints = DataPoints(context, height.toFloat(), width.toFloat())
    private var mHeight = 0f
    private var mWidth = 0f
    private val mGraph = LayoutInflater.from(context)
        .inflate(R.layout.graph, rootView as ViewGroup, false)
    private lateinit var mBitmap: Bitmap

    init {
        addView(mGraph)
    }

    fun resetData() {
        mDataPoints.removeAll()
    }

    fun addData(dataPoint: DataPoint) {
        mDataPoints.addPoint(dataPoint)
    }

    fun setDim(height: Float, width: Float): Graph {
        mWidth = width
        mHeight = height
        mBitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        return this
    }

    fun applyData() {
        mDrawablePoints = mDataPoints.setDim(mHeight, mWidth).mapToDrawablePoints()
        graph_graph.removeAllViews()

        mDrawablePoints.minBy { it.params.topMargin }?.background = ColorDrawable(resources.getColor(R.color.golden_rod))
        mDrawablePoints.forEach { point ->
            mGraph.graph_graph.addView(point, point.params)
        }

        val canvas = Canvas(mBitmap)
        val painter = Paint()
        painter.strokeWidth = 4f
        painter.color = resources.getColor(R.color.black)
        painter.style = Paint.Style.FILL
        drawLinesBetweenResults(canvas, painter)
        graph_imageView.setImageBitmap(mBitmap)

        mGraph.invalidate()
    }

    private fun drawLinesBetweenResults(canvas: Canvas?, painter: Paint) {

        mDrawablePoints = mDrawablePoints.sortedBy { it.params.leftMargin }
        val bests = ArrayList<Point>()
        var best = mDrawablePoints[0]
        bests.add(best)

        mDrawablePoints.forEach {
            if(it.params.topMargin < best.params.topMargin) {
                best = it
                bests.add(best)
            }
        }
        var x = 0
        while (x < bests.size - 2) {
            canvas?.drawLine(bests[x].params.leftMargin.toFloat() + Point.radius, bests[x].params.topMargin.toFloat() + Point.radius, bests[x + 1].params.leftMargin.toFloat() + Point.radius, bests[x++ + 1].params.topMargin.toFloat() + Point.radius, painter)
        }
        canvas?.drawLine(bests[x].params.leftMargin.toFloat() + Point.radius, bests[x].params.topMargin.toFloat() + Point.radius, bests[x + 1].params.leftMargin.toFloat() + Point.radius, bests[x++ + 1].params.topMargin.toFloat() + Point.radius, painter)

    }
}
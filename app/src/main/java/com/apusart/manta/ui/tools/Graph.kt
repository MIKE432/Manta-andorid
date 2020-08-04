package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.apusart.manta.R
import kotlinx.android.synthetic.main.graph.view.*
import java.util.*
import kotlin.collections.ArrayList
import com.apusart.manta.api.models.Result
import kotlinx.android.synthetic.main.result_details_fragment.*
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
    fun mSetBackground(c: Int = Color.parseColor("#000000")) {
        val gradientDrawable = GradientDrawable()
        val strokeWidth = radius.toInt() * 2
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setStroke(strokeWidth, c)
        background = gradientDrawable
    }

    init {
        mSetBackground()
    }
}

class Graph(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var mDrawablePoints = listOf<Point>()
    private val mDataPoints = DataPoints(context, height.toFloat(), width.toFloat())
    private var mHeight = 0f
    private var mWidth = 0f
    private val mGraph = LayoutInflater.from(context)
        .inflate(R.layout.graph, rootView as ViewGroup, false)
    private var mBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    private val mPainter = Paint()
    private val mLinesPainter = Paint()

    init {
        addView(mGraph)
        setUpPainters()
        graph_graph.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setDim(graph_graph.height.toFloat(), graph_graph.width.toFloat())
                graph_graph.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        graph_container.visibility = View.VISIBLE
        graph_spinner.isVisible = false
    }

    private fun setUpPainters() {
        mPainter.strokeWidth = 4f
        mPainter.color = resources.getColor(R.color.black)
        mPainter.style = Paint.Style.FILL

        mLinesPainter.strokeWidth = 2f
        mLinesPainter.color = resources.getColor(R.color.cool_grey)
        mLinesPainter.style = Paint.Style.FILL
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
        mDrawablePoints.forEach { point ->
            point.setOnClickListener {
                Toast.makeText(context, point.result.res_total_time, Toast.LENGTH_SHORT).show()
            }
        }
        graph_graph.removeAllViews()
        graph_axisYData.removeAllViews()
        graph_axisXData.removeAllViews()

        val bestRes = mDrawablePoints.minBy { it.params.topMargin }
        bestRes?.mSetBackground(Color.parseColor("#EEB507"))
        mDrawablePoints.forEach { point ->
            mGraph.graph_graph.addView(point, point.params)
        }

        val canvas = Canvas(mBitmap)
        if(mDrawablePoints.size > 1) {

            mDrawablePoints = mDrawablePoints.sortedBy { it.params.leftMargin }
            drawLinesBetweenResults(canvas, mPainter)
        }
        graph_imageView.setImageBitmap(mBitmap)
        drawLinesToPoints(canvas, mLinesPainter)
//        drawLinesForPoint(canvas, bestRes!!, mLinesPainter)
        mGraph.invalidate()

        graph_graph.visibility = View.VISIBLE
        graph_spinner.isVisible = false
    }

    private fun addTime(point: Point) {
        val textView = TextView(context)
        textView.text = Tools.convertResult(point.result.res_total_time?.toFloat())
        val params = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.topToTop = 0
        params.startToStart = 0
        params.endToEnd = 0
        params.topMargin = point.params.topMargin - 20
        textView.gravity = Gravity.START

        graph_axisYData.addView(textView, params)
        textView.setTextAppearance(R.style.CaptionRoboto12Pt)
    }

    private fun addDate(point: Point) {
        val textView = TextView(context)
        textView.text = point.result.mt_from
        val params = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.topToTop = 0
        params.startToStart = 0
        params.bottomToBottom = 0
        textView.gravity = Gravity.CENTER
        params.verticalBias = 0.3f
        params.leftMargin = point.params.leftMargin - 100

        if(point.params.leftMargin > 0.9 * graph_graph.width) {
            params.startToStart = -1
            params.endToEnd = 0
        }

        if(point.params.leftMargin < 0.1 * graph_graph.width) {
            params.startToStart = 0
            params.endToEnd = -1
            params.leftMargin = 0
        }

        textView.setTextAppearance(R.style.CaptionRoboto12Pt)
        graph_axisXData.addView(textView, params)
    }

    private fun pointDetails(canvas: Canvas, point: Point, painter: Paint) {
        canvas.drawLine(point.params.leftMargin.toFloat() + Point.radius, point.params.topMargin.toFloat() + Point.radius,
            point.params.leftMargin.toFloat() + Point.radius,  graph_graph.height.toFloat(), painter )
        canvas.drawLine(point.params.leftMargin.toFloat() + Point.radius, point.params.topMargin.toFloat() + Point.radius,
            0f,  point.params.topMargin.toFloat() + Point.radius, painter)
        addTime(point)
        addDate(point)

    }

    private fun drawLinesToPoints(canvas: Canvas, painter: Paint) {
        when(mDrawablePoints.size) {
            0 -> {}
            1 -> {
                pointDetails(canvas, mDrawablePoints[0], painter)
            }
            2 -> {
                pointDetails(canvas, mDrawablePoints[0], painter)
                pointDetails(canvas, mDrawablePoints[1], painter)
            }
            else -> {
                val first = mDrawablePoints[0]
                val last =  mDrawablePoints.last()
                val best = mDrawablePoints.minBy { it.params.topMargin }

                pointDetails(canvas, first, painter)
                pointDetails(canvas, best!!, painter)

                fun condition(point: Point): Boolean {
                    return (abs(point.params.leftMargin - first.params.leftMargin) > 400) &&
                            (abs(point.params.leftMargin - best.params.leftMargin) > 400)
                }


                if(condition(last))
                    pointDetails(canvas, last, painter)
                else {
                    mDrawablePoints.firstOrNull { condition(it) }.let {
                        if(it != null)
                            pointDetails(canvas, it, painter)
                    }
                }
            }
        }
    }

    private fun drawLinesBetweenResults(canvas: Canvas?, painter: Paint) {

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
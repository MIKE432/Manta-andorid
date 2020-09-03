package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.apusart.manta.R
import kotlinx.android.synthetic.main.empty_state_component.view.*

class EmptyStateComponent(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val mView = LayoutInflater.from(context)
        .inflate(R.layout.empty_state_component, this, false)
    init {
        addView(mView)
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.EmptyState,
            0,0
        ).apply {
            mView.empty_state_image.setImageDrawable(getDrawable(R.styleable.EmptyState_image))
            mView.empty_state_image.setColorFilter(resources.getColor(R.color.battleship_grey))
            mView.empty_state_text.text = getString(R.styleable.EmptyState_text)
            mView.empty_state_text.setTextColor(resources.getColor(R.color.battleship_grey))
        }
    }


    private var mScaleFactor = 1f

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= 2

            // Don't let the object get too small or too large.
            mScaleFactor = 0.1f.coerceAtLeast(Math.min(mScaleFactor, 5.0f))

            invalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev)
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            save()
            scale(mScaleFactor, mScaleFactor)
            // onDraw() code goes here

            restore()
        }
    }
}
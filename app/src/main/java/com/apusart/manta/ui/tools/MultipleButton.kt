package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import com.apusart.manta.R
import kotlinx.android.synthetic.main.graph.view.*
import java.lang.Exception
import kotlin.math.max

class MultipleButton(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val mChildButtons = ArrayList<ChildButton>()
    private val MAX_CHILDREN_COUNT = 5

    private inner class ChildButton: ConstraintLayout(context) {
        val textView = TextView(context)

        init {
            id = View.generateViewId()
        }

        private fun setupTextView() {
            textView.gravity = Gravity.CENTER
            textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            (textView.layoutParams as LayoutParams).topToTop = 0
            (textView.layoutParams as LayoutParams).startToStart = 0
            (textView.layoutParams as LayoutParams).endToEnd = 0
            (textView.layoutParams as LayoutParams).bottomToBottom = 0

            textView.setTextAppearance(R.style.ButtonRoboto14Pt)
            textView.setTextColor(resources.getColor(R.color.white))
        }

        fun setUpChild(text: String, onClickListener: ((View) -> Unit)? = null): ChildButton {
            this.textView.text = text
            setOnClickListener(onClickListener)
            layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT)
            (layoutParams as LayoutParams).topToTop = 0
            (layoutParams as LayoutParams).bottomToBottom = 0
            addView(textView)
            setupTextView()
            return this
        }
    }

    init {
        initParams()
        mChildButtons.add(ChildButton().setUpChild("1"))
        mChildButtons.add(ChildButton().setUpChild("2"))
        addChildren()
        setBackgrounds()

    }

    private fun initParams() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        (layoutParams as LayoutParams).marginStart = Tools.toDp(1, context)
        (layoutParams as LayoutParams).marginEnd = Tools.toDp(1, context)
    }

    private fun setupInnerChild(child: ChildButton, index: Int) {
        child.background = resources.getDrawable(R.drawable.multiple_button_inner_button)

        (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(2, context)

        (child.layoutParams as LayoutParams).startToEnd = mChildButtons[index - 1].id
        (child.layoutParams as LayoutParams).endToStart = mChildButtons[index + 1].id
        (child.layoutParams as LayoutParams).topToTop = 0
        (child.layoutParams as LayoutParams).bottomToBottom = 0
        child.requestLayout()
    }

    private fun setupOuterChild(child: ChildButton, background: Drawable, index: Int) {
        child.background = background
        (child.layoutParams as LayoutParams).marginStart = Tools.toDp(0, context)
        (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(0, context)

        if(index == 0) {
            (child.layoutParams as LayoutParams).startToStart = 0
            (child.layoutParams as LayoutParams).endToStart = mChildButtons[1].id
            (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(2, context)
        } else {
            (child.layoutParams as LayoutParams).endToEnd = 0
            (child.layoutParams as LayoutParams).startToEnd = mChildButtons[index - 1].id
        }

        (child.layoutParams as LayoutParams).topToTop = 0
        (child.layoutParams as LayoutParams).bottomToBottom = 0
        child.requestLayout()
    }


    private fun setBackgrounds() {
        mChildButtons.forEachIndexed { index, child ->
            when (index) {
                0 -> setupOuterChild(
                    child,
                    resources.getDrawable(R.drawable.multiple_button_first_button),
                    index
                )
                mChildButtons.size - 1 -> setupOuterChild(
                    child,
                    resources.getDrawable(R.drawable.multiple_button_last_button),
                    index
                )
                else -> setupInnerChild(child, index)
            }
        }
    }

    private fun addChildren() {
        mChildButtons.forEach {
            addView(it, it.layoutParams)
        }
    }

    fun addButton(text: String, onClickListener: ((View) -> Unit)? = null) {
        mChildButtons.takeIf { it.size < MAX_CHILDREN_COUNT && mChildButtons.find { child -> child.textView.text == text } == null }?.add(ChildButton().setUpChild(text, onClickListener))
        removeAllViews()
        setBackgrounds()
        addChildren()

    }

    fun removeButton(text: String) {
        mChildButtons.removeIf { it.textView.text == text }
    }

    fun removeButton(position: Int) {
        mChildButtons.takeIf { it.size > position }?.removeAt(position) ?: throw Exception("it.size < position")
        removeAllViews()
        setBackgrounds()
        addChildren()
        requestLayout()
    }

    fun setButtonOnClickListener(index: Int, onClickListener: ((View) -> Unit)?) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.setOnClickListener(onClickListener) ?: throw Exception("index > it.size")
    }

    fun setText(index: Int, text: String) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.textView?.text = text
    }
}
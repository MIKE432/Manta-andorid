package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import com.apusart.manta.R
import java.lang.Exception

class MultipleButton(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val mChildButtons = ArrayList<ChildButton>()
    private val MAX_CHILDREN_COUNT = 5

    private inner class ChildButton: ConstraintLayout(context) {
        val textView = TextView(context)

        init {
            id = View.generateViewId()
        }

        fun setUpChild(text: String, onClickListener: ((View) -> Unit)? = null): ChildButton {
            this.textView.text = text
            setOnClickListener(onClickListener)
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            addView(textView)
            return this
        }
    }

    init {
        initParams()
        mChildButtons.add(ChildButton().setUpChild("1") {
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
        })
        mChildButtons.add(ChildButton().setUpChild("2") {
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
        })
        addChildren()
        setBackgrounds()
    }

    private fun initParams() {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        (layoutParams as LayoutParams).marginStart = Tools.toDp(1, context)
        (layoutParams as LayoutParams).marginEnd = Tools.toDp(1, context)
    }

    private fun setupInnerChild(child: ChildButton, index: Int) {
        child.background = ColorDrawable(resources.getColor(R.color.black))
        (child.layoutParams as LayoutParams).marginStart = Tools.toDp(2, context)
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
                mChildButtons.lastIndex -> setupOuterChild(
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
        mChildButtons.takeIf { it.size < MAX_CHILDREN_COUNT }?.add(ChildButton().setUpChild(text, onClickListener)) ?: throw Exception("Max buttons count achieved")
        removeAllViews()
        addChildren()
        setBackgrounds()
        invalidate()
    }

    fun removeButton(text: String) {
        mChildButtons.removeIf { it.textView.text == text }
    }

    fun removeButton(position: Int) {
        mChildButtons.takeIf { it.size > position }?.removeAt(position) ?: throw Exception("it.size < position")
    }
}
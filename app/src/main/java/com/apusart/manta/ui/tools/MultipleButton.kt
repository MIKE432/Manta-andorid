package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView

import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
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
        val imageView = ImageView(context)
        private var mIsIconSet = false

        init {
            id = View.generateViewId()
            textView.id = View.generateViewId()
            imageView.id = View.generateViewId()
        }

        private fun setupTextView() {
            textView.gravity = Gravity.CENTER

            textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            (textView.layoutParams as LayoutParams).topToBottom = imageView.id
            (textView.layoutParams as LayoutParams).startToStart = 0
            (textView.layoutParams as LayoutParams).endToEnd = 0
            (textView.layoutParams as LayoutParams).bottomToBottom = 0

            textView.setTextAppearance(R.style.CaptionRoboto12Pt)
            textView.setTextColor(resources.getColor(R.color.nav_default_color))
        }

        private fun setupImageView(icon: Int?) {
            imageView.layoutParams = LayoutParams(Tools.toDp(24), Tools.toDp(24))
            (imageView.layoutParams as LayoutParams).topToTop = 0
            (imageView.layoutParams as LayoutParams).startToStart = 0
            (imageView.layoutParams as LayoutParams).endToEnd = 0

            if(icon == null)
                imageView.visibility = View.INVISIBLE
            else {
                imageView.visibility = View.VISIBLE
                imageView.setImageDrawable(Tools.changeIconColor(icon, R.color.nav_default_color, resources))
            }

        }

        fun setUpChild(text: String, icon: Int? = null, onClickListener: ((View) -> Unit)? = null): ChildButton {
            mIsIconSet = icon != null
            this.textView.text = text
            setOnClickListener(onClickListener)
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            setupImageView(icon)
            setupTextView()
            addView(textView, textView.layoutParams)
            addView(imageView, imageView.layoutParams)

            return this
        }

        fun setIcon(icon: Int) {
            mIsIconSet = true
            setupImageView(icon)
            setupTextView()
        }
    }

    init {
        initParams()
        mChildButtons.add(ChildButton().setUpChild("WWW", R.drawable.www_icon))
        mChildButtons.add(ChildButton().setUpChild("2"))
        addChildren()
        setBackgrounds()

    }

    private fun initParams() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        (layoutParams as LayoutParams).marginStart = Tools.toDp(1)
        (layoutParams as LayoutParams).marginEnd = Tools.toDp(1)
    }

    private fun setupInnerChild(child: ChildButton, index: Int) {
        child.background = resources.getDrawable(R.drawable.multiple_button_inner_button)

        (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(2)

        (child.layoutParams as LayoutParams).startToEnd = mChildButtons[index - 1].id
        (child.layoutParams as LayoutParams).endToStart = mChildButtons[index + 1].id
        (child.layoutParams as LayoutParams).topToTop = 0
        (child.layoutParams as LayoutParams).bottomToBottom = 0
        child.requestLayout()
    }

    private fun setupOuterChild(child: ChildButton, background: Drawable, index: Int) {
        child.background = background
        (child.layoutParams as LayoutParams).marginStart = Tools.toDp(0)
        (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(0)

        if(index == 0) {
            (child.layoutParams as LayoutParams).startToStart = 0
            (child.layoutParams as LayoutParams).endToStart = mChildButtons[1].id
            (child.layoutParams as LayoutParams).marginEnd = Tools.toDp(2)
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
        mChildButtons.takeIf { it.size < MAX_CHILDREN_COUNT && mChildButtons.find { child -> child.textView.text == text } == null }?.add(ChildButton().setUpChild(text, null, onClickListener))
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


    fun setButtonIcon(index: Int, icon: Int) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.setIcon(icon) ?: throw Exception("index > it.size")
    }



    fun setText(index: Int, text: String) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.textView?.text = text
    }
}
package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.ImageView

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.apusart.manta.R
import java.lang.Exception

class MultipleButton(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val mChildButtons = ArrayList<ChildButton>()
    private val MAX_CHILDREN_COUNT = 5


    private inner class ChildButton: ConstraintLayout(context) {
        val mTextView = TextView(context)
        val mImageView = ImageView(context)
        private var mIsIconSet = false
        var mIsActive = true
        private var mIcon = 0
        init {
            id = View.generateViewId()
            mTextView.id = View.generateViewId()
            mImageView.id = View.generateViewId()
            background = ColorDrawable(resources.getColor(R.color.cool_grey))
        }

        private fun setupTextView() {
            mTextView.gravity = Gravity.CENTER
            mTextView.ellipsize = TextUtils.TruncateAt.END
            mTextView.setLines(1)
            mTextView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            (mTextView.layoutParams as LayoutParams).topToBottom = mImageView.id
            (mTextView.layoutParams as LayoutParams).startToStart = 0
            (mTextView.layoutParams as LayoutParams).endToEnd = 0
            (mTextView.layoutParams as LayoutParams).bottomToBottom = 0

            mTextView.setTextAppearance(R.style.CaptionRoboto12Pt)
            mTextView.setTextColor(resources.getColor(R.color.black))
        }

        private fun setupImageView(icon: Int?) {
            mIcon = icon ?: 0
            mImageView.layoutParams = LayoutParams(Tools.toDp(24), Tools.toDp(24))
            (mImageView.layoutParams as LayoutParams).topToTop = 0
            (mImageView.layoutParams as LayoutParams).startToStart = 0
            (mImageView.layoutParams as LayoutParams).endToEnd = 0
            (mImageView.layoutParams as LayoutParams).bottomToBottom = 0

            if(icon == null)
                mImageView.visibility = View.INVISIBLE
            else {
                mImageView.visibility = View.VISIBLE
                mImageView.setImageDrawable(Tools.changeIconColor(icon, R.color.cool_grey, resources))
            }

        }

        fun setUpChild(text: String, icon: Int? = null, onClickListener: ((View) -> Unit)? = null): ChildButton {
            mIsIconSet = icon != null
            this.mTextView.text = text
            setOnClickListener(onClickListener)
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            setupImageView(icon)
            setupTextView()
            addView(mImageView, mImageView.layoutParams)

            return this
        }

        fun setIcon(icon: Int) {
            mIsIconSet = true
            setupImageView(icon)
            setupTextView()
        }

        fun setBackGround(background: Int) {
            if(mIcon != 0) {
                mImageView.setImageDrawable(Tools.changeIconColor(mIcon, background, resources))
            }
            isClickable = false
            mIsActive = false

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
    }

    private fun setupInnerChild(child: ChildButton, index: Int) {
        child.background = resources.getDrawable(R.drawable.multiple_button_inner_button)

        (child.layoutParams as LayoutParams).startToEnd = mChildButtons[index - 1].id
        (child.layoutParams as LayoutParams).endToStart = mChildButtons[index + 1].id
        (child.layoutParams as LayoutParams).topToTop = 0
        (child.layoutParams as LayoutParams).bottomToBottom = 0

    }

    private fun setupOuterChild(child: ChildButton, background: Drawable, index: Int) {
        child.background = background

        if(index == 0) {
            (child.layoutParams as LayoutParams).startToStart = 0
            (child.layoutParams as LayoutParams).endToStart = mChildButtons[1].id
        } else {
            (child.layoutParams as LayoutParams).endToEnd = 0
            (child.layoutParams as LayoutParams).startToEnd = mChildButtons[index - 1].id
        }

        (child.layoutParams as LayoutParams).topToTop = 0
        (child.layoutParams as LayoutParams).bottomToBottom = 0

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
        requestLayout()
    }

    private fun addChildren() {
        mChildButtons.forEach {
            addView(it, it.layoutParams)
        }
    }

    fun addButton(text: String, onClickListener: ((View) -> Unit)? = null) {
        mChildButtons.takeIf { it.size < MAX_CHILDREN_COUNT && mChildButtons.find { child -> child.mTextView.text == text } == null }?.add(ChildButton().setUpChild(text, null, onClickListener))
        removeAllViews()
        setBackgrounds()
        addChildren()

    }

    fun removeButton(text: String) {
        mChildButtons.removeIf { it.mTextView.text == text }
    }

    fun removeButton(position: Int) {
        mChildButtons.takeIf { it.size > position }?.removeAt(position) ?: throw Exception("it.size < position")
        removeAllViews()
        setBackgrounds()
        addChildren()
        requestLayout()
    }

    fun setButtonOnClickListener(index: Int, onClickListener: ((View) -> Unit)?) {
        val child = mChildButtons.takeIf { index < it.size }?.get(index)
        if(child?.hasOnClickListeners() != null) {
            child.setOnClickListener(onClickListener) ?: throw Exception("index > it.size")
        }
    }


    fun setButtonIcon(index: Int, icon: Int) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.setIcon(icon) ?: throw Exception("index > it.size")
    }


    fun setText(index: Int, text: String) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.mTextView?.text = text
    }

    fun setToInactive(index: Int, background: Int) {
        mChildButtons.takeIf { index < it.size }?.get(index)?.setBackGround(background)
        setBackgrounds()
    }
}
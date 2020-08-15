package com.apusart.manta.ui.tools

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
            mView.empty_state_text.text = getString(R.styleable.EmptyState_text)
        }
    }
}
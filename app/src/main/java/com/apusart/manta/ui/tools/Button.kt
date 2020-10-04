package com.apusart.manta.ui.tools

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.apusart.manta.R
import kotlinx.android.synthetic.main.button.view.*
import java.lang.Exception

class Button(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {
    private val mView = LayoutInflater.from(context)
        .inflate(R.layout.button, this, false)

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.MantaButton,
            0,0
        ).apply {

            if(getString(R.styleable.MantaButton_button_title) == "")
                throw Exception("No button title")
            else {
                mView.button_subtitle.text = getString(R.styleable.MantaButton_button_subtitle)
            }

            val title = getString(R.styleable.MantaButton_button_title)
            val subtitle = getString(R.styleable.MantaButton_button_subtitle)
            val image = getDrawable(R.styleable.MantaButton_button_image)

            if(title == "" ||  title == null)
                throw Exception("No button title")
            else {
                mView.button_title.text = getString(R.styleable.MantaButton_button_title)
            }

            if(subtitle == "" ||  subtitle == null) {
                mView.button_subtitle.visibility = View.GONE
            } else {
                mView.button_subtitle.text = getString(R.styleable.MantaButton_button_subtitle)
            }

            if(image == null) {
                mView.button_imageView.visibility = View.GONE
            } else {
                mView.button_imageView.setImageDrawable(image)
            }

            if((subtitle == "" ||  subtitle == null) && (image == null)) {
                mView.button_title.gravity = Gravity.CENTER
            }

            addView(mView)
        }
    }
}
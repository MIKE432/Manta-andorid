package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import com.apusart.manta.R

class Navigation(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        background = ColorDrawable(resources.getColor(R.color.colorPrimary))
        val textView = TextView(context)
        textView.text = "!3231321321"
        setPadding( 0, Tools.toDp(8, context), 0,Tools.toDp(12, context))
        addView(NavItem(R.drawable.dashboard_icon, "Aktualności"))
    }

    private inner class NavItem(val src: Int, val text: String): LinearLayout(context) {
        val imageView = ImageView(context)
        val textView = TextView(context)

        init {
            orientation = VERTICAL
            imageView.setImageResource(src)
            imageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Tools.toDp(24, context))
            textView.text = text
            imageView
            textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            textView.gravity = Gravity.CENTER
            addView(imageView)
            addView(textView)
        }
    }
}
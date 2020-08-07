package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import com.apusart.manta.R

class Navigation(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        background = ColorDrawable(resources.getColor(R.color.colorPrimary))
        setPadding( 0, Tools.toDp(8), 0,Tools.toDp(6))
        val x1 = NavItem(R.drawable.home_icon, "Aktualności")
        val x2 = NavItem(R.drawable.swimmer_icon, "Aktualności")
        val x3 = NavItem(R.drawable.plot_icon, "Aktualności")
        val x4 = NavItem(R.drawable.profile_icon, "Aktualności")

        (x1.layoutParams as LayoutParams).topToTop = 0

        (x1.layoutParams as LayoutParams).bottomToBottom = 0
        (x1.layoutParams as LayoutParams).startToStart = 0
        (x1.layoutParams as LayoutParams).endToStart = x2.id

        (x2.layoutParams as LayoutParams).topToTop = 0

        (x2.layoutParams as LayoutParams).bottomToBottom = 0
        (x2.layoutParams as LayoutParams).startToEnd = x1.id
        (x2.layoutParams as LayoutParams).endToStart = x3.id

        (x3.layoutParams as LayoutParams).topToTop = 0

        (x3.layoutParams as LayoutParams).bottomToBottom = 0
        (x3.layoutParams as LayoutParams).startToEnd = x2.id
        (x3.layoutParams as LayoutParams).endToStart = x4.id

        (x4.layoutParams as LayoutParams).topToTop = 0

        (x4.layoutParams as LayoutParams).bottomToBottom = 0
        (x4.layoutParams as LayoutParams).startToEnd = x3.id
        (x4.layoutParams as LayoutParams).endToEnd = 0

        addView(x1)
        addView(x2)
        addView(x3)
        addView(x4)
    }

    private inner class NavItem(val src: Int, val text: String): LinearLayout(context) {
        val imageView = ImageView(context)
        val textView = TextView(context)

        init {
            gravity = Gravity.CENTER
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            orientation = VERTICAL
            setSelected()
            maxWidth = Tools.toDp(130)
            minWidth = Tools.toDp(68)
            imageView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Tools.toDp(24))
            textView.text = text
            textView.setTextColor(resources.getColor(R.color.white))
            textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            textView.gravity = Gravity.CENTER
            textView.setTextAppearance(R.style.CaptionRoboto12PtBlack)
            textView.setTextColor(resources.getColor(R.color.white))
            addView(imageView)
            addView(textView)
        }

        fun setSelected() {
            imageView.setImageDrawable(Tools.changeIconColor(src, R.color.white, resources))
        }
    }
}
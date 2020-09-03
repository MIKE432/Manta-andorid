package com.apusart.manta.ui.tools

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPagerExtension(context: Context, attributesSet: AttributeSet): ViewPager(context, attributesSet) {

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        var x = true
        try {
            x = super.onTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return x
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

}
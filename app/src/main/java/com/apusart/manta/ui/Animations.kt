package com.apusart.manta.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object Animations {
    fun slideView(view: View, currentHeight: Int, newHeight: Int) {

        val slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight).setDuration(250)

        slideAnimator.addUpdateListener { animation ->
            val value = animation.getAnimatedValue() as Int
            view.layoutParams.height = value

            view.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.setInterpolator(object : AccelerateDecelerateInterpolator(){})
        animatorSet.play(slideAnimator)
        animatorSet.start()
    }
}
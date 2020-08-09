package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.apusart.manta.R
import kotlinx.android.synthetic.main.nav_bar.view.*
import kotlinx.android.synthetic.main.user_activity.*

class Navigation(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val nav = LayoutInflater.from(context)
        .inflate(R.layout.nav_bar, this, false)
    private lateinit var navCtrl: NavController

    init {
        addView(nav)
    }

    private fun applyOnClickListeners() {
        nav.nav_bar_n1.setOnClickListener {
            navCtrl.navigate(R.id.dashboardFragment)
        }

        nav.nav_bar_n2.setOnClickListener {
            navCtrl.navigate(R.id.meetsFragment)
        }

        nav.nav_bar_n3.setOnClickListener {
            navCtrl.navigate(R.id.recordsFragment)
        }

        nav.nav_bar_n4.setOnClickListener {
            navCtrl.navigate(R.id.profileFragment)
        }
    }

    private fun setSelected(item: Int) {
        when(item) {
            0 ->  {
                nav.nav_bar_n1_image.setImageDrawable(Tools.changeIconColor(R.drawable.home_icon, R.color.white, resources))
                nav.nav_bar_n1_text.setTextColor(resources.getColor(R.color.white))
                //unselect
                nav.nav_bar_n2_image.setImageDrawable(Tools.changeIconColor(R.drawable.swimmer_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n2_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n3_image.setImageDrawable(Tools.changeIconColor(R.drawable.plot_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n3_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n4_image.setImageDrawable(Tools.changeIconColor(R.drawable.profile_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n4_text.setTextColor(resources.getColor(R.color.nav_default_color))
            }
            1 ->  {
                nav.nav_bar_n2_image.setImageDrawable(Tools.changeIconColor(R.drawable.swimmer_icon, R.color.white, resources))
                nav.nav_bar_n2_text.setTextColor(resources.getColor(R.color.white))

                nav.nav_bar_n1_image.setImageDrawable(Tools.changeIconColor(R.drawable.home_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n1_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n3_image.setImageDrawable(Tools.changeIconColor(R.drawable.plot_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n3_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n4_image.setImageDrawable(Tools.changeIconColor(R.drawable.profile_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n4_text.setTextColor(resources.getColor(R.color.nav_default_color))
            }
            2 ->  {
                nav.nav_bar_n3_image.setImageDrawable(Tools.changeIconColor(R.drawable.plot_icon, R.color.white, resources))
                nav.nav_bar_n3_text.setTextColor(resources.getColor(R.color.white))

                nav.nav_bar_n1_image.setImageDrawable(Tools.changeIconColor(R.drawable.home_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n1_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n2_image.setImageDrawable(Tools.changeIconColor(R.drawable.swimmer_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n2_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n4_image.setImageDrawable(Tools.changeIconColor(R.drawable.profile_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n4_text.setTextColor(resources.getColor(R.color.nav_default_color))
            }
            3 ->  {
                nav.nav_bar_n4_image.setImageDrawable(Tools.changeIconColor(R.drawable.profile_icon, R.color.white, resources))
                nav.nav_bar_n4_text.setTextColor(resources.getColor(R.color.white))

                nav.nav_bar_n1_image.setImageDrawable(Tools.changeIconColor(R.drawable.home_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n1_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n3_image.setImageDrawable(Tools.changeIconColor(R.drawable.plot_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n3_text.setTextColor(resources.getColor(R.color.nav_default_color))

                nav.nav_bar_n2_image.setImageDrawable(Tools.changeIconColor(R.drawable.swimmer_icon, R.color.nav_default_color, resources))
                nav.nav_bar_n2_text.setTextColor(resources.getColor(R.color.nav_default_color))
            }
        }
    }

    private fun destinationChangedCallback(destination: NavDestination?) {
        when(destination?.id) {
            R.id.profileFragment -> {
                if(!nav.isVisible)
                    nav.isVisible = true
                setSelected(3)
            }
            R.id.meetsFragment -> {
                if(!nav.isVisible)
                    nav.isVisible = true
                setSelected(1)
            }
            R.id.recordsFragment -> {
                if(!nav.isVisible)
                    nav.isVisible = true
                setSelected(2)
            }
            R.id.dashboardFragment -> {
                if(!nav.isVisible)
                    nav.isVisible = true
                setSelected(0)
            }
            else -> {
                nav.isVisible = false
            }
        }
    }

    private fun applyDestinationChangedCallback() {
        navCtrl.addOnDestinationChangedListener { controller, destination, arguments ->
            destinationChangedCallback(destination)
        }
    }

    fun applyNavController(navController: NavController) {
        navCtrl = navController
        applyOnClickListeners()
        destinationChangedCallback(navController.currentDestination)
        applyDestinationChangedCallback()
    }

}
package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.apusart.manta.R
import kotlinx.android.synthetic.main.multiple_button_item.view.*
import kotlinx.android.synthetic.main.nav_bar.view.*
import kotlinx.android.synthetic.main.user_activity.*
import kotlin.properties.Delegates


class NavItem(val icon: Int, val text: String, val destinationId: Int, val onClick: () -> Unit)

class Navigation1(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private lateinit var mNavigationItemsList: List<NavItemView>
    private val mNavigationItemsSet = HashMap<Int, NavItemView>()
    private lateinit var mNavController: NavController
    private lateinit var mChangeDestinationCallback: (NavController, NavDestination?, Bundle?) -> Unit


    init {
        background = ResourcesCompat.getDrawable(resources, R.color.primary_900, null)
        setItems(listOf())
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun setActive(destinationId: Int?) {
        mNavigationItemsList.takeIf { it.isNotEmpty() }?.forEach {
            if(it.mNavItem.destinationId == destinationId) {
                it.setItemActive()
            } else {
                it.setItemInactive()
            }
        }
    }

    fun setUpNavigation(items: List<NavItem>, navController: NavController) {
        mNavController = navController
        setItems(items)

        mChangeDestinationCallback = { _, destination, _ ->
            setActive(destination?.id)
            var isOneOfNavs = false

            mNavigationItemsList.forEach {
                if(it.mNavItem.destinationId == destination?.id) {
                    isOneOfNavs = true
                }
            }

            isVisible = isOneOfNavs
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            mChangeDestinationCallback(controller, destination, arguments)
        }
    }

    private inner class NavItemView(context: Context): LinearLayout(context) {
        private val mImageView = ImageView(context)
        private val mTextView = TextView(context)
        lateinit var mNavItem: NavItem

        init {
            id = View.generateViewId()
            orientation = VERTICAL
            setPadding(0, Tools.toDp(8), 0, Tools.toDp(6))
            val imageParams = LayoutParams(Tools.toDp(24), Tools.toDp(24))
            imageParams.gravity = Gravity.CENTER
            mImageView.layoutParams = imageParams
            mImageView.setColorFilter(ContextCompat.getColor(context, R.color.nav_default_color))

            val textParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            textParams.gravity = Gravity.CENTER
            mTextView.setPadding(Tools.toDp(10), 0, Tools.toDp(10), 0 )
            mTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.nav_default_color, null))
            mTextView.ellipsize = TextUtils.TruncateAt.END
            mTextView.setLines(1)
            mTextView.gravity = Gravity.CENTER
            mTextView.layoutParams = textParams
            mTextView.setTextAppearance(R.style.CaptionRoboto12Pt)
        }

        fun setItemActive() {

            mImageView.setColorFilter(ContextCompat.getColor(context, R.color.white))
            mTextView.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        fun setItemInactive() {

            mImageView.setColorFilter(ContextCompat.getColor(context, R.color.nav_default_color))
            mTextView.setTextColor(ContextCompat.getColor(context, R.color.nav_default_color))
//            invalidate()
        }

        fun setUpChild(item: NavItem): NavItemView {
            mTextView.text = item.text
            mImageView.setImageResource(item.icon)
            addView(mImageView)
            addView(mTextView)
            mNavItem = item
            setOnClickListener {
                item.onClick()
            }

            requestLayout()
            return this
        }
    }

    private fun setItems(items: List<NavItem>) {
        mNavigationItemsList = items.map { NavItemView(context).setUpChild(it) }

        if(mNavigationItemsList.isNotEmpty()) {
            mNavigationItemsList.forEachIndexed { index, navItemView ->
                mNavigationItemsSet[navItemView.mNavItem.destinationId] = navItemView
                val params = LayoutParams(0, LayoutParams.WRAP_CONTENT)
                when(index) {
                    0 -> {
                        params.leftToLeft = 0
                        if(index == mNavigationItemsList.lastIndex) {
                            params.rightToLeft = 0
                        } else {
                            params.rightToLeft = mNavigationItemsList[index + 1].id
                        }
                        params.topToTop = 0
                        params.bottomToBottom = 0
                    }
                    mNavigationItemsList.lastIndex -> {
                        params.leftToRight = mNavigationItemsList[index - 1].id
                        params.rightToRight = 0
                        params.topToTop = 0
                        params.bottomToBottom = 0
                    }
                    else -> {
                        params.leftToRight = mNavigationItemsList[index - 1].id
                        params.rightToLeft = mNavigationItemsList[index + 1].id
                        params.topToTop = 0
                        params.bottomToBottom = 0
                    }
                }
                addView(navItemView, params)

            }
        }
    }
}

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
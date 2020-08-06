package com.apusart.manta

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.Animations
import com.apusart.manta.ui.MedalStatsViewModel
import com.apusart.manta.ui.pick_athlete_module.PickAthleteActivity
import com.apusart.manta.ui.pick_athlete_module.WelcomeActivity
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_activity.*


class InitialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.AthletePreference(applicationContext)

        if(Prefs.getUser() != null) {
           startActivity(Intent(this, UserActivity::class.java)
               .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        } else {
            startActivity(Intent(this, WelcomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        finish()
    }
}

class UserActivity: AppCompatActivity(R.layout.user_activity) {
    private var mUser: Athlete? = null
    private var mUserMenuStatus = false
    private var mMenuHeight = 0
    private val mMedalStatsViewModel: MedalStatsViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
//
//        val navController = findNavController(R.id.logged_athlete_navigation_host)
//
//        val x = bottom_navigation.selectedItemId
//        when(navController.currentDestination?.id) {
//            R.id.profileFragment -> { bottom_navigation.selectedItemId = R.id.navigation_dashboard }
//            R.id.meetsFragment -> { bottom_navigation.selectedItemId = R.id.navigation_dashboard }
//            R.id.recordsFragment -> { bottom_navigation.selectedItemId = R.id.navigation_dashboard }
//            R.id.dashboardFragment -> finish()
//            else ->  {  }
//        }

        super.onBackPressed()
    }

    private fun handleMenu() {
        if(mUserMenuStatus.also { mUserMenuStatus = !mUserMenuStatus }) {
            Animations.slideView(user_menu, mMenuHeight, 0)
            app_bar_out.isVisible = false
        } else {
            Animations.slideView(user_menu, 0, mMenuHeight)

            app_bar_out.isVisible = true
            app_bar_out.bringToFront()
        }
    }

    override fun onStop() {
        super.onStop()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.AthletePreference(applicationContext)
        mUser = Prefs.getUser()
        user_name.text = getString(R.string.user_name, mUser?.ath_lastname, mUser?.ath_firstname)
        user_licence_no.text = getString(R.string.licence_no, if(mUser?.ath_licence_no != "") mUser?.ath_licence_no else "(Brak)")

        user_menu.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mMenuHeight = user_menu.measuredHeight
                user_menu.layoutParams.height = 0
                user_menu.requestLayout()
                user_menu.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        Glide
            .with(this)
            .load(Const.baseUrl + mUser?.ath_image_min_url)
            .apply(Const.glideAthleteIconOptions)
            .into(user_image)


        user_information_container.setOnClickListener {
            handleMenu()
        }

        user_menu_change_user_button.setOnClickListener {
            Prefs.removeUser()
            startActivity(Intent(this, PickAthleteActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }

        app_bar_out.setOnClickListener {
            handleMenu()
        }

        val navController = findNavController(R.id.logged_athlete_navigation_host)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id) {
                R.id.profileFragment -> {
                    if(!bottom_navigation.isVisible)
                        bottom_navigation.isVisible = true
                }
                R.id.meetsFragment -> {
                    if(!bottom_navigation.isVisible)
                        bottom_navigation.isVisible = true
                }
                R.id.recordsFragment -> {
                    if(!bottom_navigation.isVisible)
                        bottom_navigation.isVisible = true
                }
                R.id.dashboardFragment -> {
                    if(!bottom_navigation.isVisible)
                        bottom_navigation.isVisible = true
                }
                else -> {
                    bottom_navigation.isVisible = false
                }
            }
        }

        bottom_navigation.setOnNavigationItemReselectedListener {}
        bottom_navigation.setOnNavigationItemSelectedListener { item ->

            if(mUserMenuStatus) {
                handleMenu()
            }

            when(item.itemId) {
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.dashboardFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_competition -> {
                    navController.navigate(R.id.meetsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_records -> {
                    navController.navigate(R.id.recordsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.profileFragment)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener true
            }

        }

    }
}

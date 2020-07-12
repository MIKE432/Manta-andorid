package com.apusart.manta

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.Animations
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.personal_best_item.view.*
import kotlinx.android.synthetic.main.user_activity.*


class PickAthleteActivity : AppCompatActivity(R.layout.activity_main) {}

class InitialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.AthletePreference(applicationContext)

        if(Prefs.getUser() != null) {
           startActivity(Intent(this, UserActivity::class.java)
               .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        } else {
            startActivity(Intent(this, PickAthleteActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        finish()
    }
}

class UserActivity: AppCompatActivity(R.layout.user_activity) {
    private var user: Athlete? = null
    private var userMenuStatus = false
    private var mMenuHeight = 0

    private fun handleMenu() {
        if(userMenuStatus.also { userMenuStatus = !userMenuStatus }) {
            Animations.slideView(user_menu, mMenuHeight, 0)
            app_bar_out.isVisible = false
        } else {
            Animations.slideView(user_menu, 0, mMenuHeight)

            app_bar_out.isVisible = true
            app_bar_out.bringToFront()
        }
    }

    override fun onBackPressed() {
        when(findNavController(R.id.logged_athlete_navigation_host).currentDestination?.id) {
            R.id.meetsFragment -> finish()
            R.id.articlesFragment -> finish()
            R.id.recordsFragment -> finish()
            R.id.profileFragment -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.AthletePreference(applicationContext)
        user = Prefs.getUser()
        user_firstname.text = user?.ath_firstname
        user_lastname.text = user?.ath_lastname

        user_menu.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mMenuHeight = user_menu.measuredHeight
                user_menu.layoutParams.height = 0
                user_menu.requestLayout()
                user_menu.viewTreeObserver.removeOnGlobalLayoutListener(this);
            }
        })

        Glide
            .with(this)
            .load(Const.baseUrl + user?.ath_image_min_url)
            .apply(Const.glideAthleteIconOptions)
            .into(user_image)

        user_information_container.setOnClickListener {
            handleMenu()
        }

        user_menu_change_user_button.setOnClickListener {
            Prefs.removeUser()
            startActivity(Intent(this, PickAthleteActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            finish()
        }

        app_bar_out.setOnClickListener {
            handleMenu()
        }
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            val navController = findNavController(R.id.logged_athlete_navigation_host)
            if(userMenuStatus) {
                handleMenu()
            }
            when(item.itemId) {
                R.id.navigation_competition -> {
                    navController.navigate(R.id.meetsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_articles -> {
                    navController.navigate(R.id.articlesFragment)
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
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }
}
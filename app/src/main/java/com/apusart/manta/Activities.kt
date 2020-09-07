package com.apusart.manta

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.Animations
import com.apusart.manta.ui.MedalStatsViewModel
import com.apusart.manta.ui.pick_athlete_module.PickAthleteActivity
import com.apusart.manta.ui.pick_athlete_module.WelcomeActivity
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.user_activity.*
import kotlinx.coroutines.launch
import java.lang.Exception


class InitialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.athletePreference(applicationContext)

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

class UserActivityViewModel: ViewModel() {
    val theme = MutableLiveData<Int>()


    fun setTheme(t: Int) {
        theme.value = t
    }
}

class UserActivity: AppCompatActivity() {
    private var mUser: Athlete? = null
    private var mUserMenuStatus = false
    private var mMenuHeight = 0
    private val mMedalStatsViewModel: MedalStatsViewModel by viewModels()
    private val mUserActivityViewModel: UserActivityViewModel by viewModels()

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

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_in_left, 0)
        val x = Prefs.getCurrentTheme()

        if(x != mUserActivityViewModel.theme.value) {
            mUserActivityViewModel.setTheme(x)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("stop", "stop")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onPause() {
        super.onPause()
        Log.d("pause", "pause")
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.removeLastMeetId()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        super.onCreate(savedInstanceState)
        val x = Prefs.getCurrentTheme()
        setTheme(if(x == 1) R.style.Manta_Theme_Dark else R.style.Manta_Theme_Light)
        setContentView(R.layout.user_activity)
        bottom_navigation.applyNavController(findNavController(R.id.logged_athlete_navigation_host))

        Prefs.athletePreference(applicationContext)
        mUser = Prefs.getUser()
        user_name.text = getString(R.string.user_name, mUser?.ath_lastname, mUser?.ath_firstname)
        user_licence_no.text = getString(R.string.licence_no, if(mUser?.ath_licence_no != "") mUser?.ath_licence_no else "(Brak)")
        if(mUser?.ath_birth_year!! > 1000)
            athlete_birth_year.text = "'${mUser?.ath_birth_year.toString().substring(2,4)}"

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

        user_menu_about.setOnClickListener {
            findNavController(R.id.logged_athlete_navigation_host)
                .navigate(R.id.about)
            handleMenu()
        }

        user_menu_settings.setOnClickListener {
            findNavController(R.id.logged_athlete_navigation_host)
                .navigate(R.id.settingsActivity)
            handleMenu()
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
    }
}

class SettingsActivity: Fragment(R.layout.settings_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        theme_switcher.setOnCheckedChangeListener { buttonView, isChecked ->
            Prefs.toggleCurrentTheme(isChecked)
            activity?.recreate()
        }
    }
}

package com.apusart.manta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.apusart.manta.api.models.Athlete
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pick_athlete_item.view.*
import kotlinx.android.synthetic.main.user_activity.*


class PickAthleteActivity : AppCompatActivity(R.layout.activity_main) {

}

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

        Glide
            .with(this)
            .load(Const.baseUrl + user?.ath_image_min_url)
            .into(user_image)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            val navController = findNavController(R.id.logged_athlete_navigation_host)
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
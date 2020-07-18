package com.apusart.manta.ui.pick_athlete_module

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apusart.manta.R
import kotlinx.android.synthetic.main.welcome_fragment.*

class WelcomeActivity: AppCompatActivity(R.layout.welcome_fragment) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        welcome_fragment.setOnClickListener {
            startActivity(Intent(this, PickAthleteActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

        }
    }
}
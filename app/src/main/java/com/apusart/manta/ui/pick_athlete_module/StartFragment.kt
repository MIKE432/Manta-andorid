package com.apusart.manta.ui.pick_athlete_module

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.apusart.manta.R
import com.apusart.manta.ui.club_scope_module.ClubScopeActivity
import com.apusart.manta.ui.pick_athlete_module.pick_athlete.PickAthleteActivity
import com.apusart.manta.ui.tools.Prefs
import kotlinx.android.synthetic.main.pick_scope_fragment.*

class StartActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if(Prefs.getCurrentTheme() == 1) R.style.Manta_Theme_Dark else R.style.Manta_Theme_Light)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_scope_fragment)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        pick_scope_pick_athlete_button.setOnClickListener {
            startActivity(Intent(this, PickAthleteActivity::class.java))
        }

        pick_scope_club_scope_button.setOnClickListener {
            startActivity(Intent(this, ClubScopeActivity::class.java))
        }
    }
}

class PickScopeFragment: Fragment(R.layout.pick_scope_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pick_scope_pick_athlete_button.setOnClickListener {
            startActivity(Intent(this.context, PickAthleteActivity::class.java))
        }

        pick_scope_club_scope_button.setOnClickListener {
            startActivity(Intent(this.context, ClubScopeActivity::class.java))
        }
    }
}
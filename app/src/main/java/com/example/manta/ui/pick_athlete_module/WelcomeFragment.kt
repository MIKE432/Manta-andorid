package com.example.manta.ui.pick_athlete_module

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.manta.Prefs
import com.example.manta.R
import kotlinx.android.synthetic.main.welcome_fragment.*

class WelcomeFragment: Fragment(R.layout.welcome_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcome_fragment_pick_athlete_button.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_pickAthleteFragment)
        }
    }
}
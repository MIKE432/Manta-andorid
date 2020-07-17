package com.apusart.manta.ui.tools

import android.view.View
import androidx.navigation.NavController

class Navigation(val navController: NavController, val bottomBar: View) {
    fun xd() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->  }
    }
}
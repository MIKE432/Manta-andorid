package com.apusart.manta.ui.club_scope_module

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.apusart.manta.R
import com.apusart.manta.ui.club_scope_module.ui.club_records.ClubRecordsViewModel
import com.apusart.manta.ui.tools.AppViewModelFactory
import com.apusart.manta.ui.tools.NavItem
import com.apusart.manta.ui.tools.Prefs
import kotlinx.android.synthetic.main.club_scope_activity.*

class ClubScopeActivity: AppCompatActivity() {
    private val recordsViewModel: ClubRecordsViewModel by viewModels { AppViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if(Prefs.getCurrentTheme() == 1) R.style.Manta_Theme_Dark else R.style.Manta_Theme_Light)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.club_scope_activity)

        val navItemList = ArrayList<NavItem>()

        navItemList.add(NavItem(R.drawable.articles_icon, "Artykuły", R.id.clubArticles) {
            findNavController(R.id.club_scope_navigation_host).navigate(R.id.clubArticles)
        })

        navItemList.add(NavItem(R.drawable.plot_icon, "Rekordy", R.id.clubRecords) {
            findNavController(R.id.club_scope_navigation_host).navigate(R.id.clubRecords)
        })

        navItemList.add(NavItem(R.drawable.swimmer_icon, "Zawody", R.id.clubMeets) {
            findNavController(R.id.club_scope_navigation_host).navigate(R.id.clubMeets)
        })

        club_scope_nav_bar.setUpNavigation(navItemList, findNavController(R.id.club_scope_navigation_host))
    }
}
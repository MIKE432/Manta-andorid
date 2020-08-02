package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.navArgs
import com.apusart.manta.R
import com.apusart.manta.ui.user_module.profile.MedalStatisticsFragment
import kotlinx.android.synthetic.main.results_fragment.*
import java.lang.Exception

class ResultsFragment: Fragment(R.layout.results_fragment) {
    private lateinit var specificSectionAdapter: UserResultsAdapter
    private val navArgs by navArgs<ResultsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specificSectionAdapter =
            UserResultsAdapter(childFragmentManager)

        results_fragment_view_pager.apply {
            adapter = specificSectionAdapter
            currentItem = navArgs.openOnPage
        }

    }
}

class UserResultsAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val COUNT = 3

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MostValuableResultsFragment()
            1 -> MedalStatisticsFragment()
            2 -> PersonalBestFragment()
            else -> throw Exception("Unsupported fragment")
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Najwartościowsze wyniki"
            1 -> "Statystyka medalowa"
            2 -> "Życiówki"
            else -> throw Exception("Unsupported fragment")
        }
    }
}
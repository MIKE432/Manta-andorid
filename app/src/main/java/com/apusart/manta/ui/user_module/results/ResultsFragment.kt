package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.apusart.manta.R
import kotlinx.android.synthetic.main.results_fragment.*
import java.lang.Exception

class ResultsFragment: Fragment(R.layout.results_fragment) {
    private lateinit var specificSectionAdapter: UserResultsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specificSectionAdapter =
            UserResultsAdapter(childFragmentManager)

        results_fragment_view_pager.apply {
            adapter = specificSectionAdapter
        }
    }
}

class UserResultsAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val COUNT = 2

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> PersonalBestFragment()
            1 -> MostValuableResultsFragment()
            else -> throw Exception("Unsupported fragment")
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Życiówki"
            1 -> "Najwartościowsze wyniki"
            else -> throw Exception("Unsupported fragment")
        }
    }
}
package com.apusart.manta.ui.user_module.profile

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apusart.manta.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.profile_fragment.*
import java.lang.Exception

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private lateinit var specificSectionAdapter: SpecificSectionAdapter
    private val titles =  bundleOf("0" to "PB",
        "1" to "MVR")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        specificSectionAdapter = SpecificSectionAdapter(this)

        profile_specific_section.apply {
            adapter = specificSectionAdapter
        }

        TabLayoutMediator(specific_section_tab_layout, profile_specific_section) { tab, p ->
            tab.text = titles.getString("${p}")

        }.attach()
    }
}

class SpecificSectionAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    private val COUNT = 2


    override fun getItemCount(): Int {
        return COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PersonalBestFragment()
            1 -> MostValuableResultsFragment()
            else -> throw Exception("Unsupported fragment")
        }
    }
}
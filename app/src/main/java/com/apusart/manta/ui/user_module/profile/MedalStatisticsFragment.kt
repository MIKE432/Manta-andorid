package com.apusart.manta.ui.user_module.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.MedalStatsViewModel
import kotlinx.android.synthetic.main.all_competition_stats_item.*
import kotlinx.android.synthetic.main.medal_statistics_fragment.*
import kotlinx.android.synthetic.main.medal_statistics_fragment.profile_fragment_athlete_medals_statistics
import kotlinx.android.synthetic.main.medal_statistics_fragment.profile_fragment_athlete_medals_statistics_scroll_view
import kotlinx.android.synthetic.main.all_competition_stats_item.view.*
import kotlinx.android.synthetic.main.all_competition_stats_item.view.profile_fragment_medal_stats
import kotlinx.android.synthetic.main.medals_statistics_item.view.*

class MedalStatisticsFragment: Fragment(R.layout.medal_statistics_fragment) {
    private val medalStatsViewModel: MedalStatsViewModel by viewModels()
    private var mUser: Athlete? = Prefs.getUser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_fragment_athlete_medals_statistics.removeAllViews()

        medalStatsViewModel.mAllMedalStats.observe(viewLifecycleOwner, Observer { allMedalStats ->

            allMedalStats.forEach { specificMedalStat ->
                val medalStatView = LayoutInflater.from(profile_fragment_athlete_medals_statistics.context)
                    .inflate(R.layout.all_competition_stats_item, profile_fragment_athlete_medals_statistics, false)

                medalStatView.profile_fragment_medal_stats_grade_title.text = specificMedalStat?.grade
                val medalsView = LayoutInflater.from(medalStatView.context)
                    .inflate(R.layout.medals_statistics_item, profile_fragment_medal_stats, false)


                medalStatView.profile_fragment_medal_stats.addView(medalsView)

                medalsView.medal_stats_item_gold_medal_count.text = "${specificMedalStat?.gold}"
                medalsView.medal_stats_item_silver_medal_count.text = "${specificMedalStat?.silver}"
                medalsView.medal_stats_item_bronze_medal_count.text = "${specificMedalStat?.bronze}"

                if(specificMedalStat?.gold == 0) {
                    medalsView.medal_stats_item_gold_medals_stats.visibility = View.INVISIBLE
                }

                if(specificMedalStat?.silver == 0) {
                    medalsView.medal_stats_item_silver_medals_stats.visibility = View.INVISIBLE
                }

                if(specificMedalStat?.bronze == 0) {
                    medalsView.medal_stats_item_bronze_medals_stats.visibility = View.INVISIBLE
                }

                profile_fragment_athlete_medals_statistics.addView(medalStatView)
            }
            var areAllNull = 0

            allMedalStats.forEach { if(it == null) areAllNull += 1 }

            profile_fragment_athlete_medals_statistics_no_medals.isVisible = areAllNull == allMedalStats.size
            medal_statistics_fragment_container.isVisible = areAllNull != allMedalStats.size
        })

        medalStatsViewModel.isAllMedalStatsRequestInProgress.observe(viewLifecycleOwner, Observer {
            profile_fragment_athlete_medals_statistics_spinner.isVisible = it
            profile_fragment_athlete_medals_statistics_scroll_view.isVisible = !it
        })

        medal_statistics_fragment_refresher.setOnRefreshListener {
            profile_fragment_athlete_medals_statistics.removeAllViews()
            medalStatsViewModel.getAllMedalStats(mUser!!.athlete_id)
            medal_statistics_fragment_refresher.isRefreshing = false
        }

        medalStatsViewModel.getAllMedalStats(mUser!!.athlete_id)
    }
}
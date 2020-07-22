package com.apusart.manta.ui.user_module.meets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.user_module.results.ResultsViewModel
import kotlinx.android.synthetic.main.last_meet_fragment.*
import kotlinx.android.synthetic.main.medals_statistics_item.view.*


class LastMeetFragment: Fragment(R.layout.last_meet_fragment) {
    private val resultsViewModel: LastMeetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultsViewModel.lastMeetResult.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                last_meet_fragment_header.text = it[0].mt_name
                last_meet_fragment_course.text = Const.courseSize.getString(it[0].mt_course_abbr)
                last_meet_fragment_date.text = it[0].mt_from
            }
        })
        val medalStat = LayoutInflater.from(last_meet_fragment_container.context)
            .inflate(R.layout.medals_statistics_item, last_meet_fragment_container, false)
        last_meet_fragment_container.addView(medalStat)

        resultsViewModel.lastMeetMedalStat.observe(viewLifecycleOwner, Observer {


            medalStat.medal_stats_item_gold_medal_count.text = it.gold.toString()
            medalStat.medal_stats_item_silver_medal_count.text = it.silver.toString()
            medalStat.medal_stats_item_bronze_medal_count.text = it.bronze.toString()

        })

        last_meet_fragment_refresher.setOnRefreshListener {
            resultsViewModel.getResultsFromLastMeetByAthleteId(Prefs.getUser()!!.athlete_id)
            last_meet_fragment_refresher.isRefreshing = false
        }

        resultsViewModel.getResultsFromLastMeetByAthleteId(Prefs.getUser()!!.athlete_id)
    }
}
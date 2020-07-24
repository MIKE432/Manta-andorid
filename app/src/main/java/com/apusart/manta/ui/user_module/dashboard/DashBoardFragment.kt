package com.apusart.manta.ui.user_module.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.MedalStatsViewModel
import com.apusart.manta.ui.tools.Tools
import com.apusart.manta.ui.user_module.meets.MeetsPager
import com.apusart.manta.ui.user_module.meets.MeetsViewModel
import com.apusart.manta.ui.user_module.results.MostValuableResultsViewModel
import com.apusart.manta.ui.user_module.results.ResultsFragment
import com.apusart.manta.ui.user_module.results.ResultsViewModel
import kotlinx.android.synthetic.main.medals_statistics_item.view.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.last_results_for_dashboard.view.*
import kotlinx.android.synthetic.main.medal_statistics_for_dashboard.*
import kotlinx.android.synthetic.main.medal_statistics_for_dashboard.view.*
import kotlinx.android.synthetic.main.meet_information_for_dashboard.view.*
import kotlinx.android.synthetic.main.most_valuable_results_for_dashboard.view.*

class DashBoardFragment: Fragment(R.layout.dashboard_fragment) {
    private var mUser: Athlete? = Prefs.getUser()
    private val medalStatsViewModel: MedalStatsViewModel by viewModels()
    private val mostValuableResultsViewModel: MostValuableResultsViewModel by viewModels()
    private val resultsViewModel: ResultsViewModel by viewModels()
    private val meetsViewModel: MeetsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        -----------------Meets--------------------

        val meetsInfo = LayoutInflater.from(this.context)
            .inflate(R.layout.meet_information_for_dashboard, dashboard_information, false)

        dashboard_information.addView(meetsInfo)

        meetsViewModel.incomingMeets.observe(viewLifecycleOwner, Observer {

            if(it.isNotEmpty()) {
                meetsInfo.meet_information_for_dashboard_incoming_meet_name.text = it[0].mt_name
            }

            meetsInfo.meet_information_for_dashboard_no_incoming_meet.isVisible = it.isEmpty()
            meetsInfo.meet_information_for_dashboard_incoming_meet_container.isVisible = it.isNotEmpty()
        })


//        -----------------Medal statsistics--------------------

        val medalStats = LayoutInflater.from(this.context)
            .inflate(R.layout.medal_statistics_for_dashboard, dashboard_information, false)
        val medalStatsItem = LayoutInflater.from(this.context)
            .inflate(R.layout.medals_statistics_item, medalStats.medals_statistics_for_dashboard_container, false)
        dashboard_information.addView(medalStats)
        medalStats.medals_statistics_for_dashboard_container.addView(medalStatsItem)

        medalStatsViewModel.mGeneralMedalStats.observe(viewLifecycleOwner, Observer {
            medalStats.medal_stats_item_gold_medal_count.text = "${it.gold}"
            medalStats.medal_stats_item_silver_medal_count.text = "${it.silver}"
            medalStats.medal_stats_item_bronze_medal_count.text = "${it.bronze}"

            medalStats.isVisible =
                !(it.gold == 0 && it.silver == 0 && it.bronze == 0)

            if(it.gold == 0)
                medalStats.medal_stats_item_gold_medals_stats.visibility = View.INVISIBLE
            else
                medalStats.medal_stats_item_gold_medals_stats.visibility = View.VISIBLE

            if(it.silver == 0)
                medalStats.medal_stats_item_silver_medals_stats.visibility = View.INVISIBLE
            else
                medalStats.medal_stats_item_silver_medals_stats.visibility = View.VISIBLE

            if(it.bronze == 0)
                medalStats.medal_stats_item_bronze_medals_stats.visibility = View.INVISIBLE
            else
                medalStats.medal_stats_item_bronze_medals_stats.visibility = View.VISIBLE
        })

        medalStats.medals_statistics_for_dashboard_container.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToRecordsFragment(1))
        }

//        -----------------MVR statsistics--------------------
        val mvrs = LayoutInflater.from(this.context)
            .inflate(R.layout.most_valuable_results_for_dashboard, dashboard_information, false)
        dashboard_information.addView(mvrs)

        mostValuableResultsViewModel.isInProgress.observe(viewLifecycleOwner, Observer {
            val best50 = mostValuableResultsViewModel.mostValuableResults.value?.firstOrNull { mvr -> mvr.res_course_abbr == "LCM" }
            val best25 = mostValuableResultsViewModel.mostValuableResults.value?.firstOrNull { mvr -> mvr.res_course_abbr == "SCM" }
            mvrs.mvr_for_dashboard_container.isVisible = (!it and (best25 != null || best50 != null))
            mvrs.mvr_for_dashboard_spinner.isVisible = it
        })

        mostValuableResultsViewModel.mostValuableResults.observe(viewLifecycleOwner, Observer {
            val best50 = it.firstOrNull { mvr -> mvr.res_course_abbr == "LCM" }
            val best25 = it.firstOrNull { mvr -> mvr.res_course_abbr == "SCM" }

            //for 25
            mvrs.mvr_for_dashboard_25_distance.text = best25?.sev_distance.toString()
            mvrs.mvr_for_dashboard_25_style.text = best25?.sst_name_pl.toString()
            mvrs.mvr_for_dashboard_25_total_time.text = Tools.convertResult(
                best25?.res_total_time?.toFloat() ?: 0f)
            mvrs.mvr_for_dashboard_25_points.text = best25?.res_points.toString()

            //for 50
            mvrs.mvr_for_dashboard_50_distance.text = best50?.sev_distance.toString()
            mvrs.mvr_for_dashboard_50_style.text = best50?.sst_name_pl.toString()
            mvrs.mvr_for_dashboard_50_total_time.text = Tools.convertResult(
                best50?.res_total_time?.toFloat() ?: 0f)
            mvrs.mvr_for_dashboard_50_points.text = best50?.res_points.toString()

            mvrs.isVisible = (best25 != null && best50 != null)
            mvrs.mvr_for_dashboard_50_container.isVisible = best50 != null
            mvrs.mvr_for_dashboard_25_container.isVisible = best25 != null
        })

        mvrs.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToRecordsFragment(0))
        }

//        -----------------Last results statsistics--------------------
        val lastResults = LayoutInflater.from(this.context)
            .inflate(R.layout.last_results_for_dashboard, dashboard_information, false)
        dashboard_information.addView(lastResults)

        lastResults.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToMeetsFragment(1))
        }

        resultsViewModel.results.observe(viewLifecycleOwner, Observer {
            when(it.size > 0) {
                false -> { lastResults.isVisible = false }
                true -> {
                    lastResults.last_results_2_container.isVisible = false
                    lastResults.last_results_1_header.text = getString(R.string.meet_header, it[0].mt_name, it[0].mt_from)
                    lastResults.last_results_1_distance.text = getString(R.string.distance, it[0].sev_distance.toString())
                    lastResults.last_results_1_style.text = it[0].sst_name_pl
                    lastResults.last_results_1_time.text = Tools.convertResult(it[0].res_total_time?.toFloat() ?: 0f)
                    lastResults.last_results_1_points.text = it[0].res_points.toString()

                    if(it.size > 2) {
                        lastResults.last_results_2_container.isVisible = true

                        lastResults.last_results_2_header.text = getString(R.string.meet_header, it[1].mt_name, it[1].mt_from)
                        lastResults.last_results_2_distance.text = getString(R.string.distance, it[1].sev_distance.toString())
                        lastResults.last_results_2_style.text = it[1].sst_name_pl
                        lastResults.last_results_2_time.text = Tools.convertResult(it[1].res_total_time?.toFloat() ?: 0f)
                        lastResults.last_results_2_points.text = it[1].res_points.toString()
                    }
                }
            }
        })



        resultsViewModel.inProgress.observe(viewLifecycleOwner, Observer {
            lastResults.isVisible = resultsViewModel.results.value?.isNotEmpty()?.equals(!it) ?: false
            lastResults.last_results_spinner.isVisible = it

        })

        dashboard_refresher.setOnRefreshListener {
            resultsViewModel.getResultsByAthleteId(mUser!!.athlete_id)
            mostValuableResultsViewModel.getMostValuableResultsByAthleteId(mUser!!.athlete_id)
            medalStatsViewModel.getGeneralMedalStatsByAthleteId(mUser!!.athlete_id)
            dashboard_refresher.isRefreshing = false
        }

        meetsViewModel.getLastMeetsByAthleteId(mUser!!.athlete_id)
        meetsViewModel.getIncomingMeetsByAthleteId(mUser!!.athlete_id)
        resultsViewModel.getResultsByAthleteId(mUser!!.athlete_id)
        mostValuableResultsViewModel.getMostValuableResultsByAthleteId(mUser!!.athlete_id)
        medalStatsViewModel.getGeneralMedalStatsByAthleteId(mUser!!.athlete_id)
    }
}
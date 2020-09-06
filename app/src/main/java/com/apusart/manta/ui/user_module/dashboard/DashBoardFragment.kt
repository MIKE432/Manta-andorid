package com.apusart.manta.ui.user_module.dashboard

import android.content.Intent
import android.net.Uri
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
import com.apusart.manta.navigation.ResultArgument
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Tools
import com.apusart.manta.ui.user_module.meets.MeetsPagerDirections
import kotlinx.android.synthetic.main.medals_statistics_item.view.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.achievements_for_dashboard.view.*
import kotlinx.android.synthetic.main.last_meet_for_dashboard.view.*
import kotlinx.android.synthetic.main.incoming_meet_for_dashboard.view.*
import kotlinx.android.synthetic.main.meet_fragment.*

class DashBoardFragment: Fragment(R.layout.dashboard_fragment) {
    private var mUser: Athlete? = Prefs.getUser()
    private val mDashBoardViewModel: DashboardViewModel by viewModels()
    private var mMeet_id: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        -----------------Meets--------------------

        val meetsInfo = LayoutInflater.from(this.context)
            .inflate(R.layout.incoming_meet_for_dashboard, dashboard_information, false)

        val achievements = LayoutInflater.from(this.context)
            .inflate(R.layout.achievements_for_dashboard, dashboard_information, false)

        val medalStatsItem = LayoutInflater.from(this.context)
            .inflate(R.layout.medals_statistics_item, achievements.medals_statistics_for_dashboard_container, false)
        achievements.medals_statistics_for_dashboard_container.addView(medalStatsItem)

        val lastResults = LayoutInflater.from(this.context)
            .inflate(R.layout.last_results_for_dashboard, dashboard_information, false)

        val lastMeet = LayoutInflater.from(this.context)
            .inflate(R.layout.last_meet_for_dashboard, dashboard_information, false)

        dashboard_information.addView(meetsInfo)
        dashboard_information.addView(lastMeet)
        dashboard_information.addView(achievements)


        lastMeet.last_meet_for_dashboard_multiple_button.setText(0, "WWW")
        lastMeet.last_meet_for_dashboard_multiple_button.setButtonIcon(0, R.drawable.www_icon)
        lastMeet.last_meet_for_dashboard_multiple_button.setText(1, "Lista startowa")
        lastMeet.last_meet_for_dashboard_multiple_button.setButtonIcon(1, R.drawable.articles_icon)
//        dashboard_information.addView(mvrs)

        mDashBoardViewModel.incomingMeets.observe(viewLifecycleOwner, Observer {

            if(it.isNotEmpty()) {
//                meetsInfo.meet_information_for_dashboard_incoming_meet_name.text = it[0].mt_name
            }

            meetsInfo.meet_information_for_dashboard_no_incoming_meet.isVisible = it.isEmpty()
            meetsInfo.meet_information_for_dashboard_incoming_meet_container.isVisible = it.isNotEmpty()
        })


        mDashBoardViewModel.areTherePhotos.observe(viewLifecycleOwner, Observer { areThereAnyPhotos ->
            if(areThereAnyPhotos) {
                lastMeet.last_meet_for_dashboard_multiple_button.setButtonOnClickListener(3) {
                    if(mMeet_id != -1) {
                        Prefs.setPreviousMeetPhoto(0)
                        findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToGalleryFragment(mMeet_id))
                    }

                }
            } else {
                lastMeet.last_meet_for_dashboard_multiple_button.setToInactive(3, R.color.pale_grey_three)
            }
        })


        mDashBoardViewModel.lastMeet.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                mMeet_id = it.meet_id
                lastMeet.isVisible = true
                lastMeet.last_meet_for_dashboard_meet_name.text = it.mt_name
                lastMeet.last_meet_for_dashboard_meet_city.text = it.mt_city
                lastMeet.last_meet_for_dashboard_course.text = Const.courseSize.getString(it.mt_course_abbr)
                lastMeet.last_meet_for_dashboard_date.text = resources.getString(R.string.meeting_date, it.mt_from, it.mt_to)
                lastMeet.last_meet_for_dashboard_multiple_button.addButton("Wyniki")
                lastMeet.last_meet_for_dashboard_multiple_button.setButtonIcon(2, R.drawable.stopwatch_icon)
                lastMeet.last_meet_for_dashboard_multiple_button.addButton("Galeria")
                lastMeet.last_meet_for_dashboard_multiple_button.setButtonIcon(3, R.drawable.gallery_icon64)

                if(it.mt_main_page != "") {

                    lastMeet.last_meet_for_dashboard_multiple_button.setButtonOnClickListener(0) { _ ->

                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_main_page))
                        startActivity(intent)
                    }
                } else {
                    lastMeet.last_meet_for_dashboard_multiple_button.setToInactive(0, R.color.pale_grey_three)
                }

                if(it.mt_start_list_page != "") {

                    lastMeet.last_meet_for_dashboard_multiple_button.setButtonOnClickListener(1) { _ ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_start_list_page))
                        startActivity(intent)
                    }
                } else {
                    lastMeet.last_meet_for_dashboard_multiple_button.setToInactive(1, R.color.pale_grey_three)
                }

                if(it.mt_results_page != "") {

                    lastMeet.last_meet_for_dashboard_multiple_button.setButtonOnClickListener(2) { _ ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_results_page))
                        startActivity(intent)
                    }
                } else {
                    lastMeet.last_meet_for_dashboard_multiple_button.setToInactive(2, R.color.pale_grey_three)
                }

                lastMeet.last_meet_for_dashboard_header_main_container.setOnClickListener { _ ->
                    Prefs.setPreviousMeetId(it.meet_id)
                    findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToMeetFragment(it.meet_id))
                }

            } else {
                lastMeet.isVisible = false
            }
        })

//        -----------------Medal statsistics--------------------


        mDashBoardViewModel.mGeneralMedalStats.observe(viewLifecycleOwner, Observer {
            achievements.medal_stats_item_gold_medal_count.text = "${it.gold}"
            achievements.medal_stats_item_silver_medal_count.text = "${it.silver}"
            achievements.medal_stats_item_bronze_medal_count.text = "${it.bronze}"

            achievements.isVisible =
                !(it.gold == 0 && it.silver == 0 && it.bronze == 0)

            if(it.gold == 0)
                achievements.medal_stats_item_gold_medals_stats.visibility = View.INVISIBLE
            else
                achievements.medal_stats_item_gold_medals_stats.visibility = View.VISIBLE

            if(it.silver == 0)
                achievements.medal_stats_item_silver_medals_stats.visibility = View.INVISIBLE
            else
                achievements.medal_stats_item_silver_medals_stats.visibility = View.VISIBLE

            if(it.bronze == 0)
                achievements.medal_stats_item_bronze_medals_stats.visibility = View.INVISIBLE
            else
                achievements.medal_stats_item_bronze_medals_stats.visibility = View.VISIBLE
        })

        achievements.medals_statistics_for_dashboard_container.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToRecordsFragment(0))
        }

//        -----------------MVR statsistics--------------------

        mDashBoardViewModel.isInProgress.observe(viewLifecycleOwner, Observer {
//            val best50 = mostValuableResultsViewModel.mostValuableResults.value?.firstOrNull { mvr -> mvr.res_course_abbr == "LCM" }
//            val best25 = mostValuableResultsViewModel.mostValuableResults.value?.firstOrNull { mvr -> mvr.res_course_abbr == "SCM" }
//            mvrs.mvr_for_dashboard_container.isVisible = (!it)
//            mvrs.mvr_for_dashboard_spinner.isVisible = it

            dashboard_spinner.isVisible = it
            profile_fragment_athlete_medals_statistics_scroll_view.isVisible = !it
        })

        mDashBoardViewModel.mostValuableResults.observe(viewLifecycleOwner, Observer {
            val best50 = it.firstOrNull { mvr -> mvr.res_course_abbr == "LCM" }
            val best25 = it.firstOrNull { mvr -> mvr.res_course_abbr == "SCM" }
//
//            //for 25
//            mvrs.mvr_for_dashboard_25_distance.text = best25?.sev_distance.toString()
//            mvrs.mvr_for_dashboard_25_style.text = best25?.sst_name_pl.toString()
//            mvrs.mvr_for_dashboard_25_total_time.text = Tools.convertResult(
//                best25?.res_total_time?.toFloat() ?: 0f)
//            mvrs.mvr_for_dashboard_25_points.text = best25?.res_points.toString()
//
//            //for 50
//            mvrs.mvr_for_dashboard_50_distance.text = best50?.sev_distance.toString()
//            mvrs.mvr_for_dashboard_50_style.text = best50?.sst_name_pl.toString()
//            mvrs.mvr_for_dashboard_50_total_time.text = Tools.convertResult(
//                best50?.res_total_time?.toFloat() ?: 0f)
//            mvrs.mvr_for_dashboard_50_points.text = best50?.res_points.toString()
//
            achievements.isVisible = (best25 != null && best50 != null)
            achievements.achievements_mvr_50_container.isVisible = best50 != null
            achievements.achievements_mvr_25_container.isVisible = best25 != null

            achievements.achievements_mvr_25_total_time.text = Tools.convertResult(best25?.res_total_time?.toFloat())
            achievements.achievements_mvr_25_concurence.text = resources.getString(R.string.concurence_no_course, best25?.sev_distance.toString(), best25?.sst_name_pl.toString())
            achievements.achievements_mvr_25_points.text = best25?.res_points.toString()

            achievements.achievements_mvr_50_total_time.text = Tools.convertResult(best50?.res_total_time?.toFloat())
            achievements.achievements_mvr_50_concurence.text = resources.getString(R.string.concurence_no_course, best50?.sev_distance.toString(), best50?.sst_name_pl.toString())
            achievements.achievements_mvr_50_points.text = best50?.res_points.toString()

            achievements.achievements_mvr_50_container.setOnClickListener {
                findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToResultDetails(
                    ResultArgument(best50!!.sev_distance, best50.sst_name_pl, best50.res_course_abbr)
                ))
            }

            achievements.achievements_mvr_25_container.setOnClickListener {
                findNavController().navigate(DashBoardFragmentDirections.actionDashboardFragmentToResultDetails(
                    ResultArgument(best25!!.sev_distance, best25.sst_name_pl, best25.res_course_abbr)
                ))
            }

        })



//        -----------------Last results statsistics--------------------



//        resultsViewModel.inProgress.observe(viewLifecycleOwner, Observer {
//            lastResults.isVisible = resultsViewModel.results.value?.isNotEmpty()?.equals(!it) ?: false
//            lastResults.last_results_spinner.isVisible = it
//
//        })

        dashboard_refresher.setOnRefreshListener {
//            resultsViewModel.getResultsByAthleteId(mUser!!.athlete_id)
//            mostValuableResultsViewModel.getMostValuableResultsByAthleteId(mUser!!.athlete_id)
//            medalStatsViewModel.getGeneralMedalStatsByAthleteId(mUser!!.athlete_id)
            mDashBoardViewModel.getInfoForDashboardByAthleteId(mUser!!.athlete_id)
            dashboard_refresher.isRefreshing = false
        }

//        meetsViewModel.getLastMeetsByAthleteId(mUser!!.athlete_id)
//        meetsViewModel.getIncomingMeetsByAthleteId(mUser!!.athlete_id)
//        resultsViewModel.getResultsByAthleteId(mUser!!.athlete_id)
//        mostValuableResultsViewModel.getMostValuableResultsByAthleteId(mUser!!.athlete_id)
//        medalStatsViewModel.getGeneralMedalStatsByAthleteId(mUser!!.athlete_id)
//
        mDashBoardViewModel.getInfoForDashboardByAthleteId(mUser!!.athlete_id)
    }
}
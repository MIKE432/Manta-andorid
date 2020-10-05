package com.apusart.manta.ui.user_module.profile

import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.MedalStatsViewModel
import com.apusart.manta.ui.Stats
import kotlinx.android.synthetic.main.all_competition_stats_item.view.*

import kotlinx.android.synthetic.main.medal_statistics_fragment.*
import kotlinx.android.synthetic.main.medals_statistics_item.view.*

class MedalStatisticsFragment: Fragment(R.layout.medal_statistics_fragment) {
    private val medalStatsViewModel: MedalStatsViewModel by viewModels()
    private var mUser: Athlete? = Prefs.getUser()
    private lateinit var mStatsListAdapter: MedalStatsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mStatsListAdapter = MedalStatsListAdapter()

        medalStatsViewModel.mAllMedalStats.observe(viewLifecycleOwner, Observer { allMedalStats ->

            medal_statistics_list.apply {
                adapter = mStatsListAdapter
            }

            mStatsListAdapter.submitList(allMedalStats)

            var areAllNull = 0

            allMedalStats.forEach { if(it == null) areAllNull += 1 }

            profile_fragment_athlete_medals_statistics_no_medals.isVisible = areAllNull == allMedalStats.size
            profile_fragment_athlete_medals_statistics_scroll_view.isVisible = areAllNull != allMedalStats.size
        })

        medalStatsViewModel.isAllMedalStatsRequestInProgress.observe(viewLifecycleOwner, Observer {
            profile_fragment_athlete_medals_statistics_spinner.isVisible = it
            profile_fragment_athlete_medals_statistics_scroll_view.isVisible = !it
        })


        medalStatsViewModel.getAllMedalStats(mUser!!.athlete_id)
    }
}

class MedalStatsListAdapter: ListAdapter<Stats, StatsViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<Stats>() {
        override fun areItemsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Stats, newItem: Stats): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_competition_stats_item, parent, false)

        return StatsViewHolder(container)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StatsViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: Stats) {
        itemView.apply {
            profile_fragment_medal_stats_grade_title.text = item.grade


            profile_fragment_medal_stats_medals.medal_stats_item_gold_medal_count.text = "${item.gold}"
            profile_fragment_medal_stats_medals.medal_stats_item_silver_medal_count.text = "${item.silver}"
            profile_fragment_medal_stats_medals.medal_stats_item_bronze_medal_count.text = "${item.bronze}"

            if(item.gold == 0) {
                profile_fragment_medal_stats_medals.medal_stats_item_gold_medals_stats.visibility = View.INVISIBLE
            }

            if(item.silver == 0) {
                profile_fragment_medal_stats_medals.medal_stats_item_silver_medals_stats.visibility = View.INVISIBLE
            }

            if(item.bronze == 0) {
                profile_fragment_medal_stats_medals.medal_stats_item_bronze_stats.visibility = View.INVISIBLE
            }
        }
    }
}
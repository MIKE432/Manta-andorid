package com.apusart.manta.ui.user_module.meets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.MultipleButton
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.tools.Tools
import com.apusart.manta.ui.user_module.results.ResultsViewModel
import kotlinx.android.synthetic.main.last_meet_fragment.*
import kotlinx.android.synthetic.main.medals_statistics_item.view.*
import kotlinx.android.synthetic.main.result_comparison_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat


class LastMeetFragment: Fragment(R.layout.last_meet_fragment) {
    private val resultsViewModel: LastMeetViewModel by viewModels()
    private lateinit var comparedResultAdapter: ComparedResultAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comparedResultAdapter = ComparedResultAdapter()

        last_meet_fragment_results.apply {
            adapter = comparedResultAdapter
        }
        multiple_button.addButton("3") {
            Toast.makeText(context, "3", Toast.LENGTH_SHORT).show()
        }
        multiple_button.addButton("4") {
            Toast.makeText(context, "4", Toast.LENGTH_SHORT).show()
        }
        multiple_button.addButton("5") {
            Toast.makeText(context, "5", Toast.LENGTH_SHORT).show()
        }
        resultsViewModel.lastMeetResultCompared.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                last_meet_fragment_header.text = it[0].result.mt_name
                last_meet_fragment_course.text = Const.courseSize.getString(it[0].result.mt_course_abbr)
                last_meet_fragment_date.text = it[0].result.mt_from
            }
            comparedResultAdapter.submitList(it)
        })

        last_meet_fragment_refresher.setOnRefreshListener {
            resultsViewModel.getResultsFromLastMeetByAthleteId(Prefs.getUser()!!.athlete_id)
            last_meet_fragment_refresher.isRefreshing = false
        }

        resultsViewModel.getResultsFromLastMeetByAthleteId(Prefs.getUser()!!.athlete_id)
    }
}

class ComparedResultAdapter: ListAdapter<ComparedResult, ComparedResultViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<ComparedResult>() {
        override fun areItemsTheSame(oldItem: ComparedResult, newItem: ComparedResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ComparedResult, newItem: ComparedResult): Boolean {
            return oldItem.result.result_id == newItem.result.result_id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparedResultViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_comparison_item, parent, false)

        return ComparedResultViewHolder(container)
    }

    override fun onBindViewHolder(holder: ComparedResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ComparedResultViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: ComparedResult) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_DOWN
        val src = when(item.result.res_place) {
            1 -> R.drawable.medal_gold_icon32
            2 -> R.drawable.medal_silver_icon32
            3 -> R.drawable.medal_bronze_icon32
            else -> 0
        }
        itemView.place.isVisible = src == 0
        itemView.medalIcon.isVisible = src != 0

        itemView.apply {
            result_comparison_item_header.text = resources.getString(R.string.concurence_no_course, item.result.sev_distance.toString(), item.result.sst_name_pl)
            result_comparison_item_progress_value.text = df.format(item.progress* 100).toString() + "%"
            medalIcon.setBackgroundResource(src)
            place.text = "${item.result.res_place} msc"
            result_comparison_item_entry_time.text = Tools.convertResult(item.result.res_entry_time?.toFloat())
            result_comparison_item_best_prev_time.text = Tools.convertResult(item.result.res_prev_best_time?.toFloat())
            result_comparison_item_actual_time.text = Tools.convertResult(item.result.res_total_time?.toFloat())
            result_comparison_item_actual_points.text = item.result.res_points.toString()
            result_comparison_item_additional_info_split_times.text = item.result.res_split_times
        }
    }
}
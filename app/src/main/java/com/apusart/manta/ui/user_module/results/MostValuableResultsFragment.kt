package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Tools
import com.apusart.manta.api.models.MostValuableResult
import com.apusart.manta.navigation.ResultArgument
import com.apusart.manta.ui.tools.Const
import kotlinx.android.synthetic.main.most_valuable_result_item.view.*
import kotlinx.android.synthetic.main.most_valuable_results_fragment.*

class MostValuableResultsFragment: Fragment(R.layout.most_valuable_results_fragment) {
    private lateinit var mostValuableResultsAdapter: MostValuableResultsAdapter
    private val mostValuableResultsViewModel: MostValuableResultsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mostValuableResultsAdapter = MostValuableResultsAdapter(findNavController())

        most_valuable_results_list.apply {
            adapter = mostValuableResultsAdapter
        }
        mostValuableResultsViewModel.mostValuableResults.observe(viewLifecycleOwner, Observer { mvrList ->
            mostValuableResultsAdapter.submitList(mvrList)
            most_valuable_results_list_no_mvr.isVisible = mvrList.isEmpty()
            most_valuable_results_list.isVisible = mvrList.isNotEmpty()
        })

        mostValuableResultsViewModel.isInProgress.observe(viewLifecycleOwner, Observer {
             most_valuable_results_list.isVisible = !it
            most_valuable_results_list_spinner.isVisible = it
        })

        mvr_refresher.setOnRefreshListener {
            mostValuableResultsViewModel.getMostValuableResultsByAthleteId(Prefs.getUser()!!.athlete_id)
            mvr_refresher.isRefreshing = false
        }
        mostValuableResultsViewModel.getMostValuableResultsByAthleteId(Prefs.getUser()!!.athlete_id)

    }
}

class MostValuableResultsAdapter(val navController: NavController): ListAdapter<MostValuableResult, MostValuableResultViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<MostValuableResult>() {
        override fun areItemsTheSame(
            oldItem: MostValuableResult,
            newItem: MostValuableResult
        ): Boolean {
            return oldItem.result_id == newItem.result_id
        }

        override fun areContentsTheSame(
            oldItem: MostValuableResult,
            newItem: MostValuableResult
        ): Boolean {
            return oldItem.result_id == newItem.result_id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MostValuableResultViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.most_valuable_result_item, parent, false)

        return MostValuableResultViewHolder(viewContainer, navController)
    }

    override fun onBindViewHolder(holder: MostValuableResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MostValuableResultViewHolder(viewContainer: View, val navController: NavController): RecyclerView.ViewHolder(viewContainer) {

    fun bind(mvr: MostValuableResult) {
        itemView.apply {
            most_valuable_result_item_points.text = "${mvr.res_points}"
            most_valuable_result_item_time.text = Tools.convertResult(mvr.res_total_time.toFloat())
            most_valuable_result_item_distance.text = "${mvr.sev_distance}"
            most_valuable_result_item_style.text = mvr.sst_name_pl
        }

        itemView.setOnClickListener {
            navController.navigate(ResultsFragmentDirections.actionRecordsFragmentToResultDetails(
                ResultArgument(mvr.sev_distance, Const.styles.getString(mvr.sst_name_pl ?: "FR")!!, mvr.res_course_abbr)
            ))
        }
    }
}
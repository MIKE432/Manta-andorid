package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.navigation.ResultArgument
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Tools
import kotlinx.android.synthetic.main.personal_best_fragment.*
import kotlinx.android.synthetic.main.personal_best_item.view.*


class PersonalBestFragment: Fragment(R.layout.personal_best_fragment) {
    private val viewModel: PersonalBestsViewModel by viewModels()
    private lateinit var personalBestAdapter: PersonalBestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalBestAdapter = PersonalBestAdapter(findNavController())

        personal_best_list.apply {
            adapter = personalBestAdapter
        }

        personal_best_list.isNestedScrollingEnabled = false

        viewModel.pb1.observe(viewLifecycleOwner, Observer { pb ->
            personalBestAdapter.submitList(pb)
        })

        personal_best_refresher.setOnRefreshListener {
            viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
            personal_best_refresher.isRefreshing = false
        }

        viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
    }
}

class PersonalBestAdapter(val navController: NavController): ListAdapter<PersonalBestByCompetition, PersonalBestViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<PersonalBestByCompetition>() {
        override fun areItemsTheSame(oldItem: PersonalBestByCompetition, newItem: PersonalBestByCompetition): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PersonalBestByCompetition, newItem: PersonalBestByCompetition): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalBestViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.personal_best_item, parent, false)

        return PersonalBestViewHolder(viewContainer as ViewGroup, navController)
    }

    override fun onBindViewHolder(holder: PersonalBestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PersonalBestViewHolder(containerView: ViewGroup, val navController: NavController): RecyclerView.ViewHolder(containerView) {
    private var infoStatus = false
    private var mHeight: Int = 0

//    private fun globalLayoutListener(view: View, it: PersonalBestViewHolder) {
//        view.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                it.mHeight = itemView.personal_best_item_additional_info.measuredHeight
//                view.personal_best_item_additional_info.layoutParams.height = 0
//                view.personal_best_item_additional_info.requestLayout()
//                view.viewTreeObserver.removeOnGlobalLayoutListener(this);
//            }
//        })
//    }

    init {
//        globalLayoutListener(itemView, this)
    }

    fun bind(pb: PersonalBestByCompetition) {

        val is25Null = pb.pb25 == null
        val is50Null = pb.pb50 == null
        itemView.apply {
            //visibility
            personal_best_item_25_container.isVisible = !is25Null
            personal_best_item_50_container.isVisible = !is50Null

            personal_best_item_header.text = pb.concurence

            //25
            if(!is25Null) {
                personal_best_item_25_total_time.text = Tools.convertResult(pb.pb25?.res_total_time?.toFloat())
                personal_best_item_25_points.text = pb.pb25?.res_points.toString()
            }

            //50
            if(!is50Null) {
                personal_best_item_50_total_time.text = Tools.convertResult(pb.pb50?.res_total_time?.toFloat())
                personal_best_item_50_points.text = pb.pb50?.res_points.toString()
            }
        }

        if(!is25Null)
            itemView.personal_best_item_25_container.setOnClickListener {
                navController.navigate(ResultsFragmentDirections.actionRecordsFragmentToResultDetails(ResultArgument(pb.pb25!!.sev_distance, pb.pb25.sst_name_pl, pb.pb25.res_course_abbr)))
            }

        if(!is50Null)
            itemView.personal_best_item_50_container.setOnClickListener {
                navController.navigate(ResultsFragmentDirections.actionRecordsFragmentToResultDetails(ResultArgument(pb.pb50!!.sev_distance, pb.pb50.sst_name_pl, pb.pb50.res_course_abbr)))
            }
    }
}
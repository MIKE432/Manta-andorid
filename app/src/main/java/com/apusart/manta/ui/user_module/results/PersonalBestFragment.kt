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

        personal_best_25_list.apply {
            adapter = personalBestAdapter
        }

        personal_best_25_list.isNestedScrollingEnabled = false

        viewModel.pb.observe(viewLifecycleOwner, Observer { pb ->
            personalBestAdapter.submitList(pb)
        })
        personal_best_refresher.setOnRefreshListener {
            viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
            personal_best_refresher.isRefreshing = false
        }
        viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
    }
}

class PersonalBestAdapter(val navController: NavController): ListAdapter<PersonalBest, PersonalBestViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<PersonalBest>() {
        override fun areItemsTheSame(oldItem: PersonalBest, newItem: PersonalBest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PersonalBest, newItem: PersonalBest): Boolean {
            return ((oldItem.res_total_time == newItem.res_total_time) and (oldItem.res_total_time == newItem.res_total_time))
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

    fun bind(pb: PersonalBest) {
        itemView.apply {
            personal_best_item_header.text = "${pb.sev_distance}m ${pb.sst_name_pl}"
            personal_best_item_course_type.text = Const.courseSize.getString(pb.res_course_abbr)
            personal_best_item_25_date.text = pb.mt_from
            personal_best_item_25_points.text = pb.res_points.toString()
            personal_best_item_25_total_time.text = Tools.convertResult(pb.res_total_time.toFloat())
        }

        itemView.setOnClickListener {
            navController.navigate(ResultsFragmentDirections.actionRecordsFragmentToResultDetails(ResultArgument(pb.sev_distance, pb.sst_name_pl, pb.res_course_abbr)))
        }

    }
}
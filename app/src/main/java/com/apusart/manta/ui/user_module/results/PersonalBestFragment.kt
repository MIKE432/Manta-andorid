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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Tools
import kotlinx.android.synthetic.main.personal_best_fragment.*
import kotlinx.android.synthetic.main.personal_best_item.view.*


class PersonalBestFragment: Fragment(R.layout.personal_best_fragment) {
    private val viewModel: PersonalBestsViewModel by viewModels()
    private lateinit var personalBestAdapter: PersonalBestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalBestAdapter = PersonalBestAdapter()

        personal_best_25_list.apply {
            adapter = personalBestAdapter
        }

        personal_best_25_list.isNestedScrollingEnabled = false

        viewModel.pb.observe(viewLifecycleOwner, Observer { pb ->
            personalBestAdapter.submitList(pb)
        })

        viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
    }
}

class PersonalBestAdapter: ListAdapter<PersonalBestByCompetition, PersonalBestViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<PersonalBestByCompetition>() {
        override fun areItemsTheSame(oldItem: PersonalBestByCompetition, newItem: PersonalBestByCompetition): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PersonalBestByCompetition, newItem: PersonalBestByCompetition): Boolean {
            return ((oldItem.pb25?.res_total_time == newItem.pb25?.res_total_time) and (oldItem.pb50?.res_total_time == newItem.pb50?.res_total_time))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalBestViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.personal_best_item, parent, false)

        return PersonalBestViewHolder(viewContainer as ViewGroup)
    }



    override fun onBindViewHolder(holder: PersonalBestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PersonalBestViewHolder(containerView: ViewGroup): RecyclerView.ViewHolder(containerView) {
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
        itemView.apply {
            personal_best_item_header.text = if(pb.pb25 == null) "${pb.pb50?.sev_distance}m ${pb.pb50?.sst_name_pl}" else "${pb.pb25?.sev_distance}m ${pb.pb25?.sst_name_pl}"
            personal_best_item_25_container.isVisible = pb.pb25 != null
            personal_best_item_50_container.isVisible = pb.pb50 != null
            personal_best_item_25_date.text = pb.pb25?.mt_from
            personal_best_item_50_date.text = pb.pb50?.mt_from
            personal_best_item_25_points.text = pb.pb25?.res_points.toString()
            personal_best_item_50_points.text = pb.pb50?.res_points.toString()
            personal_best_item_25_total_time.text = Tools.convertResult(pb.pb25?.res_total_time?.toFloat())
            personal_best_item_50_total_time.text = Tools.convertResult(pb.pb50?.res_total_time?.toFloat())
        }

        itemView.setOnClickListener {

            it.apply {
//                if(infoStatus.also { infoStatus = !infoStatus }) {
//                    Animations.slideView(personal_best_item_additional_info, mHeight, 0)
//                } else {
//                    Animations.slideView(personal_best_item_additional_info, 0, mHeight)
//                }
            }
        }

    }
}
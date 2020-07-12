package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.ui.Animations
import kotlinx.android.synthetic.main.personal_best_fragment.*
import kotlinx.android.synthetic.main.personal_best_item.view.*


class PersonalBestFragment: Fragment(R.layout.personal_best_fragment) {
    private val viewModel: PersonalBestsViewModel by viewModels()
    private val titles =  bundleOf("0" to "25m", "1" to "50m")
    private lateinit var personalBest25Adapter: PersonalBestAdapter
    private lateinit var personalBest50Adapter: PersonalBestAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personalBest25Adapter = PersonalBestAdapter()
        personalBest50Adapter = PersonalBestAdapter()

        personal_best_25_list.apply {
            adapter = personalBest25Adapter

        }
        personal_best_25_list.isNestedScrollingEnabled = false
        personal_best_50_list.isNestedScrollingEnabled = false
        personal_best_50_list.apply {
            adapter = personalBest50Adapter

        }

        viewModel.pb25.observe(viewLifecycleOwner, Observer { pb25 ->
            personalBest25Adapter.submitList(pb25)
        })


        viewModel.pb50.observe(viewLifecycleOwner, Observer { pb50 ->
            personalBest50Adapter.submitList(pb50)
        })


        viewModel.getPersonalBestsByAthleteId(Prefs.getUser()!!.athlete_id, 100)
    }
}

class PersonalBestAdapter: ListAdapter<PersonalBest, PersonalBestViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<PersonalBest>() {
        override fun areItemsTheSame(oldItem: PersonalBest, newItem: PersonalBest): Boolean {
            return ((oldItem.sev_distance == newItem.sev_distance) and (oldItem.res_total_time == newItem.res_total_time))
        }

        override fun areContentsTheSame(oldItem: PersonalBest, newItem: PersonalBest): Boolean {
            return ((oldItem.sev_distance == newItem.sev_distance) and (oldItem.res_total_time == newItem.res_total_time))
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

    private fun convertResult(sec: Float): String {
        val millis = (sec * 100).toInt()
        val minutes = millis / 100 / 60
        val seconds = millis / 100 % 60
        val m = millis % 100
        return "${
            if((minutes < 10) and (minutes != 0)) "0${minutes}:" 
            else if((minutes == 0)) "" 
            else "$minutes:"}${if(seconds < 10) "0${seconds}" else "$seconds"}.${if(m < 10) "0${m}" else "${m}"}"
    }

    init {
        itemView.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mHeight = itemView.personal_best_item_additional_info.measuredHeight
                itemView.personal_best_item_additional_info.layoutParams.height = 0
                itemView.personal_best_item_additional_info.requestLayout()
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this);
            }
        })
    }

    fun bind(pb: PersonalBest) {
        itemView.apply {
            personal_best_item_time.text = convertResult(pb.res_total_time.toFloat())
            personal_best_item_distance.text = "${pb.sev_distance}m"
            personal_best_item_style.text = pb.sst_name_pl
            personal_best_item_points.text = "${pb.res_points}"
            personal_best_item_meeting_name.text = pb.mt_name
        }

        itemView.setOnClickListener {

            it.apply {
                if(infoStatus.also { infoStatus = !infoStatus }) {
                    Animations.slideView(personal_best_item_additional_info, mHeight, 0)
                } else {
                    Animations.slideView(personal_best_item_additional_info, 0, mHeight)
                }
            }
        }

    }
}
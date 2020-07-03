package com.example.manta.ui.pick_athlete_module

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.manta.*
import com.example.manta.api.models.Athlete
import kotlinx.android.synthetic.main.pick_athlete_fragment.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.*

class PickAthleteFragment: Fragment(R.layout.pick_athlete_fragment) {
    private val viewModel: AthletesViewModel by viewModels()
    private lateinit var athletesAdapter: AthletesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        athletesAdapter = AthletesAdapter(requireActivity())
        viewModel.athletes.observe(viewLifecycleOwner, Observer { athletes ->
            athletesAdapter.submitList(athletes)
        })

        viewModel.inProgress.observe(viewLifecycleOwner, Observer { v ->
            pick_athlete_athletes_list.isVisible = !v
            pick_athlete_progressbar.isVisible = v
        })

        viewModel.getAthletes()
        pick_athlete_athletes_list.apply {
            adapter = athletesAdapter
        }
    }
}


class AthletesAdapter(private val fragmentActivity: Activity): ListAdapter<Athlete, AthleteViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<Athlete>() {
        override fun areItemsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem.athlete_id == newItem.athlete_id
        }

        override fun areContentsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem.athlete_id == newItem.athlete_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.pick_athlete_item, parent, false)

        return AthleteViewHolder(
            viewContainer, parent.context
        )
    }

    override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            Prefs.AthletePreference(holder.context)
            Prefs.storeUser(getItem(position))
            startActivity(fragmentActivity.applicationContext, Intent(fragmentActivity.applicationContext, UserActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION), null)
            fragmentActivity.finish()
        }
    }
}

class AthleteViewHolder(viewContainer: View, val context: Context): RecyclerView.ViewHolder(viewContainer) {

    fun bind(item: Athlete) {
        itemView.apply {
            pick_athlete_item_firstname.text = item.ath_firstname
            pick_athlete_item_lastname.text = item.ath_lastname
            pick_athlete_item_birth_year.text = item.ath_birth_year.toString()
            if(item.ath_birth_year == 0) {
                pick_athlete_item_birth_year.text = "- - - - "
            }

            val url = Const.baseUrl + item.ath_image_min_url

            item.ath_image_min_url?.let {
                Glide
                    .with(this)
                    .load(url)
                    .into(pick_athlete_item_image)
            }

        }
    }
}
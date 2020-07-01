package com.example.manta

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.manta.api.calls.Const
import com.example.manta.api.models.Athlete
import kotlinx.android.synthetic.main.pick_athlete_activity.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.*

class MainActivity : AppCompatActivity(R.layout.pick_athlete_activity) {
    private val viewModel: AthletesViewModel by viewModels()
    private val athletesAdapter: AthletesAdapter = AthletesAdapter()
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.athletes.observe(this, Observer { athletes ->
            athletesAdapter.submitList(athletes)
        })

        pick_athlete_input?.onFocusChangeListener = object: View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if(!hasFocus) {
                    hideKeyboard(v)
                }
            }

        }
        pick_athlete_athletes_list.apply {
            adapter = athletesAdapter
        }
        viewModel.getAthletes()
    }
}

class AthletesAdapter: ListAdapter<Athlete, AthleteViewHolder>(diffUtil) {


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

        return AthleteViewHolder(viewContainer)
    }

    override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AthleteViewHolder(viewContainer: View): RecyclerView.ViewHolder(viewContainer) {

    fun bind(item: Athlete) {
        itemView.apply {
            pick_athlete_item_firstname.text = item.ath_firstname
            pick_athlete_item_lastname.text = item.ath_lastname
            pick_athlete_item_birth_year.text = item.ath_birth_year.toString()
            if(item.ath_birth_year == 0) {
                pick_athlete_item_birth_year.text = "- - - - "
            }
            val url = Const.pageUrl + item.ath_image_min_url

            item.ath_image_min_url?.let {
                Glide
                    .with(this)
                    .load(url)
                    .into(pick_athlete_item_image)
            }

        }
    }
}
package com.apusart.manta.ui.pick_athlete_module

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.apusart.manta.*
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.OnSwipeTouchListener
import com.apusart.manta.ui.tools.Prefs
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.letter_for_scroll.view.*
import kotlinx.android.synthetic.main.pick_athlete_activity.*
import kotlinx.android.synthetic.main.pick_athlete_item.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.pick_athlete_item_image
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PickAthleteActivity: AppCompatActivity(R.layout.pick_athlete_activity) {
    private val viewModel: AthletesViewModel by viewModels()
    private lateinit var athletesAdapter: AthletesAdapter
    private var lastTouch = 0L

    fun hideKayboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        athletesAdapter = AthletesAdapter(this)

        viewModel.athletes.observe(this, Observer { athletes ->
            athletesAdapter.submitList(athletes)

            if(Prefs.getPreviousAthlete() != null && athletes.isNotEmpty()) {
                val athlete: Athlete? = athletesAdapter.currentList.firstOrNull { it.athlete_id == Prefs.getPreviousAthlete()?.athlete_id }
                val position = athletesAdapter.currentList.indexOf(athlete ?: athletesAdapter.currentList[0])

                pick_athlete_athletes_list.scrollToPosition(position)
            }

            if(athletes.isNotEmpty()) {
                pick_athlete_scroll_bar.setupWithRecyclerView(pick_athlete_athletes_list, {
                    val item = athletes.get(it)

                    FastScrollItemIndicator.Text(item.ath_lastname.substring(0, 1).toUpperCase())
                })

                pick_athlete_fastscroller_thumb.setupWithFastScroller(pick_athlete_scroll_bar)
            }

        })

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        viewModel.inProgress.observe(this, Observer { v ->
            pick_athlete_athletes_list.isVisible = !v
            pick_athlete_progressbar.isVisible = v


        })

//        viewModel.mantaAlph.observe(this, Observer { list ->
//            list.forEach { s ->
//                val textView = LayoutInflater.from(this)
//                    .inflate(R.layout.letter_for_scroll, pick_athlete_scroll_bar, false)
//                textView.letter.text = s
//                textView.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    1f
//                )
//                pick_athlete_scroll_bar.addView(textView)
//                pick_athlete_scroll_bar.setOnClickListener(null)
//                textView.setOnClickListener { view ->
//                    val athlete: Athlete? = athletesAdapter.currentList.firstOrNull { it.ath_lastname.substring(0, 1) == s }
//                    val position = athletesAdapter.currentList.indexOf(athlete ?: athletesAdapter.currentList[0])
//
//                    pick_athlete_athletes_list.scrollToPosition(position)
//                    lastTouch = System.currentTimeMillis()
//                    lifecycleScope.launch {
//                        delay(2000)
//                        if(System.currentTimeMillis() - lastTouch > 2000) {
//                            pick_athlete_scroll_bar.isVisible = false
//                        }
//                    }
//                }
//            }
//        })

        viewModel.getAthletes()
        pick_athlete_athletes_list.apply {
            adapter = athletesAdapter
        }

//        pick_athlete_scroll_bar.isVisible = false
        pick_athlete_athletes_list.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

//                when(newState) {
//                    RecyclerView.SCROLL_STATE_DRAGGING -> {
//                        pick_athlete_scroll_bar.isVisible = true
//                        lastTouch = System.currentTimeMillis()
//                    }
//                    RecyclerView.SCROLL_STATE_IDLE -> lifecycleScope.launch {
//                        delay(5000)
//                        if(System.currentTimeMillis() - lastTouch > 5000) {
//                            pick_athlete_scroll_bar.isVisible = false
//                        }
//
//                    }
//                }
                hideKayboard()
            }
        })


//        pick_athlete_athletes_list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                hideKayboard()
//            }
//        })

        pick_athlete_athletes_edit_text.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val athlete: Athlete? = athletesAdapter.currentList.firstOrNull { (it.ath_lastname.contains(s ?: "")) or (it.ath_firstname.contains(s ?: "")) }
                val position = athletesAdapter.currentList.indexOf(athlete ?: athletesAdapter.currentList[0])

                pick_athlete_athletes_list.scrollToPosition(position)
            }

        })

    }
}


class AthletesAdapter(private val activity: Activity): ListAdapter<Athlete, AthleteViewHolder>(diffUtil) {

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
            startActivity(activity, Intent(activity, UserActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK), null)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}

class AthleteViewHolder(viewContainer: View, val context: Context): RecyclerView.ViewHolder(viewContainer) {

    fun bind(item: Athlete) {
        itemView.apply {
            pick_athlete_name.text = context.getString(R.string.user_name, item.ath_lastname, item.ath_firstname)
            pick_athlete_licence_no.text = context.getString(R.string.licence_no, if(item.ath_licence_no.toString() == "") "(Brak)" else item.ath_licence_no.toString())
            val prevAthlete = Prefs.getPreviousAthlete()
            background = ColorDrawable(resources.getColor(R.color.white))
            if(prevAthlete != null && prevAthlete.athlete_id == item.athlete_id) {
                background = resources.getDrawable(R.drawable.selected_ahtlete_background)
            }

            val url = Const.baseUrl + item.ath_image_min_url

            item.ath_image_min_url?.let {
                Glide
                    .with(this)
                    .load(url)
                    .apply(Const.glideAthleteIconOptions)
                    .into(pick_athlete_item_image)
            }

        }
    }
}
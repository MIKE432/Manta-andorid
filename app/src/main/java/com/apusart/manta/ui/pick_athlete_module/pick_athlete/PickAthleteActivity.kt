package com.apusart.manta.ui.pick_athlete_module.pick_athlete

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.apusart.manta.*
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.databinding.PickAthleteActivityBinding
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.tools.Tools
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import kotlinx.android.synthetic.main.pick_athlete_activity.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.*
import kotlinx.android.synthetic.main.pick_athlete_item.view.pick_athlete_item_image

class PickAthleteActivity: AppCompatActivity() {
    private val viewModel: AthletesViewModel by viewModels()
    private lateinit var athletesAdapter: AthletesAdapter
    private val pickAthleteViewModel: PickAthleteViewModel by viewModels()
    private lateinit var binding: PickAthleteActivityBinding

    companion object {
        var mLastClick: Long = 0
    }

    fun hideKayboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val x = Prefs.getCurrentTheme()
        setTheme(if (x == 1) R.style.Manta_Theme_Dark else R.style.Manta_Theme_Light)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.pick_athlete_activity)
        binding.pickAthleteViewModel = pickAthleteViewModel
        athletesAdapter =
            AthletesAdapter(
                this
            )

        viewModel.athletes.observe(this, Observer { athletes ->
            athletesAdapter.submitList(athletes)

            if (Prefs.getPreviousAthlete() != null && athletes.isNotEmpty()) {
                val athlete: Athlete? =
                    athletesAdapter.currentList.firstOrNull { it.athlete_id == Prefs.getPreviousAthlete()?.athlete_id }
                val position =
                    athletesAdapter.currentList.indexOf(athlete ?: athletesAdapter.currentList[0])

                pick_athlete_athletes_list.scrollToPosition(position)
            }

            if (athletes.isNotEmpty()) {
                pick_athlete_scroll_bar.setupWithRecyclerView(pick_athlete_athletes_list, {
                    val item = athletes[it]

                    FastScrollItemIndicator.Text(item.ath_lastname.substring(0, 1).toUpperCase())
                })

                pick_athlete_fastscroller_thumb.setupWithFastScroller(pick_athlete_scroll_bar)
            }
        })

        pickAthleteViewModel.athleteNameText.observe(this, Observer { s ->
            val isThereAText = s.toString() != ""

            binding.pickAthleteScrollBar.isVisible = !isThereAText
            binding.pickAthleteFastscrollerThumb.isVisible = !isThereAText
            val newList = viewModel.athletes.value?.filter {
                it.ath_firstname.startsWith(s.toString(), true) or
                        it.ath_lastname.startsWith(s.toString(), true) or
                        "${it.ath_firstname} ${it.ath_lastname}".startsWith(s.toString(), true) or
                        "${it.ath_lastname} ${it.ath_firstname}".startsWith(s.toString(), true)}
            binding.pickAthleteAthletesEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_drawable,0, if (isThereAText) R.drawable.erase_icon16 else 0, 0)
            athletesAdapter.submitList(newList)
        })

//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        viewModel.inProgress.observe(this, Observer { v ->
            pick_athlete_athletes_list.isVisible = !v
            pick_athlete_progressbar.isVisible = v


        })

        viewModel.getAthletes()
        pick_athlete_athletes_list.apply {
            adapter = athletesAdapter
        }

        pick_athlete_athletes_list.isMotionEventSplittingEnabled = false

        pick_athlete_athletes_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideKayboard()
            }
        })

        pick_athlete_athletes_edit_text.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0;
            val DRAWABLE_TOP = 1;
            val DRAWABLE_RIGHT = 2;
            val DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (pick_athlete_athletes_edit_text.right - Tools.toDp(32))) {
                    pick_athlete_athletes_edit_text.text.clear()
                    v.performClick()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

//        pick_athlete_athletes_edit_text.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//                val isThereAText = s.toString() != ""
//
//                pick_athlete_scroll_bar.isVisible = !isThereAText
//                pick_athlete_fastscroller_thumb.isVisible = !isThereAText
//                val newList = viewModel.athletes.value?.filter {
//                    it.ath_firstname.startsWith(s.toString(), true) or
//                    it.ath_lastname.startsWith(s.toString(), true) or
//                    "${it.ath_firstname} ${it.ath_lastname}".startsWith(s.toString(), true) or
//                    "${it.ath_lastname} ${it.ath_firstname}".startsWith(s.toString(), true)}
//                pick_athlete_athletes_edit_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon_drawable,0, if (isThereAText) R.drawable.erase_icon16 else 0, 0)
//                athletesAdapter.submitList(newList)
//            }
//        })

    }
}


class AthletesAdapter(private val activity: Activity): ListAdapter<Athlete, AthleteViewHolder>(
    diffUtil
) {

    object diffUtil : DiffUtil.ItemCallback<Athlete>() {
        override fun areItemsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Athlete, newItem: Athlete): Boolean {
            return oldItem.equals(newItem)
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return when(getItem(position)) {
//
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.pick_athlete_item, parent, false)

        return AthleteViewHolder(
            viewContainer, parent.context
        )
    }

    override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
        val athlete = getItem(position)
        holder.bind(athlete)
        holder.itemView.setOnClickListener {

            val now = System.currentTimeMillis()

            if (now - PickAthleteActivity.mLastClick > 1000) {
                Prefs.athletePreference(holder.context)
                Prefs.storeUser(athlete)
                PickAthleteActivity.mLastClick = now

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    Pair.create(holder.itemView, "pick_athlete_to_header")
                )

                startActivity(
                    activity,
                    Intent(activity, UserActivity::class.java),
                    options.toBundle()
                )
            }
        }
    }
}

class AthleteViewHolder(viewContainer: View, val context: Context) :
    RecyclerView.ViewHolder(viewContainer) {

    fun bind(item: Athlete) {
        itemView.apply {
            pick_athlete_name.text =
                context.getString(R.string.user_name, item.ath_lastname, item.ath_firstname)
            pick_athlete_licence_no.text = context.getString(
                R.string.licence_no,
                if (item.ath_licence_no.toString() == "") "(Brak)" else item.ath_licence_no.toString()
            )
            val prevAthlete = Prefs.getPreviousAthlete()
            background = ColorDrawable(resources.getColor(R.color.white))

            if (prevAthlete != null && prevAthlete.athlete_id == item.athlete_id) {
                background = resources.getDrawable(R.drawable.selected_ahtlete_background)
            }

            if (item.ath_birth_year > 1000)
                pick_athlete_birth_year.text = "'${item?.ath_birth_year.toString().substring(2, 4)}"
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
package com.apusart.manta.ui.user_module.meets

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.serivces.MeetService
import com.apusart.manta.ui.tools.Const
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.meet_fragment.*
import kotlinx.android.synthetic.main.meet_list_item.view.*
import kotlinx.android.synthetic.main.meets_view_pager.*
import kotlinx.coroutines.launch
import java.lang.Exception

class MeetsFragmentViewModel: ViewModel() {
    val mMeetService = MeetService()
    val mLastMeets = MutableLiveData<List<Meet>>()
    val mInProgress = MutableLiveData<Boolean>()

    fun getLastMeets(athleteId: Int) {
        viewModelScope.launch {
            try {
                mInProgress.value = true
                val resMeets = mMeetService.getMeetsByAthleteId(athleteId)
                mLastMeets.value = resMeets

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mInProgress.value = false
            }
        }
    }
}

class MeetsPager: Fragment(R.layout.meets_view_pager) {
    private lateinit var mMeetsAdapter: MeetsAdapter
    private val navArgs by navArgs<MeetsPagerArgs>()
    private val mMeetsViewModel: MeetsFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMeetsViewModel.mInProgress.observe(viewLifecycleOwner, Observer {
            meets_spinner.isVisible = it
        })

        mMeetsViewModel.mLastMeets.observe(viewLifecycleOwner, Observer {
            meets_list.isVisible = it.isNotEmpty()

            mMeetsAdapter = MeetsAdapter(findNavController(), requireActivity())
            if(it.isEmpty()) {
                no_meets_to_display.isVisible = true
                meets_list.isVisible = false
            } else {
                no_meets_to_display.isVisible = false
                meets_list.isVisible = true

                mMeetsAdapter.submitList(it)
                meets_list.apply {
                    adapter = mMeetsAdapter
                }


            }
        })

        mMeetsViewModel.getLastMeets(Prefs.getUser()!!.athlete_id)
    }
}


class MeetsAdapter(private val navController: NavController, private val activity: Activity): ListAdapter<Meet, MeetViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<Meet>() {
        override fun areItemsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }

        override fun areContentsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.meet_list_item, parent, false)

        return MeetViewHolder(container, navController, activity)
    }

    override fun onBindViewHolder(holder: MeetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class MeetViewHolder(container: View, private val navController: NavController, private val activity: Activity): RecyclerView.ViewHolder(container) {

    fun bind(meet: Meet) {
        itemView.apply {
            meet_list_item_multiple_button.setText(0, "WWW")
            meet_list_item_multiple_button.setButtonIcon(0, R.drawable.www_icon)
            meet_list_item_multiple_button.setText(1, "Lista startowa")
            meet_list_item_multiple_button.setButtonIcon(1, R.drawable.articles_icon)
            meet_list_item_multiple_button.addButton("Wyniki")
            meet_list_item_multiple_button.setButtonIcon(2, R.drawable.stopwatch_icon)
            meet_list_item_multiple_button.addButton("Galeria")
            meet_list_item_multiple_button.setButtonIcon(3, R.drawable.gallery_icon64)

            meet.run {
                meet_list_item_meet_name.text = mt_name
                meet_list_item_meet_city.text = mt_city
                meet_list_item_course.text = Const.courseSize.getString(mt_course_abbr)
                meet_list_item_date.text = resources.getString(R.string.meeting_date, mt_from, mt_to)

                meet_list_item_header_main_container.setOnClickListener {
                    navController.navigate(MeetsPagerDirections.actionMeetsFragmentToMeetFragment(meet_id))
                }

                val areThereAnyPhotos = meet.path != null

                meet_list_item_image_container.setOnClickListener {
                    navController.navigate(MeetsPagerDirections.actionMeetsFragmentToMeetFragment(meet_id))
                }



                if(areThereAnyPhotos) {
                    Glide.with(itemView)
                        .load(Const.baseUrl + meet.path)
                        .error(R.drawable.no_photo)
                        .into(meet_list_item_image)

                    meet_list_item_multiple_button.setButtonOnClickListener(3) {
                        Prefs.setPreviousMeetPhoto(0)
                        navController.navigate(MeetsPagerDirections.actionMeetsFragmentToGalleryFragment(meet_id))
                    }

                } else {
                    meet_list_item_multiple_button.setToInactive(3, R.color.pale_grey_three)

                    Glide.with(itemView)
                        .load(R.drawable.no_photo)
                        .into(meet_list_item_image)
                }

                if(mt_main_page != "") {
                    meet_list_item_multiple_button.setButtonOnClickListener(0) { v ->

                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(mt_main_page))
                        activity.startActivity(intent)
                    }
                } else {
                    meet_list_item_multiple_button.setToInactive(0, R.color.pale_grey_three)
                }

                if(mt_start_list_page != "") {

                    meet_list_item_multiple_button.setButtonOnClickListener(1) { v ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(mt_start_list_page))
                        activity.startActivity(intent)
                    }
                } else {
                    meet_list_item_multiple_button.setToInactive(1, R.color.pale_grey_three)
                }

                if(mt_results_page != "") {

                    meet_list_item_multiple_button.setButtonOnClickListener(2) { v ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(mt_results_page))
                        activity.startActivity(intent)
                    }
                } else {
                    meet_list_item_multiple_button.setToInactive(2, R.color.pale_grey_three)

                }
            }
        }
    }
}
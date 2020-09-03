package com.apusart.manta.ui.user_module.meets

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Meet
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.LoadingScreen
import kotlinx.android.synthetic.main.incoming_meets_fragment.*
import kotlinx.android.synthetic.main.last_meets_fragment.*
import kotlinx.android.synthetic.main.meet_item.view.*
import kotlinx.android.synthetic.main.meets_view_pager.*
import java.lang.Exception

class MeetsPager: Fragment(R.layout.meets_view_pager) {
    private lateinit var meetsFragmentAdapter: MeetsFragmentAdapter
    private val navArgs by navArgs<MeetsPagerArgs>()
    private val meetsViewModel: MeetsViewModel by viewModels()

    private inner class MeetsFragmentAdapter(fm: FragmentManager): FragmentStatePagerAdapter (fm) {
        private val COUNT = meetsViewModel.lastMeets.value?.size ?: 0

        override fun getItem(position: Int): Fragment {
          return MeetFragment(meetsViewModel.lastMeets.value?.get(position)?.meet_id ?: -1)
        }

        override fun getCount(): Int {
            return COUNT
        }

        override fun getPageTitle(position: Int): CharSequence? {

            val meet = meetsViewModel.lastMeets.value?.get(position)
            return  "${meet?.mt_city} ${meet?.mt_from?.substring(0, 4)}"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prevMeetId = Prefs.getPreviousMeetId()
        meetsViewModel.lastMeets.observe(viewLifecycleOwner, Observer {
            meets_view_pager.isVisible = it.isNotEmpty()
            meets_spinner.isVisible = false

            if(it.isEmpty()) {
                no_meets_to_display.isVisible = true
                meets_view_pager.isVisible = false
            } else {
                no_meets_to_display.isVisible = false
                meets_view_pager.isVisible = true
                meetsFragmentAdapter = MeetsFragmentAdapter(childFragmentManager)

                meets_view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        if(position < it.size) {
                            Prefs.setPreviousMeetId(it[position].meet_id)
                        }
                    }

                    override fun onPageSelected(position: Int) {
//
                    }

                })
                meets_view_pager.apply {
                    adapter = meetsFragmentAdapter
                    if(prevMeetId != 0) {
                        currentItem = (it as ArrayList).indexOf(it.firstOrNull { meet -> meet.meet_id == prevMeetId })
                    }
                    setPageMarginDrawable(ColorDrawable(resources.getColor(R.color.black)))
                    pageMargin = 5
                }
            }
        })

        meetsViewModel.getLastMeetsByAthleteId(Prefs.getUser()!!.athlete_id)

    }
}

class LastMeetsFragment: Fragment(R.layout.last_meets_fragment) {
    private val meetsViewModel: MeetsViewModel by viewModels()
    private lateinit var lastMeetsAdapter:  LastMeetsAdapter
    private val athlete = Prefs.getUser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastMeetsAdapter = LastMeetsAdapter()

        meetsViewModel.lastMeets.observe(viewLifecycleOwner, Observer { lastMeets ->

            meets_fragment_last_meets_empty_list_info.isVisible = lastMeets.isEmpty()
            meets_fragment_last_meets_list.isVisible = lastMeets.isNotEmpty()

            lastMeetsAdapter.submitList(lastMeets)
        })

        meetsViewModel.inProgressLastMeets.observe(viewLifecycleOwner, Observer {
            meets_fragment_last_meets_list.isVisible = !it && lastMeetsAdapter.currentList.isNotEmpty()
        })

        meets_fragment_last_meets_list.apply {
            adapter = lastMeetsAdapter
        }

        meetsViewModel.getLastMeetsByAthleteId(athlete!!.athlete_id)

        meets_fragment_last_meets_swipe_layout.setOnRefreshListener {
            meetsViewModel.getLastMeetsByAthleteId(athlete.athlete_id)
            meets_fragment_last_meets_swipe_layout.isRefreshing = false
        }
    }
}

class IncomingMeetsFragment: Fragment(R.layout.incoming_meets_fragment) {
    private val meetsViewModel: MeetsViewModel by viewModels()
    private lateinit var incomingMeetsAdapter: IncomingMeetsAdapter
    private val athlete = Prefs.getUser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomingMeetsAdapter = IncomingMeetsAdapter()

        meets_fragment_incoming_meets_list.apply {
            adapter = incomingMeetsAdapter
        }

        meetsViewModel.incomingMeets.observe(viewLifecycleOwner, Observer { incMeets ->

            meets_fragment_incoming_meets_empty_list_info.isVisible = incMeets.isEmpty()
            meets_fragment_incoming_meets_list.isVisible = incMeets.isNotEmpty()


            incomingMeetsAdapter.submitList(incMeets)
        })

        meetsViewModel.getIncomingMeetsByAthleteId(athlete!!.athlete_id)

        meets_fragment_incoming_meets_swipe_layout.setOnRefreshListener {
            meetsViewModel.getIncomingMeetsByAthleteId(athlete.athlete_id)
            meets_fragment_incoming_meets_swipe_layout.isRefreshing = false
        }
    }
}

class IncomingMeetsAdapter: ListAdapter<Meet, MeetViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<Meet>() {
        override fun areItemsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }

        override fun areContentsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.meet_item, parent, false)

        return MeetViewHolder.IncomingMeet(viewContainer)
    }

    override fun onBindViewHolder(holder: MeetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class LastMeetsAdapter: ListAdapter<Meet, MeetViewHolder>(diffUtil) {

    object diffUtil: DiffUtil.ItemCallback<Meet>() {
        override fun areItemsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }

        override fun areContentsTheSame(oldItem: Meet, newItem: Meet): Boolean {
            return oldItem.meet_id == newItem.meet_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetViewHolder {
        val viewContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.meet_item, parent, false)

        return MeetViewHolder.LastMeets(viewContainer)
    }

    override fun onBindViewHolder(holder: MeetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

sealed class MeetViewHolder(containerView: View): RecyclerView.ViewHolder(containerView) {
    abstract fun bind(meet: Meet)

    class IncomingMeet(containerView: View): MeetViewHolder(containerView) {
        override fun bind(meet: Meet) {
            itemView.apply {
                meet_item_meet_title.text = meet.mt_name
                meet_item_meet_city.text = meet.mt_city
            }
        }
    }

    class LastMeets(containerView: View): MeetViewHolder(containerView) {
        override fun bind(meet: Meet) {
            itemView.apply {
                meet_item_meet_title.text = meet.mt_name
                meet_item_meet_city.text = meet.mt_city
                meet_item_course.text = Const.courseSize.getString(meet.mt_course_abbr)
                meet_item_date.text = resources.getString(R.string.meeting_date, meet.mt_from, meet.mt_to)
            }
        }
    }
}
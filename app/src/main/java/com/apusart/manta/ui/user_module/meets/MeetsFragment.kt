package com.apusart.manta.ui.user_module.meets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Meet
import kotlinx.android.synthetic.main.incoming_meets_fragment.*
import kotlinx.android.synthetic.main.last_meets_fragment.*
import kotlinx.android.synthetic.main.meet_item.view.*
import kotlinx.android.synthetic.main.meets_view_pager.*
import java.lang.Exception

class MeetsPager: Fragment(R.layout.meets_view_pager) {
    private lateinit var meetsFragmentAdapter: MeetsFragmentAdapter
    private val navArgs by navArgs<MeetsPagerArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meetsFragmentAdapter = MeetsFragmentAdapter(childFragmentManager)

        meets_view_pager.apply {
            adapter = meetsFragmentAdapter
            currentItem = navArgs.openOnPage
        }
    }
}

class MeetsFragmentAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    private val COUNT = 3
    override fun getItem(position: Int): Fragment {

        return when(position) {
            0 -> IncomingMeetsFragment()
            1 -> LastMeetFragment()
            2 -> LastMeetsFragment()
            else -> throw Exception("Unsupported fragment")
        }

    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Nadchodzące zawody"
            1 -> "Ostatnie zawody"
            2 -> "Przeszłe zawody"
            else -> throw Exception("Unsupported title")
        }
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
            meets_fragment_last_meets_list.isVisible = !it
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

        return MeetViewHolder.IncomingMeet(viewContainer)
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
                meet_item_meet_nation.text = meet.mt_nation_abbr
            }
        }
    }
}
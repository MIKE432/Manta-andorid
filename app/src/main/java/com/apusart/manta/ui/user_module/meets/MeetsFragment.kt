package com.apusart.manta.ui.user_module.meets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.Prefs
import com.apusart.manta.R
import com.apusart.manta.api.models.Meet
import kotlinx.android.synthetic.main.incoming_meets_fragment.*
import kotlinx.android.synthetic.main.last_meets_fragment.*
import kotlinx.android.synthetic.main.meet_item.view.*
import kotlinx.android.synthetic.main.meets_view_pager.*

class MeetsPager: Fragment(R.layout.meets_view_pager) {
    private lateinit var meetsFragmentAdapter: MeetsFragmentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meetsFragmentAdapter = MeetsFragmentAdapter(childFragmentManager)

        meets_view_pager.adapter = meetsFragmentAdapter
    }
}

class MeetsFragmentAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {
    private val COUNT = 2
    override fun getItem(position: Int): Fragment {
        if(position == 0) {
            return IncomingMeetsFragment()
        }
        return LastMeetsFragment()
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(position == 0) {
            return "Najbliższe zawody"
        }
        return "Odbyte zawody"
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
            lastMeetsAdapter.submitList(lastMeets)
        })

        meets_fragment_last_meets_list.apply {
            adapter = lastMeetsAdapter
        }
        meetsViewModel.getLastMeetsByAthleteId(athlete!!.athlete_id)
    }
}

class IncomingMeetsFragment: Fragment(R.layout.incoming_meets_fragment) {
    private val meetsViewModel: MeetsViewModel by viewModels()
    private lateinit var incomingMeetsAdapter: IncomingMeetsAdapter
    private val athlete = Prefs.getUser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomingMeetsAdapter = IncomingMeetsAdapter()

        meetsViewModel.lastMeets.observe(viewLifecycleOwner, Observer { lastMeets ->
            incomingMeetsAdapter.submitList(lastMeets)
        })

        meets_fragment_incoming_meets_list.apply {
            adapter = incomingMeetsAdapter
        }
        //implement get incomingMeetsGetter
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
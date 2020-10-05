package com.apusart.manta.ui.club_scope_module.ui.club_records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.R
import com.apusart.manta.api.models.Record
import com.apusart.manta.databinding.ClubRecordsFragmentBinding
import com.apusart.manta.ui.tools.AppViewModelFactory
import com.apusart.manta.ui.tools.RecordListItem
import com.apusart.manta.ui.tools.RecordListItemData
import kotlinx.android.synthetic.main.club_records_fragment.*


class ClubRecords: Fragment(R.layout.club_records_fragment) {
    private val viewModel: ClubRecordsViewModel by activityViewModels { AppViewModelFactory(viewLifecycleOwner) }
    private lateinit var binding: ClubRecordsFragmentBinding
    private lateinit var recordsAdapter: ClubRecordsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ClubRecordsFragmentBinding.inflate(inflater, container, false)
        binding.clubRecordsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordsAdapter = ClubRecordsAdapter()

        club_records_list.apply {
            adapter = recordsAdapter
        }

        viewModel.recordsData.observe(viewLifecycleOwner, Observer {
            recordsAdapter.submitList(it)
        })

        filters.setOnClickListener {
            viewModel.rejectFilters()
            findNavController().navigate(ClubRecordsDirections.actionClubRecordsToClubRecordsFiltersFragment())
        }
    }
}

class ClubRecordsAdapter: ListAdapter<RecordListItemData, RecordViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<RecordListItemData>() {
        override fun areItemsTheSame(oldItem: RecordListItemData, newItem: RecordListItemData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RecordListItemData, newItem: RecordListItemData): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val container = RecordListItem(parent.context)

        return RecordViewHolder(container)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}

class RecordViewHolder(private val container: RecordListItem): RecyclerView.ViewHolder(container) {
    fun bind(data: RecordListItemData) {
        container.bind(data)
    }

}
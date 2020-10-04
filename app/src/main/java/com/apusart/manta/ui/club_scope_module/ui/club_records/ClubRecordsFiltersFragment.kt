package com.apusart.manta.ui.club_scope_module.ui.club_records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.apusart.manta.R
import com.apusart.manta.databinding.ClubRecordsFiltersFragmentBinding
import com.apusart.manta.databinding.ClubRecordsFragmentBinding
import com.apusart.manta.ui.tools.AppViewModelFactory
import kotlinx.android.synthetic.main.club_records_filters_fragment.*
import kotlinx.android.synthetic.main.club_records_filters_fragment.view.*
import kotlinx.android.synthetic.main.club_records_fragment.*

class ClubRecordsFiltersFragment: Fragment() {
    private val viewModel: ClubRecordsViewModel by activityViewModels { AppViewModelFactory(viewLifecycleOwner) }
    private lateinit var binding: ClubRecordsFiltersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ClubRecordsFiltersFragmentBinding.inflate(inflater, container, false)
        binding.clubRecordsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ageAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), R.layout.spinner_item, resources.getStringArray(R.array.age_array))


        club_records_fragment_commit_filters_button.setOnClickListener {
            viewModel.commitFilters()
            viewModel.getRecords()
            requireActivity().onBackPressed()
        }

        club_records_fragment_reject_filters_button.setOnClickListener {
            viewModel.rejectFilters()
            requireActivity().onBackPressed()
        }

        club_records_fragment_age_spinner1.adapter = ageAdapter

    }
}
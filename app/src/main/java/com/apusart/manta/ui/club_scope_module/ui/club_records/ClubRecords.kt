package com.apusart.manta.ui.club_scope_module.ui.club_records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.apusart.manta.R
import com.apusart.manta.databinding.ClubRecordsFiltersFragmentBinding
import com.apusart.manta.databinding.ClubRecordsFragmentBinding
import com.apusart.manta.ui.tools.AppViewModelFactory
import kotlinx.android.synthetic.main.club_records_fragment.*


class ClubRecords: Fragment(R.layout.club_records_fragment) {
    private val viewModel: ClubRecordsViewModel by activityViewModels { AppViewModelFactory(viewLifecycleOwner) }
    private lateinit var binding: ClubRecordsFragmentBinding

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


        filters.setOnClickListener {
            findNavController().navigate(ClubRecordsDirections.actionClubRecordsToClubRecordsFiltersFragment())
        }
    }
}
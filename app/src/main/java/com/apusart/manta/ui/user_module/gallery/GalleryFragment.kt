package com.apusart.manta.ui.user_module.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Tools
import kotlinx.android.synthetic.main.gallery_fragment.*
import kotlinx.android.synthetic.main.gallery_slider.*

class GalleryFragment: Fragment(R.layout.gallery_fragment) {
    private val mGalleryViewModel: GalleryViewModel by viewModels()
    private val mArgs by navArgs<GalleryFragmentArgs>()
    private lateinit var mGalleryAdapter: GalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mGalleryAdapter = GalleryAdapter(mArgs.meetId)


        gallery_fragment_gallery.apply {
            adapter = mGalleryAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        mGalleryViewModel.photos.observe(viewLifecycleOwner, Observer {
            mGalleryAdapter.submitList(it)
        })

        if(mArgs.meetId != -1) {
            mGalleryViewModel.getPhotosByMeetId(mArgs.meetId)
        }
    }
}

class SliderFragment: Fragment(R.layout.gallery_slider) {
    private val navArgs by navArgs<SliderFragmentArgs>()
    private lateinit var  mSliderAdapter: SliderAdapter
    private val mGalleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(navArgs.photos?.meetId != null)
            mGalleryViewModel. getMeetByMeetId(navArgs.photos?.meetId!!)

        if(navArgs.photos?.data != null) {
            mSliderAdapter = SliderAdapter(childFragmentManager, navArgs.photos!!.data!!)
        }

        gallery_slider.apply {
            adapter = mSliderAdapter
            currentItem = navArgs.photos?.position ?: 0
        }

        gallery_slider_back_arrow.setImageDrawable(Tools.changeIconColor(R.drawable.back_arrow_icon32, R.color.white, resources))
        gallery_slider_back_arrow.setOnClickListener {
            findNavController().popBackStack()
        }

        mGalleryViewModel.meet.observe(viewLifecycleOwner, Observer {
            gallery_slider_meet_name.text = it.mt_name
        })
    }
}
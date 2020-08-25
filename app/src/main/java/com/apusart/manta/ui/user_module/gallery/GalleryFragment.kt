package com.apusart.manta.ui.user_module.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.apusart.manta.R
import kotlinx.android.synthetic.main.gallery_fragment.*

class GalleryFragment: Fragment(R.layout.gallery_fragment) {
    private val mGalleryViewModel: GalleryViewModel by viewModels()
    private val mArgs by navArgs<GalleryFragmentArgs>()
    private lateinit var mGalleryAdapter: GalleryAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mGalleryAdapter = GalleryAdapter()

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
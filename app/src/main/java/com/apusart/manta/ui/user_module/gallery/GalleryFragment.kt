package com.apusart.manta.ui.user_module.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.tools.Tools
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
            layoutManager = GridLayoutManager(context, Tools.calculateNoOfColumns(context, 128f))
        }


        mGalleryViewModel.photos.observe(viewLifecycleOwner, Observer {
            mGalleryAdapter.submitList(it)
            val scrollToPosition = Prefs.getPreviousMeetPhoto()

            val scrollTo = if(scrollToPosition > it.size) 0 else scrollToPosition
            gallery_fragment_gallery.postDelayed({
                gallery_fragment_gallery.scrollToPosition(scrollTo)
            }, 200)
        })

        mGalleryViewModel.meet.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                gallery_fragment_meet_details.isVisible = true
                gallery_fragment_meet_details.apply {
                    mMeetCity = it.mt_city
                    mMeetTitle = it.mt_name
                    mMeetCourse = Const.courseSize.getString(it.mt_course_abbr, "25m")
                    mMeetDate = resources.getString(R.string.meeting_date, it.mt_from, it.mt_to)
                }
            } else {
                gallery_fragment_meet_details.isVisible = false
            }
        })

        if(mArgs.meetId != -1) {
            mGalleryViewModel.getMeetByMeetId(mArgs.meetId)
        }
    }
}

class SliderFragment: Fragment(R.layout.gallery_slider) {
    private val navArgs by navArgs<SliderFragmentArgs>()
    private lateinit var  mSliderAdapter: SliderAdapter
    private val mGalleryViewModel: GalleryViewModel by viewModels()

    private fun sharePhoto(bitmap: Bitmap?) {
        if(bitmap != null) {
            val bmpUri = Tools.getBitmapFromImageView(bitmap, requireContext())

            if(bmpUri != null) {
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, bmpUri)
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setType("image/*")

                startActivity(Intent.createChooser(intent, "Udostępnij zdjęcie"))
            } else {
                Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(navArgs.photos?.meetId != null)
            mGalleryViewModel.getMeetByMeetId(navArgs.photos?.meetId!!)

        if(navArgs.photos?.links != null) {
            mSliderAdapter = SliderAdapter(childFragmentManager, navArgs.photos!!.links!!, gallery_slider_title, gallery_slider_bottom_tools)
        }

        gallery_slider.apply {
            adapter = mSliderAdapter
            currentItem = navArgs.photos?.position ?: 0
        }

        gallery_slider_share_button.setOnClickListener {

            Glide
                .with(requireContext())
                .asBitmap()
                .dontAnimate()
                .load(Const.baseUrl + (navArgs.photos?.links?.get(gallery_slider.currentItem) ?: ""))
                .into(object: CustomTarget<Bitmap>() {

                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        sharePhoto(resource)
                    }
                })
        }

        mGalleryViewModel.meet.observe(viewLifecycleOwner, Observer {
            gallery_slider_meet_name.text = it.mt_name
        })
    }
}
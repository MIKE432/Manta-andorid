package com.apusart.manta.ui.user_module.gallery

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.apusart.manta.R
import com.apusart.manta.api.models.Photo
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.MiddleLayer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_slider_item.*

class SliderAdapter(fm: FragmentManager, private val links: List<String>, private val title: View, private val tools: View): FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return SliderItem(links[position], title, tools)
    }

    override fun getCount(): Int {
        return links.size
    }
}

class SliderItem(private val photo: String, private val title: View, private val tools: View): Fragment(R.layout.gallery_slider_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MiddleLayer.loadIntoImageView(photo, gallery_slider_item_photo, requireContext())

    }
}


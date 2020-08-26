package com.apusart.manta.ui.user_module.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.apusart.manta.R
import com.apusart.manta.api.models.Photo
import com.apusart.manta.ui.tools.Const
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_slider_item.*

class SliderAdapter(fm: FragmentManager, private val data: List<Photo>): FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return SliderItem(Const.baseUrl + data[position].path)
    }

    override fun getCount(): Int {
        return data.size
    }
}

class SliderItem(private val photo: String): Fragment(R.layout.gallery_slider_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide
            .with(this)
            .load(photo)
            .into(gallery_slider_item_photo)
    }
}


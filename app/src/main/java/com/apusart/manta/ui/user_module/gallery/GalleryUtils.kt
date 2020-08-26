package com.apusart.manta.ui.user_module.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.apusart.manta.R
import com.apusart.manta.api.models.Photo
import com.apusart.manta.navigation.GallerySliderArgument
import com.apusart.manta.ui.tools.Const
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gallery_fragment.view.*
import kotlinx.android.synthetic.main.gallery_item.view.*
import kotlinx.android.synthetic.main.mini_photo_item.view.*

class MiniPhotosAdapter: ListAdapter<Photo, MiniPhotoViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniPhotoViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.mini_photo_item, parent, false)

        return MiniPhotoViewHolder(container)
    }

    override fun onBindViewHolder(holder: MiniPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class GalleryAdapter(private val meetId: Int): ListAdapter<Photo, PhotoViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false)

        return PhotoViewHolder(container)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), position, currentList, meetId)
    }
}

class PhotoViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: Photo, position: Int, currentList: List<Photo>, meetId: Int) {

        itemView.apply {

            gallery_item_photo.setOnClickListener {
                findNavController().navigate(GalleryFragmentDirections.actionGalleryFragmentToSliderFragment(
                    GallerySliderArgument(currentList, position, meetId)
                ))
            }

            Glide
                .with(this)
                .load(Const.baseUrl + item.path_thumb_m)
                .apply(Const.glideMeetPhotoOptions)
                .into(gallery_item_photo)
        }
    }
}

class MiniPhotoViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: Photo) {
        itemView.apply {
            isClickable = true

            Glide
                .with(this)
                .load(Const.baseUrl + item.path_thumb_m)
                .apply(Const.glideMeetPhotoOptions)
                .into(photo)
        }
    }
}
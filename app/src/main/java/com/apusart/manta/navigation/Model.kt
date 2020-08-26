package com.apusart.manta.navigation

import android.os.Parcelable
import com.apusart.manta.api.models.Photo
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class ResultArgument(
    val distance: Int?,
    val ss_abbr: String?,
    val res_course_abbr: String?
): Parcelable

@Parcelize
data class GallerySliderArgument(
    val data: @RawValue List<Photo>?,
    val position: Int,
    val meetId: Int
): Parcelable
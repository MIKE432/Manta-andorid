package com.apusart.manta.navigation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultArgument(
    val distance: Int,
    val ss_abbr: String,
    val res_course_abbr: String
): Parcelable
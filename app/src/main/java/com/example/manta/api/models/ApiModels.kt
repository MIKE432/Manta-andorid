package com.example.manta.api.models

import java.util.*

class Athlete(
    val athlete_id: Int,
    val club_id: Int,
    val ath_from: Date,
    val ath_to: Date,
    val section_id: Int,
    val gender_abbr: String,
    val ath_firstname: String,
    val ath_lastname: String,
    val ath_licence_no: String?,
    val ath_image_min_url: String?,
    val ath_image_max_url: String?,
    val ath_birth_year: Int,
    val ath_birth_date: Date,
    val ath_birth_month: Int,
    val ath_birth_day: Int,
    val ath_status: String,
    val ath_email: String?,
    val ath_contact_name: String,
    val ath_contact_email: String,
    val ath_contact_phone: Long,
    val ath_address: String?,
    val cath_code: String?,
    val ath_swimrankings_id: Int?
)
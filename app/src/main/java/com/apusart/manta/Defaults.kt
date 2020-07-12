package com.apusart.manta

import android.content.Context
import android.content.SharedPreferences
import com.apusart.manta.api.models.Athlete
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson


object Const {
    const val pageUrl = "http://ukp-manta.pl/api/"
    const val baseUrl = "http://ukp-manta.pl"
    const val defaultLimit = 100
    val glideAthleteIconOptions = RequestOptions()
        .placeholder(R.drawable.athlete_icon)
        .error(R.drawable.athlete_icon)
}

object Prefs {
    private const val PREFS_NAME = "com.example.manta.AthletePrefs"
    private const val ATHLETE = "Athlete"
    private var settings: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val gson = Gson()
    private var actualAthlete: Athlete? = null

    fun AthletePreference(context: Context?) {

        if(context != null) {
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            editor = settings?.edit()
        }
    }

    fun removeUser() {
        editor?.remove(ATHLETE)
        editor?.commit()
        actualAthlete = null
    }
    fun storeUser(athlete: Athlete) {
        val stringifiedAthlete = gson.toJson(athlete)
        editor?.putString(ATHLETE, stringifiedAthlete)
        editor?.commit()
        actualAthlete = athlete
    }

    fun getUser(): Athlete? {
        if(actualAthlete != null)
            return actualAthlete
        return gson.fromJson((settings?.getString(ATHLETE,"") ?: ""), Athlete::class.java)
    }
}
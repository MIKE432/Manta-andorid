package com.example.manta.api.calls


import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.manta.api.models.AthletesResponse
import com.example.manta.api.models.LastMeetsResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MantaEndpoints {
    @GET("athletes")
    suspend fun getAthletes(): AthletesResponse

    @GET("athletes/{id}/last-meets/{limit}")
    suspend fun getLastMeetsByAthleteId(@Path("id") id: Int, @Path("limit") limit: Int): LastMeetsResponse
}

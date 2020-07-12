package com.apusart.manta.api.calls


import com.apusart.manta.api.models.AthletesResponse
import com.apusart.manta.api.models.LastMeetsResponse
import com.apusart.manta.api.models.MostValuableResultsResponse
import com.apusart.manta.api.models.PersonalBestsResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MantaEndpoints {
    @GET("athletes")
    suspend fun getAthletes(): AthletesResponse

    @GET("athletes/{id}/last-meets")
    suspend fun getLastMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): LastMeetsResponse

    @GET("athletes/{id}/most-valuable-results")
    suspend fun getMostValuableResultsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): MostValuableResultsResponse

    @GET("athletes/{id}/personal-best-results")
    suspend fun getPersonalBestsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): PersonalBestsResponse

}

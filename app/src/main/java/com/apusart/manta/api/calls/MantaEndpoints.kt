package com.apusart.manta.api.calls


import com.apusart.manta.api.models.*

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MantaEndpoints {
    @GET("athletes")
    suspend fun getAthletes(): AthletesResponse

    @GET("athletes/{id}/last-meets")
    suspend fun getLastMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): LastMeetsResponse

    @GET("athletes/{id}/incoming-meets")
    suspend fun getIncomingMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): IncomingMeetsResponse

    @GET("athletes/{id}/most-valuable-results")
    suspend fun getMostValuableResultsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): MostValuableResultsResponse

    @GET("athletes/{id}/personal-best-results")
    suspend fun getPersonalBestsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): PersonalBestsResponse

    @GET("athletes/{id}/medal-stats")
    suspend fun getMedalStatsByAthleteId(@Path("id") id: Int, @Query("grade-abbr") grade: String?,  @Query("up-to-place") upToPlace: Int? = 3): MedalStatsResponse

    @GET("athletes/{id}/results")
    suspend fun getResultsByAthleteId(@Path("id") id: Int, @Query("limit") grade: Int? = 3, @Query("ss_abbr") ss_abbr: String? = null, @Query("ss_abbr") distance: Int? = null): ResultsResponse
}

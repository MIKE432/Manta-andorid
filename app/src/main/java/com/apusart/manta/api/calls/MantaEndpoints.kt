package com.apusart.manta.api.calls


import com.apusart.manta.api.models.*

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MantaEndpoints {
    @GET("athletes")
    suspend fun getAthletes(): AthletesResponse

    @GET("athletes/{id}/last-meets")
    suspend fun getLastMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): MeetsResponse

    @GET("athletes/{id}/meets")
    suspend fun getAllMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): MeetsResponse

    @GET("athletes/{id}/incoming-meets")
    suspend fun getIncomingMeetsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): IncomingMeetsResponse

    @GET("athletes/{id}/most-valuable-results")
    suspend fun getMostValuableResultsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?): MostValuableResultsResponse

    @GET("athletes/{id}/personal-best-results")
    suspend fun getPersonalBestsByAthleteId(@Path("id") id: Int, @Query("limit") limit: Int?, @Query("style") ss_abbr: String? = null, @Query("distance") distance: Int? = null, @Query("course") course: String? = null): PersonalBestsResponse

    @GET("athletes/{id}/medals")
    suspend fun getMedalStatsByAthleteId(@Path("id") id: Int, @Query("grade-abbr") grade: String?,  @Query("up-to-place") upToPlace: Int? = null): MedalStatsResponse

    @GET("athletes/{id}/results")
    suspend fun getResultsByAthleteId(@Path("id") id: Int, @Query("limit") grade: Int? = 3, @Query("style") ss_abbr: String? = null, @Query("distance") distance: Int? = null, @Query("course") course: String? = null): ResultsResponse

    @GET("records")
    suspend fun getRecords(@Query("age") age: Int? = null, @Query("style") ss_abbr: String? = null, @Query("distance") distance: Int? = null, @Query("course") course: String? = null, @Query("gender") gender: String? = null): RecordsResponse

    @GET("athletes/{id}/meets/{meet_id}/results")
    suspend fun getMeetDetailsByAthleteId(@Path("id") id: Int, @Path("meet_id") meetId: Int): MeetDetailsResponse

    @GET("meets/{id}/photos")
    suspend fun getPhotosByMeetId(@Path("id") id: Int): MeetPhotosResponse

    @GET("meets/{id}")
    suspend fun getMeetById(@Path("id") id: Int): MeetDetailsResponse

    @GET("articles")
    suspend fun getArticles(): ArticlesResponse
}

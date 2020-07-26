package com.apusart.manta.api.serivces

import com.apusart.manta.ui.tools.Const
import com.apusart.manta.api.calls.MantaEndpoints
import com.apusart.manta.api.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AthletesService {
    private val endpoints: MantaEndpoints

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level =  HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.pageUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        endpoints = retrofit.create(MantaEndpoints::class.java)
    }

    suspend fun getAthletes(): List<Athlete> {
        val result = endpoints.getAthletes()
        return result.athletes ?: listOf()
    }

    suspend fun getMostValuableResultsByAthleteId(id: Int, limit: Int? = Const.defaultLimit): List<MostValuableResult> {
        val result = endpoints.getMostValuableResultsByAthleteId(id, limit)
        return result.mostValuableResults ?: listOf()
    }

    suspend fun getPersonalBestsByAthleteId(id: Int, limit: Int? = Const.defaultLimit, ss_abbr: String? = null, distance: Int? = null, course: String? = null): List<PersonalBest> {
        val result = endpoints.getPersonalBestsByAthleteId(id, limit, ss_abbr, distance, course)
        return result.personalBestResults ?: listOf()
    }

    suspend fun getMedalStatsByAthleteId(id: Int, grade: String? = null): List<MedalStat> {
        val result = endpoints.getMedalStatsByAthleteId(id, grade)
        return result.medalStats ?: listOf()
    }

    suspend fun getResultsByAthleteId(id: Int, limit: Int? = null, ss_abbr: String? = null, distance: Int? = null, course: String? = null): List<Result> {
        val result = endpoints.getResultsByAthleteId(id, limit, ss_abbr, distance, course)
        return result.results
    }

}
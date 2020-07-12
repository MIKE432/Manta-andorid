package com.apusart.manta.api.serivces

import android.util.Log
import com.apusart.manta.Const
import com.apusart.manta.api.calls.MantaEndpoints
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.api.models.MostValuableResult
import com.apusart.manta.api.models.PersonalBest
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

    suspend fun getPersonalBestsByAthleteId(id: Int, limit: Int? = Const.defaultLimit): List<PersonalBest> {
        val result = endpoints.getPersonalBestsByAthleteId(id, limit)
        return result.personalBestResults ?: listOf()
    }

}
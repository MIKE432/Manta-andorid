package com.example.manta.api.serivces

import com.example.manta.Const
import com.example.manta.api.calls.MantaEndpoints
import com.example.manta.api.models.Athlete
import com.example.manta.api.models.AthletesResponse
import com.example.manta.api.models.Meet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

}
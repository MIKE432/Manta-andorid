package com.apusart.manta.api.serivces

import com.apusart.manta.api.calls.MantaEndpoints
import com.apusart.manta.api.models.Record
import com.apusart.manta.ui.tools.Const
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class MantaService {
    private val endpoints: MantaEndpoints

    init {
        val client = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Const.pageUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        endpoints = retrofit.create(MantaEndpoints::class.java)
    }

    suspend fun getRecords(age: Int? = null, ss_abbr: String? = null, distance: Int? = null, course: String? = null, gender: String? = null): List<Record> {
        return endpoints.getRecords(age, ss_abbr,distance,course,gender).records
    }
}
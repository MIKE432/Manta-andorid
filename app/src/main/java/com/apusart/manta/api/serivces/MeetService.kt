package com.apusart.manta.api.serivces

import com.apusart.manta.ui.tools.Const
import com.apusart.manta.api.calls.MantaEndpoints
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.models.Photo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MeetService {
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

    suspend fun getMeetsByAthleteId(id: Int, limit: Int? = Const.defaultLimit): List<Meet> {
        val result = endpoints.getAllMeetsByAthleteId(id, limit)
        return result.meets ?: listOf()
    }

    suspend fun getIncomingMeetsByAthleteId(id: Int, limit: Int? = Const.defaultLimit): List<Meet> {
        val result = endpoints.getIncomingMeetsByAthleteId(id, limit)
        return result.IncomingMeets ?: listOf()
    }


    suspend fun getMeetsDetailsByAthleteId(id: Int, meet_id: Int): Meet? {
        val result = endpoints.getMeetDetailsByAthleteId(id, meet_id)
        return result.meet
    }

    suspend fun getPhotosByMeetId(id: Int): List<Photo>? {
        val result = endpoints.getPhotosByMeetId(id)
        return result.photos
    }
}
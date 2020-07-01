package com.example.manta.api.calls

import com.example.manta.api.models.Athlete
import com.example.manta.api.models.AthletesResponse
import com.google.gson.Gson
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import kotlin.concurrent.thread
object Const {
    const val pageUrl = "http://ukp-manta.pl"
}
object Methods {
    const val GET: String = "GET"
    const val POST: String = "POST"
}

inline fun safe(f: () -> Unit) {
    try {
        f()
    } catch(e: Exception) {
        e.printStackTrace()
    }
}

object CALL {
    object GET {
        fun athletes(callBack: (data:AthletesResponse?) -> Unit) {
            safe {
               thread {
                   val url = URL("${Const.pageUrl}/api/athletes")
                   val connection = url.openConnection() as HttpURLConnection

                   connection.apply {
                       requestMethod = Methods.GET
                   }

                   val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                   var inputLine: String?
                   val content = StringBuilder()

                   while(inputStream.readLine().also { inputLine = it } != null) {
                       content.append(inputLine)
                   }

                   inputStream.close()

                   val contentStr = content.toString()

                   val gson = Gson()

                   callBack(gson.fromJson(contentStr, AthletesResponse::class.java))
               }
            }
        }
    }

    object POST {
        fun athlete(mockup: String) {

        }
    }
}


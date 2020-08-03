package com.apusart.manta.ui.tools

import android.content.Context
import android.content.SharedPreferences
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.os.bundleOf
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


object Const {
    const val pageUrl = "http://ukp-manta.pl/api/"
    const val baseUrl = "http://ukp-manta.pl"
    const val defaultLimit = 100
    val glideAthleteIconOptions = RequestOptions()
        .placeholder(R.drawable.athlete_icon)
        .error(R.drawable.athlete_icon)
    val gradesAbbr = bundleOf(
        "0" to "IO",
        "1" to "PS",
        "2" to "MS",
        "3" to "ME",
        "4" to "MR",
        "5" to "WP",
        "6" to "MG",
        "7" to "MP",
        "8" to "MO",
        "9" to "ZM",
        "10" to "ZO",
        "11" to "ZW"
    )

    val grades = bundleOf(
        "0" to "Igrzyska Olimpijskie",
        "1" to "Puchar Świata",
        "2" to "Mistrzostwa Świata",
        "3" to "Mistrzostwa Europy",
        "4" to "Mistrzostwa Regionu",
        "5" to "Międzynarodowy Wielomecz",
        "6" to "Główne Mistrzostwa Polski",
        "7" to "Mistrzostwa Polski",
        "8" to "Mistrzostwa okręgu",
        "9" to "Międzynarodowe zawody",
        "10" to "Zawody",
        "11" to "Sprawdzian klubowy"
    )

    val styles = bundleOf(
        "klasyczny" to "BT",
        "motylkowy" to "FL",
        "zmienny" to "ME",
        "dowolny" to "FR",
        "grzbietowy" to "BK"
    )

    val stylesRev = bundleOf(
        "BT" to "klasyczny",
        "FL" to "motylkowy",
        "ME" to "zmienny",
         "FR" to "dowolny",
        "BK" to "grzbietowy"
    )

    val courseSize = bundleOf(
        "SCM" to "Basen 25m",
        "LCM" to "Basen 50m"
    )
}

object Font {
    const val ROBOTO_BLACK = "fonts/Roboto-Black.ttf"
    const val ROBOTO_BLACK_ITALIC = "fonts/Roboto-BlackItalic.ttf"
    const val ROBOTO_BOLD = "fonts/Roboto-Bold.ttf"
    const val ROBOTO_BOLD_ITALIC = "fonts/Roboto-BoldItalic.ttf"
    const val ROBOTO_ITALIC = "fonts/Roboto-Italic.ttf"
    const val ROBOTO_LIGHT = "fonts/Roboto-Light.ttf"
    const val ROBOTO_LIGHT_ITALIC = "fonts/Roboto-LightItalic.ttf"
    const val ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf"
    const val ROBOTO_MEDIUM_ITALIC = "fonts/Roboto-MediumItalic.ttf"
    const val ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf"
    const val ROBOTO_THIN = "fonts/Roboto-Thin.ttf"
    const val ROBOTO_THIN_ITALIC = "fonts/Roboto-ThinItalic.ttf"

}

object Tools {
    private val monthsNames = bundleOf(
        "Jan" to 1,
        "Feb" to 2,
        "Mar" to 3,
        "Apr" to 4,
        "May" to 5,
        "Jun" to 6,
        "Jul" to 7,
        "Aug" to 8,
        "Sep" to 9,
        "Oct" to 10,
        "Nov" to 11,
        "Dec" to 12
    )

    fun toDp(x: Int, context: Context): Int {
        return (x * (context.resources.displayMetrics.density)).toInt()
    }

    fun parseDate(date: String?): String {
        if (date == null)
            return "Brak"

        var month = monthsNames.getInt(date.substring(0, 3))
        var day = date.substring(4, 6)
        var year: String
        try {
            day.toInt()
            year = date.substring(8, 12)
        } catch (e: Exception) {
            day = date.substring(4, 5)
            year = date.substring(7, 11)
        }

        val sMonth = if (month < 10) "0${month}" else month.toString()
        val sDay = if (day.toInt() < 10) "0${day}" else day

        return "${sDay}-${sMonth}-$year"
    }

    fun convertResult(sec: Float?): String {

        if(sec == null)
            return "Brak"
        val millis = (sec * 100).toInt()
        val minutes = millis / 100 / 60
        val seconds = millis / 100 % 60
        val m = millis % 100
        return "${
        if((minutes < 10) and (minutes != 0)) "0${minutes}:"
        else if((minutes == 0)) ""
        else "$minutes:"}${if(seconds < 10) "0${seconds}" else "$seconds"}.${if(m < 10) "0${m}" else "${m}"}"
    }

    class SplitTimesPairs(val odd: String, val even: String)

    fun splitLaps(splittedLaps: String): List<String> {
        val splittedLaps = splittedLaps.split(" ")
        val splittedTimesPairs = arrayListOf<SplitTimesPairs>()
        var odd: String = ""
        var even: String = ""
        splittedLaps.forEachIndexed { index, s ->
            if(index % 2 == 0) {
                odd = s
            } else {
                even = s
                splittedTimesPairs.add(SplitTimesPairs(odd, even))
            }
        }
        return splittedLaps
    }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
}

object Prefs {
    private const val PREFS_NAME = "com.example.manta.AthletePrefs"
    private const val ATHLETE = "Athlete"
    private var settings: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val gson = Gson()
    private var actualAthlete: Athlete? = null

    fun AthletePreference(context: Context?) {

        if(context != null) {
            settings = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE)

            editor = settings?.edit()
        }
    }

    fun removeUser() {
        editor?.remove(ATHLETE)
        editor?.commit()
        actualAthlete = null
    }
    fun storeUser(athlete: Athlete) {
        val stringifiedAthlete = gson.toJson(athlete)
        editor?.putString(ATHLETE, stringifiedAthlete)
        editor?.commit()
        actualAthlete = athlete
    }

    fun getUser(): Athlete? {
        if(actualAthlete != null)
            return actualAthlete
        return gson.fromJson((settings?.getString(
            ATHLETE,"") ?: ""), Athlete::class.java)
    }
}

open class OnSwipeTouchListener(val ctx: Context): View.OnTouchListener {
    open val SWIPE_THRESHOLD = 100
    open val SWIPE_VELOCITY_THRESHOLD = 100

    private var gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(ctx, object: GestureListener() {})
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    open fun onSwipeBottom() {}
    open fun onSwipeTop() {}
    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    open fun onClick() {}
    open inner class GestureListener: GestureDetector.SimpleOnGestureListener() {


        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false

            try {
                val diffY = e1?.y?.let { e2?.y?.minus(it) }
                val diffX = e1?.x?.let { e2?.x?.minus(it) }
                if (abs(diffX!!) > abs(diffY!!)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if(diffX > 0)
                            onSwipeLeft()
                        else
                            onSwipeRight()

                        result = true
                    }
                } else if(abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if(diffY > 0)
                        onSwipeBottom()
                    else
                        onSwipeTop()

                    result = true
                } else {
                    onClick()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            return result
        }
    }
}
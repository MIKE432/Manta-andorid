package com.apusart.manta.ui.tools

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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

    val glideMeetPhotoOptions = RequestOptions()
        .placeholder(R.drawable.gradient_list)

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
        "7" to "Mistrzostwa Polski Junior\\u00f3w",
        "8" to "Mistrzostwa okr\\u0119gu",
        "9" to "Miting p\\u0142ywacki",
        "10" to "Zawody lokalne",
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
        "SCM" to "25m",
        "LCM" to "50m"
    )
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

    val medal = bundleOf(
        "1" to R.drawable.medal_gold_icon32,
        "2" to R.drawable.medal_silver_icon32,
        "3" to R.drawable.medal_bronze_icon32
    )

    fun toDp(x: Int): Int {
        return (x * (Resources.getSystem().displayMetrics.density) + 0.5f).toInt()
    }

    fun changeIconColor(src: Int, newColor: Int, resources: Resources): Drawable? {
        val newSrc = ResourcesCompat.getDrawable(resources, src, null)
        newSrc?.setTint(resources.getColor(newColor))
        return newSrc
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
            return "--:--.--"
        val millis = (sec * 100).toInt()
        val minutes = millis / 100 / 60
        val seconds = millis / 100 % 60
        val m = millis % 100
        return "${
        if((minutes < 10) and (minutes != 0)) "$minutes:"
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

    fun getBitmapFromImageView(bitmap: Bitmap, context: Context): Uri? {

        var bmpUri: Uri? = null
        try {

            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun storeToDirectory(imageView: ImageView, context: Context, fileName: String) {
        val drawable = imageView.drawable
        val bmp = (drawable as BitmapDrawable).bitmap

        val filePath = Environment.DIRECTORY_DCIM + "/Manta/"
        val dir = File(filePath)
        val file = File(dir, fileName)
        try {
            val outputStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(context, "Zapisano!", Toast.LENGTH_SHORT).show()
            outputStream.flush()
            outputStream.close()
        } catch(e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp: Float = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}

object Prefs {
    private const val PREFS_NAME = "com.example.manta.AthletePrefs"
    private const val ATHLETE = "actual_athlete"
    private const val PREV_ATHLETE = "prev_athlete"
    private const val PREV_MEET_TAB = "prev_meet_tab"
    private const val PREV_MEET_PHOTO = "prev_meet_photo"
    private const val CURRENT_THEME = "current_theme"
    private var settings: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val gson = Gson()
    private var mActualAthlete: Athlete? = null
    private var mPreviousAthlete: Athlete? = null
    private var mPreviousMeetId = 0
    private var mPreviousMeetPhoto = 0
    private var mCurrentTheme = 0

    fun athletePreference(context: Context?) {

        if(context != null) {
            settings = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE)

            editor = settings?.edit()
        }
    }

    fun removeUser() {
        mPreviousAthlete = gson.fromJson((settings?.getString(ATHLETE, "") ?: ""), Athlete::class.java)

        val stringifiedPrevAthlete = gson.toJson(mPreviousAthlete)
        editor?.putString(PREV_ATHLETE, stringifiedPrevAthlete)
        editor?.commit()

        editor?.remove(ATHLETE)
        editor?.commit()
        mActualAthlete = null
    }

    fun storeUser(athlete: Athlete) {
        mPreviousAthlete = gson.fromJson((settings?.getString(ATHLETE, "") ?: ""), Athlete::class.java)

        val stringifiedPrevAthlete = gson.toJson(mPreviousAthlete)
        editor?.putString(PREV_ATHLETE, stringifiedPrevAthlete)
        editor?.commit()

        val stringifiedAthlete = gson.toJson(athlete)
        editor?.putString(ATHLETE, stringifiedAthlete)
        editor?.commit()
        mActualAthlete = athlete
    }

    fun getUser(): Athlete? {
        if(mActualAthlete != null)
            return mActualAthlete
        return gson.fromJson((settings?.getString(
            ATHLETE,"") ?: ""), Athlete::class.java)
    }

    fun getPreviousAthlete(): Athlete? {
        if(mPreviousAthlete != null)
            return mPreviousAthlete
        return gson.fromJson((settings?.getString(
            PREV_ATHLETE,"") ?: ""), Athlete::class.java)
    }

    fun setPreviousMeetId(id: Int) {
        mPreviousMeetId = id
        editor?.putInt(PREV_MEET_TAB, mPreviousMeetId)
        editor?.commit()
    }

    fun getPreviousMeetId(): Int {

        mPreviousMeetId = settings?.getInt(
            PREV_MEET_TAB,0) ?: 0
        return mPreviousMeetId
    }

    fun removeLastMeetId() {
        mPreviousMeetId = 0
        editor?.putInt(PREV_MEET_TAB, mPreviousMeetId)
        editor?.commit()
    }

    fun setPreviousMeetPhoto(photoNo: Int) {
        mPreviousMeetPhoto = photoNo
        editor?.putInt(PREV_MEET_PHOTO, mPreviousMeetPhoto)
        editor?.commit()
    }


    fun getPreviousMeetPhoto(): Int {
        mPreviousMeetPhoto = settings?.getInt(
            PREV_MEET_PHOTO,0) ?: 0
        return mPreviousMeetPhoto
    }

    fun toggleCurrentTheme() {

        mCurrentTheme = settings?.getInt(
            CURRENT_THEME,0) ?: 0
        if(mCurrentTheme == 0) {
            mCurrentTheme = 1
            editor?.putInt(CURRENT_THEME, mCurrentTheme)
            editor?.commit()
        } else {
            mCurrentTheme = 0
            editor?.putInt(CURRENT_THEME, mCurrentTheme)
            editor?.commit()
        }
    }

    fun toggleCurrentTheme(b: Boolean) {

        if(b) {
            mCurrentTheme = 1
            editor?.putInt(CURRENT_THEME, mCurrentTheme)
            editor?.commit()
        } else {
            mCurrentTheme = 0
            editor?.putInt(CURRENT_THEME, mCurrentTheme)
            editor?.commit()
        }
    }

    fun getCurrentTheme(): Int  {
        mCurrentTheme = settings?.getInt(
            CURRENT_THEME,0) ?: 0

        return mCurrentTheme
    }
}

class ThemeUtils {
    companion object ThemeUtils {

        var sTheme = R.style.Manta_Theme_Dark
        fun setTheme(activity: Activity) {
            activity.setTheme(sTheme)
        }
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

class LoadingScreen: Fragment(R.layout.loading_screen)
class DevToolFragment: Fragment(R.layout.dev_tools_fragment)
class GenericFileProvider : FileProvider()
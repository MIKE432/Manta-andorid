package com.apusart.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.api.models.Record
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import com.apusart.manta.api.serivces.MantaService
import com.apusart.manta.ui.GeneralMedalStats
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ComparedResult(val result: Result, val record: Record?) {
    val progress = 1f - (result.res_prev_best_time?.toFloat()?.let {
        result.res_total_time?.toFloat()?.div(
            it
        )
    }
        ?: 0f)
}

class MeetViewModel: ViewModel() {
    private val athletesService = AthletesService()
    private val mantaService = MantaService()

    val results = MutableLiveData<List<Result>>()
    val meetResultCompared = MutableLiveData<List<ComparedResult>>()
    val mRecords = MutableLiveData<List<Record>>()
    val inProgress = MutableLiveData(false)
    val meetMedalStat = MutableLiveData<GeneralMedalStats>()
    val mShowContent = MutableLiveData(false)


    fun getResultsFromLastMeetByAthleteId(athlete: Athlete) {
        viewModelScope.launch {
            try {
                inProgress.value = true
                val result = athletesService.getResultsByAthleteId(athlete.athlete_id)
                val lastMeetId = result.takeIf { it.isNotEmpty() }
                    ?.get(0)?.meet_id
                val age = Calendar.getInstance().get(Calendar.YEAR) - athlete.ath_birth_year
                val records = mantaService.getRecords(age = age, gender = athlete.gender_abbr, course = result.takeIf { it.isNotEmpty() }?.get(0)?.mt_course_abbr)
                mRecords.value = records

               if(lastMeetId != null) {
                   meetResultCompared.value = result.takeIf { it.isNotEmpty() }
                       ?.filter { it.meet_id == lastMeetId }
                       ?.map { return@map ComparedResult(it, records.firstOrNull { record -> record.style_abbr ==  it.style_abbr && record.sev_distance == it.sev_distance }) }
               }

                meetMedalStat.value = GeneralMedalStats(meetResultCompared.value?.count { it.result.res_place == 1 }, meetResultCompared.value?.count { it.result.res_place == 2 }, meetResultCompared.value?.count { it.result.res_place == 3 })

                inProgress.value = false
                mShowContent.value = !inProgress.value!! && lastMeetId != null
            } catch (e: Exception) {}
        }
    }


    fun getResultsFromMeetByAthleteId(athlete: Athlete, meet_id: Int?) {
        viewModelScope.launch {
            try {
                inProgress.value = true


                val result = athletesService.getResultsByAthleteId(athlete.athlete_id)

                val age = Calendar.getInstance().get(Calendar.YEAR) - athlete.ath_birth_year
                val records = mantaService.getRecords(age = age, gender = athlete.gender_abbr, course = result.takeIf { it.isNotEmpty() }?.get(0)?.mt_course_abbr)
                mRecords.value = records

                if(meet_id != null) {
                    meetResultCompared.value = result.takeIf { it.isNotEmpty() }
                        ?.filter { it.meet_id == meet_id }
                        ?.map { return@map ComparedResult(it, records.firstOrNull { record -> record.style_abbr ==  it.style_abbr && record.sev_distance == it.sev_distance }) }
                }

                meetMedalStat.value = GeneralMedalStats(meetResultCompared.value?.count { it.result.res_place == 1 }, meetResultCompared.value?.count { it.result.res_place == 2 }, meetResultCompared.value?.count { it.result.res_place == 3 })

                inProgress.value = false
                mShowContent.value = !inProgress.value!! && meet_id != null
            } catch (e: Exception) {}
        }
    }
}
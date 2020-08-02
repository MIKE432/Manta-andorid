package com.apusart.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import com.apusart.manta.ui.GeneralMedalStats
import kotlinx.coroutines.launch
import java.lang.Exception

class ComparedResult(val result: Result) {
    val progress = 1f - (result.res_prev_best_time?.toFloat()?.let {
        result.res_total_time?.toFloat()?.div(
            it
        )
    }
        ?: 0f)
}

class LastMeetViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val results = MutableLiveData<List<Result>>()
    val lastMeetResultCompared = MutableLiveData<List<ComparedResult>>()
    val inProgress = MutableLiveData(false)
    val lastMeetMedalStat = MutableLiveData<GeneralMedalStats>()

    fun getResultsFromLastMeetByAthleteId(id: Int) {
        viewModelScope.launch {
            try {
                inProgress.value = true
                val result = athletesService.getResultsByAthleteId(id)
                val lastMeetId = result.takeIf { it.isNotEmpty() }
                    ?.get(0)?.meet_id

                lastMeetResultCompared.value = result.takeIf { it.isNotEmpty() }?.filter { it.meet_id == lastMeetId }?.map { return@map ComparedResult(it) }
                lastMeetMedalStat.value = GeneralMedalStats(lastMeetResultCompared.value?.count { it.result.res_place == 1 }, lastMeetResultCompared.value?.count { it.result.res_place == 2 }, lastMeetResultCompared.value?.count { it.result.res_place == 3 })

                inProgress.value = false
            } catch (e: Exception) {}
            finally {
                inProgress.value = false
            }
        }
    }
}
package com.apusart.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import com.apusart.manta.ui.GeneralMedalStats
import kotlinx.coroutines.launch
import java.lang.Exception

class LastMeetViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val results = MutableLiveData<List<Result>>()
    val lastMeetResult = MutableLiveData<List<Result>>()
    val inProgress = MutableLiveData(false)
    val lastMeetMedalStat = MutableLiveData<GeneralMedalStats>()

    fun getResultsFromLastMeetByAthleteId(id: Int) {
        viewModelScope.launch {
            try {
                inProgress.value = true
                val result = athletesService.getResultsByAthleteId(id, 25)
                val lastMeetId = result.takeIf { it.isNotEmpty() }
                    ?.get(0)?.meet_id

                lastMeetResult.value = result.takeIf { it.isNotEmpty() }?.filter { it.meet_id == lastMeetId }
                lastMeetMedalStat.value = GeneralMedalStats(lastMeetResult.value?.count { it.res_place == 1 }, lastMeetResult.value?.count { it.res_place == 2 }, lastMeetResult.value?.count { it.res_place == 3 })
                inProgress.value = false
            } catch (e: Exception) {}
            finally {
                inProgress.value = false
            }
        }
    }
}
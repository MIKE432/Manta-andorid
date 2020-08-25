package com.apusart.manta.ui.user_module.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.models.MostValuableResult
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import com.apusart.manta.api.serivces.MeetService
import com.apusart.manta.ui.GeneralMedalStats
import com.apusart.manta.ui.tools.Const
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel: ViewModel() {
    private val mAthletesService = AthletesService()
    private val mMeetService = MeetService()
    val isInProgress = MutableLiveData(false)
    val mGeneralMedalStats = MutableLiveData(GeneralMedalStats())
    val mostValuableResults = MutableLiveData<List<MostValuableResult>>()
    val results = MutableLiveData<List<Result>>()
    val incomingMeets = MutableLiveData<List<Meet>>()
    val lastMeet = MutableLiveData<Meet>()


    fun getInfoForDashboardByAthleteId(id: Int, limit: Int? = 10, ss_abbr: String? = null, distance: Int? = null, course: String? = null, dsq: String? = "") {
        viewModelScope.launch {


            try {
                isInProgress.value = true
                val result = mAthletesService.getMedalStatsByAthleteId(id)
                val allMedals = result.filter { it.mt_grade_abbr == "ALL" }.takeIf { it.isNotEmpty() }
                    ?.get(0)
                val gold = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 1 }?.res_count ?: 0
                val silver = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 2 }?.res_count ?: 0
                val bronze = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 3 }?.res_count ?: 0

                mGeneralMedalStats.value = GeneralMedalStats(gold, silver, bronze)

                mostValuableResults.value = mAthletesService.getMostValuableResultsByAthleteId(id)

                results.value = mAthletesService.getResultsByAthleteId(id, limit, ss_abbr, distance, course).filter { it.res_total_time != null && (if(dsq != "") true else dsq == it.res_dsq_abbr)}

                incomingMeets.value = mMeetService.getIncomingMeetsByAthleteId(id, limit)
                lastMeet.value = mMeetService.getMeetsByAthleteId(id, Const.defaultLimit).takeIf { it.isNotEmpty() }?.get(0)


                isInProgress.value = false

            } catch(e: Exception) { e.printStackTrace() }
            finally {
                isInProgress.value = false
            }
        }
    }
}
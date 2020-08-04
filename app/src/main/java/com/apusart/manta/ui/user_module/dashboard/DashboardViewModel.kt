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
    val lastMeets = MutableLiveData<List<Meet>>()


    fun getInfoForDashboardByAthleteId(id: Int, limit: Int? = 10, ss_abbr: String? = null, distance: Int? = null, course: String? = null, dsq: String? = "") {
        viewModelScope.launch {

            try {
                isInProgress.value = true
                val medalStats = mAthletesService.getMedalStatsByAthleteId(id, null)
                if(medalStats.isNotEmpty()) {
                    var gold = medalStats.firstOrNull { medalStat -> medalStat.res_place == 1 }
                    var silver = medalStats.firstOrNull { medalStat -> medalStat.res_place == 2 }
                    var bronze = medalStats.firstOrNull { medalStat -> medalStat.res_place == 3 }

                    mGeneralMedalStats.value = GeneralMedalStats(gold?.res_count ?: 0, silver?.res_count ?: 0, bronze?.res_count ?: 0)
                }

                mostValuableResults.value = mAthletesService.getMostValuableResultsByAthleteId(id)

                results.value = mAthletesService.getResultsByAthleteId(id, limit, ss_abbr, distance, course).filter { it.res_total_time != null && (if(dsq != "") true else dsq == it.res_dsq_abbr)}

                incomingMeets.value = mMeetService.getIncomingMeetsByAthleteId(id, limit)
                lastMeets.value =  mMeetService.getLastMeetsByAthleteId(id, Const.defaultLimit)
                isInProgress.value = false
            } catch(e: Exception) { e.printStackTrace() }
            finally {
                isInProgress.value = false
            }
        }
    }
}
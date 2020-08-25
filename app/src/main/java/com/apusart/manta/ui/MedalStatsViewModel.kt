package com.apusart.manta.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.MedalStat
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class GeneralMedalStats(val gold: Int? = 0, val silver: Int? = 0, val bronze: Int? = 0)
class Stats(val gold: Int? = 0, val silver: Int? = 0, val bronze: Int? = 0, val otherplaces: List<MedalStat>?, val grade: String?)

class MedalStatsViewModel: ViewModel() {
    private val mAthletesService = AthletesService()
    val mGeneralMedalStats = MutableLiveData(GeneralMedalStats())
    val mAllMedalStats = MutableLiveData<List<Stats?>>()
    val isAllMedalStatsRequestInProgress = MutableLiveData(false)

    fun getGeneralMedalStatsByAthleteId(id: Int, grade: String? = null) {
        viewModelScope.launch {
            try {
                val result = mAthletesService.getMedalStatsByAthleteId(id, grade)
                val allMedals = result.filter { it.mt_grade_abbr == "ALL" }.takeIf { it.isNotEmpty() }
                    ?.get(0)
                val gold = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 1 }?.res_count ?: 0
                val silver = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 2 }?.res_count ?: 0
                val bronze = allMedals?.stats?.firstOrNull { stat -> stat.res_place == 3 }?.res_count ?: 0

                mGeneralMedalStats.value = GeneralMedalStats(gold, silver, bronze)

            } catch(e: Exception) {}
        }
    }

    fun getAllMedalStats(id: Int) {
        isAllMedalStatsRequestInProgress.value = true
        viewModelScope.launch {
            try {
                val allMedalStats = mAthletesService.getMedalStatsByAthleteId(id)
                val result = ArrayList<Stats?>()

                allMedalStats.forEachIndexed { index, medalStat ->
                    if(medalStat.stats.isNullOrEmpty())
                        result.add(null)
                    else {
                        if(index != 0) {
                            val gold = medalStat.stats.firstOrNull { stat -> stat.res_place == 1 }?.res_count ?: 0
                            val silver = medalStat.stats.firstOrNull { stat -> stat.res_place == 2 }?.res_count ?: 0
                            val bronze = medalStat.stats.firstOrNull { stat -> stat.res_place == 3 }?.res_count ?: 0
                            result.add(Stats(gold, silver, bronze, null, medalStat.mt_grade))
                        }

                    }
                }

                mAllMedalStats.value = result
                isAllMedalStatsRequestInProgress.value = false
            } catch(e: Exception) {
                e.printStackTrace()
            }
            finally {

            }
        }
    }
}
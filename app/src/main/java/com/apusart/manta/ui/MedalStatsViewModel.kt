package com.apusart.manta.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.ui.tools.Const
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
                if(result != null) {
                    val gold = result?.stats?.firstOrNull { stat -> stat.res_place == 1 }?.res_count ?: 0
                    val silver = result?.stats?.firstOrNull { stat -> stat.res_place == 2 }?.res_count ?: 0
                    val bronze = result?.stats?.firstOrNull { stat -> stat.res_place == 3 }?.res_count ?: 0

                    mGeneralMedalStats.value = GeneralMedalStats(gold, silver, bronze)
                }

            } catch(e: Exception) {}
        }
    }

    fun getAllMedalStats(id: Int) {
        isAllMedalStatsRequestInProgress.value = true
        viewModelScope.launch {
            val allMedalStats = ArrayList<MedalStat?>()
            for(i in 0..11) {
                val x = mAthletesService.getMedalStatsByAthleteId(id, Const.gradesAbbr.getString(i.toString()))
                allMedalStats.add(x)
            }
            try {


                val result = ArrayList<Stats?>()

                allMedalStats.forEach {
                    if(it?.stats.isNullOrEmpty())
                        result.add(null)
                    else {
                        val gold = it?.stats?.firstOrNull { stat -> stat.res_place == 1 }?.res_count ?: 0
                        val silver = it?.stats?.firstOrNull { stat -> stat.res_place == 2 }?.res_count ?: 0
                        val bronze = it?.stats?.firstOrNull { stat -> stat.res_place == 3 }?.res_count ?: 0
                        result.add(Stats(gold, silver, bronze, null, it?.mt_grade ))
                    }
                }

                mAllMedalStats.value = result

            } catch(e: Exception) {
                e.printStackTrace()
            }
            finally {
                isAllMedalStatsRequestInProgress.value = false
            }
        }
    }
}
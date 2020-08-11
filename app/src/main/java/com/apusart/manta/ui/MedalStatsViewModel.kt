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
class Stats(val gold: Int? = 0, val silver: Int? = 0, val bronze: Int? = 0, val otherplaces: List<MedalStat>)

class MedalStatsViewModel: ViewModel() {
    private val mAthletesService = AthletesService()
    val mGeneralMedalStats = MutableLiveData(GeneralMedalStats())
    val mAllMedalStats = MutableLiveData<List<Stats?>>()
    val isAllMedalStatsRequestInProgress = MutableLiveData(false)

    fun getGeneralMedalStatsByAthleteId(id: Int, grade: String? = null) {
        viewModelScope.launch {
            try {
                val result = mAthletesService.getMedalStatsByAthleteId(id, grade)
                if(result.isNotEmpty()) {
                    val gold = result.firstOrNull { medalStat -> medalStat.res_place == 1 }
                    val silver = result.firstOrNull { medalStat -> medalStat.res_place == 2 }
                    val bronze = result.firstOrNull { medalStat -> medalStat.res_place == 3 }

                    mGeneralMedalStats.value = GeneralMedalStats(gold?.res_count ?: 0, silver?.res_count ?: 0, bronze?.res_count ?: 0)
                }

            } catch(e: Exception) {}
        }
    }

    fun getAllMedalStats(id: Int) {
        isAllMedalStatsRequestInProgress.value = true
        viewModelScope.launch {
            try {
                val allMedalStats = ArrayList<List<MedalStat>>()
                for(i in 0..11) {
                    allMedalStats.add(mAthletesService.getMedalStatsByAthleteId(id, Const.gradesAbbr.getString(i.toString())))
                }

                val result = ArrayList<Stats?>()

                allMedalStats.forEach {
                    if(it.isEmpty())
                        result.add(null)
                    else {
                        val gold = it.firstOrNull { medalStat -> medalStat.res_place == 1 }
                        val silver = it.firstOrNull { medalStat -> medalStat.res_place == 2 }
                        val bronze = it.firstOrNull { medalStat -> medalStat.res_place == 3 }
                        val others = it.filter { medalStat -> (medalStat.res_place != 1) and (medalStat.res_place != 2) and (medalStat.res_place != 3) }
                        result.add(Stats(gold?.res_count ?: 0, silver?.res_count ?: 0, bronze?.res_count ?: 0, others))
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
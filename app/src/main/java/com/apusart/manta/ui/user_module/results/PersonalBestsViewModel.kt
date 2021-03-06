package com.apusart.manta.ui.user_module.results

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class PersonalBestByCompetition(val pb25: PersonalBest? = null, val pb50: PersonalBest? = null, val concurence: String? = null)

class PersonalBestsViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val pb25 = MutableLiveData<List<PersonalBest>>()
    val pb50 = MutableLiveData<List<PersonalBest>>()
    val pb1 = MutableLiveData<List<PersonalBestByCompetition>>()
    val pb = MutableLiveData<List<PersonalBest>>()
    val isThereAnyPersonalBest = MutableLiveData<Boolean>()

    fun getPersonalBestsByAthleteId(id: Int, limit: Int? = Const.defaultLimit) {
        viewModelScope.launch {
            try {
                val result = athletesService.getPersonalBestsByAthleteId(id, limit) as MutableList<PersonalBest>
                val personal_best_25 = result.filter { pb -> pb.res_course_abbr == "SCM" }
                val personal_best_50 = result.filter { pb -> pb.res_course_abbr == "LCM" }
                val res = ArrayList<PersonalBestByCompetition>()
                for(i in personal_best_25) {
                    res.add(PersonalBestByCompetition(i, personal_best_50.firstOrNull { (i.sst_name_pl == it.sst_name_pl) and (i.sev_distance == it.sev_distance) }, "${i.sev_distance}m ${i.sst_name_pl}"))
                }

                for(i in personal_best_50) {
                    val hasPair = personal_best_25.firstOrNull { (i.sst_name_pl == it.sst_name_pl) and (i.sev_distance == it.sev_distance) } != null
                    if((!hasPair))
                        res.add(PersonalBestByCompetition(null, i, "${i.sev_distance}m ${i.sst_name_pl}"))
                }

                pb1.value = res
                pb25.value = result.filter { pb -> pb.res_course_abbr == "SCM" }
                pb50.value = result.filter { pb -> pb.res_course_abbr == "LCM" }
                isThereAnyPersonalBest.value = pb25.value?.isNotEmpty() ?: false || pb50.value?.isNotEmpty() ?: false
            } catch(e: Exception) {}
        }
    }

    fun getPersonalBest(id: Int, limit: Int? = 5,  ss_abbr: String? = null, distance: Int? = null, course: String? = null) {
        viewModelScope.launch {
            val result = athletesService.getPersonalBestsByAthleteId(id, Const.defaultLimit) as MutableList<PersonalBest>
            pb.value = result
                .filter { it.sst_name_pl == Const.stylesRev.getString(ss_abbr) }
                .filter { if(distance == null) true else it.sev_distance == distance }
                .filter {  if(course == null) true else it.res_course_abbr == course }

        }
    }
}
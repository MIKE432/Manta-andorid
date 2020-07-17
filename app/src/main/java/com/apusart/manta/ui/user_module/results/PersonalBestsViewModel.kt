package com.apusart.manta.ui.user_module.results

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class PersonalBestByCompetition(val pb25: PersonalBest? = null, val pb50: PersonalBest? = null)

class PersonalBestsViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val pb25 = MutableLiveData<List<PersonalBest>>()
    val pb50 = MutableLiveData<List<PersonalBest>>()
    val pb = MutableLiveData<List<PersonalBestByCompetition>>()
    fun getPersonalBestsByAthleteId(id: Int, limit: Int? = Const.defaultLimit) {
        viewModelScope.launch {
            val result = athletesService.getPersonalBestsByAthleteId(id, limit) as MutableList<PersonalBest>
            try {
                val personal_best_25 = result.filter { pb -> pb.res_course_abbr == "SCM" }
                val personal_best_50 = result.filter { pb -> pb.res_course_abbr == "LCM" }
                val res = ArrayList<PersonalBestByCompetition>()
                for(i in personal_best_25) {
                    res.add(PersonalBestByCompetition(i, personal_best_50.firstOrNull { (i.sst_name_pl == it.sst_name_pl) and (i.sev_distance == it.sev_distance) }))
                }

                for(i in personal_best_50) {
                    val hasPair = personal_best_25.firstOrNull { (i.sst_name_pl == it.sst_name_pl) and (i.sev_distance == it.sev_distance) } != null

                    res.add(PersonalBestByCompetition(null, if(hasPair) null else i))
                }

                pb.value = res.filter { (it.pb25 != null) or (it.pb50 != null) }
                pb25.value = result.filter { pb -> pb.res_course_abbr == "SCM" }
                pb50.value = result.filter { pb -> pb.res_course_abbr == "LCM" }
            } catch(e: Exception) {}
        }
    }
}
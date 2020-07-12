package com.apusart.manta.ui.user_module.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.Const
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class PersonalBestsViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val pb25 = MutableLiveData<List<PersonalBest>>()
    val pb50 = MutableLiveData<List<PersonalBest>>()

    fun getPersonalBestsByAthleteId(id: Int, limit: Int? = Const.defaultLimit) {
        viewModelScope.launch {
            val result = athletesService.getPersonalBestsByAthleteId(id, limit) as MutableList<PersonalBest>
            try {

                pb25.value = result.filter { pb -> pb.res_course_abbr == "SCM" }
                pb50.value = result.filter { pb -> pb.res_course_abbr == "LCM" }
            } catch(e: Exception) {}
        }
    }
}
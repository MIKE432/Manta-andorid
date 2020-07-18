package com.apusart.manta.ui.user_module.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Result
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class ResultsViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val results = MutableLiveData<List<Result>>()

    fun getResultsByAthleteId(id: Int, limit: Int? = 10) {
        viewModelScope.launch {
            try {
                results.value = athletesService.getResultsByAthleteId(id, limit).filter { it.res_total_time != null }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
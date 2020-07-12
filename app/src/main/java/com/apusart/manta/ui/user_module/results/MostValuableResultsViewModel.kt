package com.apusart.manta.ui.user_module.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.MostValuableResult
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class MostValuableResultsViewModel: ViewModel() {
    private val athletesService = AthletesService()
    val mostValuableResults = MutableLiveData<List<MostValuableResult>>()

    fun getMostValuableResultsByAthleteId(id: Int) {
        viewModelScope.launch {

            try {
                mostValuableResults.value = athletesService.getMostValuableResultsByAthleteId(id)
            } catch(e: Exception) {}
        }
    }
}
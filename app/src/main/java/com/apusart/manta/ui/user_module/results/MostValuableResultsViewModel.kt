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
    val isInProgress = MutableLiveData(false)

    fun getMostValuableResultsByAthleteId(id: Int) {
        viewModelScope.launch {
            isInProgress.value = true
            try {
                mostValuableResults.value = athletesService.getMostValuableResultsByAthleteId(id)
                isInProgress.value = false
            } catch(e: Exception) {

            } finally {
                isInProgress.value = false
            }
        }
    }
}
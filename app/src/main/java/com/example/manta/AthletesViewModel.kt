package com.example.manta

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manta.api.models.Athlete
import com.example.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class AthletesViewModel: ViewModel() {
    val athletes = MutableLiveData<List<Athlete>>()
    private val athletesService = AthletesService()
    val inProgress = MutableLiveData(false)

    fun getAthletes() {
        inProgress.value = true
        viewModelScope.launch {
            try {
                athletes.value =  athletesService.getAthletes()
                inProgress.value = false
            } catch(e: Exception) {}
        }
    }
}
package com.example.manta

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.manta.api.calls.CALL
import com.example.manta.api.models.Athlete

class AthletesViewModel: ViewModel() {
    val athletes = MutableLiveData<List<Athlete>>()


    fun getAthletes() {
        CALL.GET.athletes { athletesResponse ->
            athletes.postValue(athletesResponse?.athletes ?: listOf())
        }

    }
}
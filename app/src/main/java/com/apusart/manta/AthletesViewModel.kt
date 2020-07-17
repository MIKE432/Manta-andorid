package com.apusart.manta

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.api.serivces.AthletesService
import kotlinx.coroutines.launch
import java.lang.Exception

class AthletesViewModel: ViewModel() {
    val athletes = MutableLiveData<List<Athlete>>()
    private val athletesService = AthletesService()
    val inProgress = MutableLiveData(false)
    val mantaAlph = MutableLiveData<List<String>>()
    fun getAthletes() {
        inProgress.value = true
        viewModelScope.launch {
            try {
                val res = athletesService.getAthletes()
                athletes.value =  res
                mantaAlph.value = res.fold(ArrayList()) { total, item ->
                    var hasItem = false

                    total.forEach {
                        if(it == item.ath_lastname.substring(0, 1))
                            hasItem = true
                    }
                    if(!hasItem)
                        total.add(item.ath_lastname.substring(0, 1))

                    return@fold total
                }
                inProgress.value = false
            } catch(e: Exception) {}
        }
    }
}
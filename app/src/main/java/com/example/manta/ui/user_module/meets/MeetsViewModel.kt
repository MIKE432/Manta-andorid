package com.example.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manta.Const
import com.example.manta.api.models.Meet
import com.example.manta.api.serivces.MeetService
import kotlinx.coroutines.launch
import java.lang.Exception

class MeetsViewModel: ViewModel() {
    private val meetService = MeetService()
    val lastMeets = MutableLiveData<List<Meet>>()
    val inProgressLastMeets = MutableLiveData(false)

    fun getLastMeetsByAthleteId(id: Int) {
        inProgressLastMeets.value = true
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    lastMeets.value =  meetService.getLastMeetsByAthleteId(id, Const.defaultLastMeetsLimit)
                    inProgressLastMeets.value = false
                } catch(e: Exception) {}
            }
        }
    }
}
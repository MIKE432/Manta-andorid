package com.apusart.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.Const
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.serivces.MeetService
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
                    lastMeets.value =  meetService.getLastMeetsByAthleteId(id, Const.defaultLimit)
                    inProgressLastMeets.value = false
                } catch(e: Exception) {}
            }
        }
    }
}
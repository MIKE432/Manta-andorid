package com.apusart.manta.ui.user_module.meets

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.serivces.MeetService
import kotlinx.coroutines.launch
import java.lang.Exception

class MeetsViewModel: ViewModel() {
    private val meetService = MeetService()

    val lastMeets = MutableLiveData<List<Meet>>()
    val inProgressLastMeets = MutableLiveData<Boolean>()

    val incomingMeets = MutableLiveData<List<Meet>>()
    val inProgressIncomingMeets = MutableLiveData(false)


    fun getLastMeetsByAthleteId(id: Int) {
        inProgressLastMeets.value = true
        viewModelScope.launch {

            try {
                lastMeets.value =  meetService.getMeetsByAthleteId(id, Const.defaultLimit)
                inProgressLastMeets.value = false
            } catch(e: Exception) {e.printStackTrace()}
        }
    }

    fun getIncomingMeetsByAthleteId(id: Int, limit: Int? = 3) {
        inProgressIncomingMeets.value = true
        viewModelScope.launch {
            try {
                incomingMeets.value = meetService.getIncomingMeetsByAthleteId(id, limit)
            } catch(e: Exception) {}
            finally { inProgressIncomingMeets.value = false }
        }

    }
}
package com.apusart.manta.ui.user_module.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Meet
import com.apusart.manta.api.models.Photo
import com.apusart.manta.api.serivces.MeetService
import kotlinx.coroutines.launch
import java.lang.Exception

class GalleryViewModel: ViewModel() {
    private val meetService = MeetService()
    val photos = MutableLiveData<List<Photo>>()
    val meet = MutableLiveData<Meet>()

    fun getMeetByMeetId(meetId: Int) {
        viewModelScope.launch {
            try {
                meet.value = meetService.getMeetById(meetId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPhotosByMeetId(id: Int) {
        viewModelScope.launch {
            try {
                photos.value = meetService.getPhotosByMeetId(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
package com.example.baseprojectkotlin_h_c.ui.base.livephoto

import android.app.Application
import androidx.lifecycle.*
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoResult
import com.example.baseprojectkotlin_h_c.data.repository.PhotoRepository

abstract class BasePhotoViewModel(app: Application) : ViewModel() {
    protected val repository = PhotoRepository(app)
    val photoLisLiveData = MutableLiveData<DataResponse<LivePhotoResult>>()

    init {
        photoLisLiveData.value = DataResponse.DataIdle()
    }

    val isLoading: LiveData<Boolean> = Transformations.map(photoLisLiveData) {
        photoLisLiveData.value!!.loadingStatus == LoadingStatus.Loading
    }
    val isLoadingMore: LiveData<Boolean> = Transformations.map(photoLisLiveData) {
        photoLisLiveData.value!!.loadingStatus == LoadingStatus.LoadingMore
    }
    abstract val isEmpty: LiveData<Boolean>
    abstract fun retry()

}
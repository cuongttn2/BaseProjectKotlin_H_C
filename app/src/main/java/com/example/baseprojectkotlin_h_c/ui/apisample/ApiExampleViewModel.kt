package com.example.baseprojectkotlin_h_c.ui.apisample

import android.app.Application
import androidx.lifecycle.*
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoResult
import com.example.baseprojectkotlin_h_c.ui.base.livephoto.BasePhotoViewModel
import kotlinx.coroutines.launch

class ApiExampleViewModel(app: Application) : BasePhotoViewModel(app) {
    override val isEmpty: LiveData<Boolean> = Transformations.map(photoLisLiveData) {
        photoLisLiveData.value!!.loadingStatus == LoadingStatus.Error
    }

    fun fetchPhotoList(isRefresh: Boolean) {
        if (photoLisLiveData.value!!.loadingStatus != LoadingStatus.Loading && photoLisLiveData.value!!.loadingStatus != LoadingStatus.Refresh) {
            photoLisLiveData.value = DataResponse.DataLoading(
                if (isRefresh) {
                    LoadingStatus.Refresh
                } else {
                    LoadingStatus.Loading
                }
            )
            viewModelScope.launch {
                val photos = repository.fetchPhotoList()
                if (photos.isNullOrEmpty()) {
                    photoLisLiveData.value = DataResponse.DataError()
                } else {
                    photoLisLiveData.value = DataResponse.DataSuccess(LivePhotoResult(1, photos))
                }
            }
        }
    }

    override fun retry() {
        fetchPhotoList(false)
    }

    class Factory(private val app: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApiExampleViewModel::class.java)) {
                return ApiExampleViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
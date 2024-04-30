package com.example.baseprojectkotlin_h_c.ui.apipagingexample

import android.app.Application
import androidx.lifecycle.*
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoResult
import com.example.baseprojectkotlin_h_c.ui.base.livephoto.BasePhotoViewModel
import com.example.baseprojectkotlin_h_c.utils.Constants
import kotlinx.coroutines.launch

class PagingExampleViewModel(app: Application) : BasePhotoViewModel(app) {
    var curPage = 1
    override val isEmpty: LiveData<Boolean> = Transformations.map(photoLisLiveData) {
        photoLisLiveData.value!!.loadingStatus == LoadingStatus.Error && curPage == 1
    }
    fun fetchData(isLoadMore: Boolean) {
        if (photoLisLiveData.value!!.loadingStatus != LoadingStatus.Loading
            && photoLisLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
            && photoLisLiveData.value!!.loadingStatus != LoadingStatus.Refresh
        ) {
            if (!isLoadMore) {
                if (curPage > 1) {
                    photoLisLiveData.value = DataResponse.DataLoading(LoadingStatus.Refresh)
                } else {
                    photoLisLiveData.value = DataResponse.DataLoading(LoadingStatus.Loading)
                }
                curPage = 1
            } else {
                photoLisLiveData.value = DataResponse.DataLoading(LoadingStatus.LoadingMore)
            }
            viewModelScope.launch {
                val photos = repository.fetchPhotoByPage(
                    Constants.ITEM_PER_PAGE, curPage
                )
                if (photos != null && photos.isNotEmpty()) {
                    photoLisLiveData.value = DataResponse.DataSuccess(LivePhotoResult(curPage,photos))
                    curPage++
                } else {
                    photoLisLiveData.value = DataResponse.DataError()
                }
            }
        }
    }
    override fun retry() {
        fetchData(false)
    }
    class Factory(private val app: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PagingExampleViewModel::class.java)) {
                return PagingExampleViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
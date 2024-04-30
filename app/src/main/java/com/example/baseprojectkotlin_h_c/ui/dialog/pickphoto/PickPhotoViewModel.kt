package com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto

import androidx.lifecycle.*
import com.example.baseprojectkotlin_h_c.App
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import com.example.baseprojectkotlin_h_c.data.repository.MediaStoreRepository
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.PickModelDataType
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.PickPhotoModel
import kotlinx.coroutines.launch

class PickPhotoViewModel(val app: App) : ViewModel() {
    private val repository = MediaStoreRepository(app)
    private val allImages = mutableListOf<PhotoModel>()
    val mPickPhotoModel = MutableLiveData<DataResponse<PickPhotoModel>>()
    val isExitDialog = MutableLiveData(false)

    init {
        mPickPhotoModel.value = DataResponse.DataIdle()
    }

    val isEmpty: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Error
    }

    val isLoading: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Loading
    }

    val isDetailMode: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        if (mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Success) {
            val pickPhotoModel = (mPickPhotoModel.value as DataResponse.DataSuccess).body
            pickPhotoModel.pickModelDataType == PickModelDataType.LoadDetailAlbum
        } else {
            false
        }
    }

    val title: LiveData<String> = Transformations.map(mPickPhotoModel) {
        if (mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Success) {
            val pickPhotoModel = (mPickPhotoModel.value as DataResponse.DataSuccess).body
            if (pickPhotoModel.pickModelDataType == PickModelDataType.LoadDetailAlbum) {
                pickPhotoModel.photoList[0].albumName
            } else {
                app.getString(R.string.title_photos)
            }

        } else {
            app.getString(R.string.title_photos)
        }
    }

    fun onClose() {
        if (mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Success) {
            val pickPhotoModel = (mPickPhotoModel.value as DataResponse.DataSuccess).body
            if (pickPhotoModel.pickModelDataType == PickModelDataType.LoadDetailAlbum){
                loadingAll()
            }else{
                isExitDialog.value = true
            }
        } else {
            isExitDialog.value = true
        }
    }

    fun loadingAll() {
        if (mPickPhotoModel.value!!.loadingStatus != LoadingStatus.Loading) {
            mPickPhotoModel.value = DataResponse.DataLoading(LoadingStatus.Loading)
            viewModelScope.launch {
                if (allImages.isEmpty()) {
                    val images = repository.getImages()
                    if (images.isNotEmpty()) {
                        allImages.addAll(images)
                    }
                }
                if (allImages.isNotEmpty()) {
                    val albums = repository.getAlbums(allImages)
                    if (albums.isNotEmpty()) {
                        mPickPhotoModel.value = DataResponse.DataSuccess(
                            PickPhotoModel(
                                PickModelDataType.LoadAlbum,
                                albums
                            )
                        )
                    } else {
                        mPickPhotoModel.value = DataResponse.DataError()
                    }
                } else {
                    mPickPhotoModel.value = DataResponse.DataError()
                }
            }
        }
    }

    fun loadAlbumDetail(albumId: Long) {
        if (mPickPhotoModel.value!!.loadingStatus != LoadingStatus.Loading) {
            mPickPhotoModel.value = DataResponse.DataLoading(LoadingStatus.Loading)
            viewModelScope.launch {
                val albumDetailList = repository.getAlbumDetail(allImages, albumId)
                if (albumDetailList.isNotEmpty()) {
                    mPickPhotoModel.value = DataResponse.DataSuccess(
                        PickPhotoModel(
                            PickModelDataType.LoadDetailAlbum,
                            albumDetailList
                        )
                    )
                } else {
                    mPickPhotoModel.value = DataResponse.DataError()
                }
            }
        }
    }


    class Factory(private val app: App) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PickPhotoViewModel::class.java)) {
                return PickPhotoViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
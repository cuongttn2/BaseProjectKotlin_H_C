package com.example.baseprojectkotlin_h_c.data.repository

import android.content.Context
import com.example.baseprojectkotlin_h_c.data.api.retrofit.photoapi.PhotoListApiHelper
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoRepository(val context: Context) {
    private var photoListApiHelper = PhotoListApiHelper.getInstance(context)
    suspend fun fetchPhotoList(): List<LivePhotoModel>? = withContext(Dispatchers.Default) {
        try {
            photoListApiHelper.photoListApi.fetchPhotoList()
        }catch (ex: Exception){
            null
        }
    }

    suspend fun fetchPhotoByPage(itemPerPage: Int, page: Int): List<LivePhotoModel>? = withContext(Dispatchers.Default) {
        try {
            photoListApiHelper.photoListApi.fetchPaging(itemPerPage,page)
        }catch (ex: Exception){
            null
        }
    }
}
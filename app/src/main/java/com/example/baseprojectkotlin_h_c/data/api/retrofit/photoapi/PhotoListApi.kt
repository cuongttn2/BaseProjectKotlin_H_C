package com.example.baseprojectkotlin_h_c.data.api.retrofit.photoapi


import com.example.baseprojectkotlin_h_c.data.model.LivePhotoModel
import retrofit2.http.*

interface PhotoListApi {
    @GET("/v2/list")
    suspend fun fetchPhotoList(): List<LivePhotoModel>?

    @GET("/v2/list")
    suspend fun fetchPaging(
        @Query("limit") itemPerPage: Int,
        @Query("page") page: Int,
    ): List<LivePhotoModel>?
}
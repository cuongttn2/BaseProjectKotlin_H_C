package com.example.baseprojectkotlin_h_c.data.api.retrofit.photoapi

import android.content.Context
import com.example.baseprojectkotlin_h_c.data.api.base.BaseRetrofitHelper
import com.example.baseprojectkotlin_h_c.utils.SingletonHolder
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PhotoListApiHelper private constructor(context: Context) : BaseRetrofitHelper(context) {
    private val BASE_URL = "https://picsum.photos"
    var photoListApi: PhotoListApi

    init {
        GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(
                CoroutineCallAdapterFactory()
            ).client(okHttpClient!!).build()
        photoListApi = retrofit.create(PhotoListApi::class.java)
    }

    companion object : SingletonHolder<PhotoListApiHelper, Context>(::PhotoListApiHelper)

}
package com.example.baseprojectkotlin_h_c.data.api.base

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

open class BaseRetrofitHelper(context: Context) {
    protected var okHttpClient: OkHttpClient? = null
   init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder().writeTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS).readTimeout(6 * 1000.toLong(), TimeUnit.MILLISECONDS).addInterceptor(interceptor)
        okHttpClient = builder.build()
    }
}
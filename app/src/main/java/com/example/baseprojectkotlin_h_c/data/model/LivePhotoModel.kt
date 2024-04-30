package com.example.baseprojectkotlin_h_c.data.model

import com.google.gson.annotations.SerializedName

data class LivePhotoModel(
    val id: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    @SerializedName("download_url")
    val download_url: String,
)

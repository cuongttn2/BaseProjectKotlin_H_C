package com.example.baseprojectkotlin_h_c.data.model

import android.net.Uri
import java.io.File

data class PhotoModel(
    val uri: Uri,
    val file: File,
    val albumId: Long,
    val albumName: String
) {
    var childCount: Int = 0
}

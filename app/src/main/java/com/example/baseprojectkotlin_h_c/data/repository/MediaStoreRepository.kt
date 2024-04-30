package com.example.baseprojectkotlin_h_c.data.repository

import com.example.baseprojectkotlin_h_c.App
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import com.example.baseprojectkotlin_h_c.utils.loader.MediaLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaStoreRepository(val app: App) {

    suspend fun getImages(): List<PhotoModel> = withContext(Dispatchers.Default) {
        MediaLoader.loadAllImages(app)
    }

    suspend fun getAlbums(photoModels: List<PhotoModel>): MutableList<PhotoModel> =
        withContext(Dispatchers.Default) {
            val albums = mutableListOf<PhotoModel>()
            if (photoModels.isNotEmpty()) {
                val maps = photoModels.groupBy { it.albumId }
                if (maps.isNotEmpty()) {
                    maps.values.forEach {
                        it[0].childCount = it.size
                        albums.add(it[0])
                    }
                }
            }
            albums
        }

    suspend fun getAlbumDetail(
        photoModels: List<PhotoModel>,
        albumId: Long
    ): MutableList<PhotoModel> =
        withContext(Dispatchers.Default) {
            val albums = mutableListOf<PhotoModel>()
            if (photoModels.isNotEmpty()) {
                val maps = photoModels.filter { it.albumId == albumId }
                if (maps.isNotEmpty()) {
                    albums.addAll(maps)
                }
            }
            albums
        }

}
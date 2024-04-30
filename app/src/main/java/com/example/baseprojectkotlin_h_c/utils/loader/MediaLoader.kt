package com.example.baseprojectkotlin_h_c.utils.loader

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import java.io.File

object MediaLoader {
    fun loadAllImages(
        context: Context,
    ): MutableList<PhotoModel> {
        val uri = getUri()
        val selectionArgs: Array<String?> =
            arrayOf("image/png", "image/jpg", "image/jpeg", "image/bmp", "image/gif")
        val selection = getWhereClause(selectionArgs)
        return getListFromUri(context, uri, selection, selectionArgs, false)
    }

    private fun getWhereClause(args: Array<String?>): String {
        var where = "("
        for (i in args.indices) {
            where = if (i < args.size - 1) {
                where.plus(MediaStore.Files.FileColumns.MIME_TYPE + " =? OR ")
            } else {
                where.plus(MediaStore.Files.FileColumns.MIME_TYPE + " =? )")
            }

        }
        return where
    }

    private fun getUri(): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Files.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Files.getContentUri("external")
        }
    }


    private fun getListFromUri(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArg: Array<String?>?, orderAscending: Boolean,
    ): MutableList<PhotoModel> {
        val list = mutableListOf<PhotoModel>()
        val projection =
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID
            )

        createCursor(
            context.contentResolver,
            uri,
            projection,
            selection,
            selectionArg,
            orderAscending
        ).use { cursor ->
            cursor?.let {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val albumNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val pathColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(pathColumn)
                    val file = File(path)
                    val id = cursor.getLong(idColumn)
                    val albumId =
                        cursor.getLong(albumIdColumn)
                    val albumName =
                        cursor.getString(albumNameColumn)
                    if (!file.exists() or file.isHidden) continue
                    val photoModel = PhotoModel(
                        ContentUris.withAppendedId(
                            uri,
                            id
                        ), file, albumId, albumName
                    )
                    list.add(photoModel)
                }
                cursor.close()
            }

        }
        return list
    }

    private fun createCursor(
        contentResolver: ContentResolver,
        collection: Uri,
        projection: Array<String>,
        whereCondition: String?,
        selectionArgs: Array<String?>?, orderAscending: Boolean,
    ): Cursor? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            val selection =
                createSelectionBundle(whereCondition, selectionArgs, orderAscending)
            contentResolver.query(collection, projection, selection, null)
        }

        else -> {
            contentResolver.query(collection, projection, whereCondition, selectionArgs, null)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createSelectionBundle(
        whereCondition: String?,
        selectionArgs: Array<String?>?,
        orderAscending: Boolean,
    ): Bundle = Bundle().apply {
        putStringArray(
            ContentResolver.QUERY_ARG_SORT_COLUMNS,
            arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
        )
        val orderDirection =
            if (orderAscending) ContentResolver.QUERY_SORT_DIRECTION_ASCENDING else ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
        putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, orderDirection)
        putString(ContentResolver.QUERY_ARG_SQL_SELECTION, whereCondition)
        putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
    }


}
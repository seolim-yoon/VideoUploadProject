package com.example.videouploadproject.viewmodel

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.videouploadproject.model.VideoInfo
import com.example.videouploadproject.repository.data.database.AppDatabase
import com.example.videouploadproject.repository.data.entity.Video
import com.example.videouploadproject.repository.repo.VideoInfoRepository

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val context = application.applicationContext

    private val repository = VideoInfoRepository(application)
    private val videos = repository.getAll()

    var isDeleteMode = false

    fun getAll(): LiveData<List<Video>> {
        return videos
    }

    fun insert(video: Video) {
        repository.insert(video)
    }

    fun delete(video: Video) {
        repository.delete(video)
    }

    fun getVideo(): ArrayList<VideoInfo> {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_TAKEN
        )

        val uri: Uri = MediaStore.Video.Media.getContentUri("external")
        val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        val cursor: Cursor? = context?.contentResolver?.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )
        val videoInfo: ArrayList<VideoInfo> = ArrayList()

        cursor?.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                val title = cursor.getString(1)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Video.Media.getContentUri("external"),
                    id.toString()
                )

                videoInfo.add(VideoInfo(title, contentUri.toString(), false))
            }
            cursor.close()
        }

        return videoInfo
    }
}
package com.example.videouploadproject.repository.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.videouploadproject.repository.data.dao.VideoInfoDao
import com.example.videouploadproject.repository.data.database.AppDatabase
import com.example.videouploadproject.repository.data.entity.Video
import java.lang.Exception

class VideoInfoRepository(application: Application) {
    private val videoDatabase = AppDatabase.getInstance(application)!!
    private val videoDao: VideoInfoDao = videoDatabase.videoinfoDao()
    private val videos: LiveData<List<Video>> = videoDao.getAll()

    fun getAll(): LiveData<List<Video>> {
        return videos
    }

    fun insert(video: Video) {
        try {
            val thread = Thread(Runnable {
                videoDao.insert(video)
            })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(video: Video) {
        try {
            val thread = Thread(Runnable {
                videoDao.delete(video)
            })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
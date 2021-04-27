package com.example.videouploadproject.repository.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.videouploadproject.repository.data.entity.Video

@Dao
interface VideoInfoDao {
    @Query("SELECT * FROM video")
    fun getAll(): LiveData<List<Video>>

    @Insert
    fun insert(video: Video)

    @Delete
    fun delete(video: Video)

}
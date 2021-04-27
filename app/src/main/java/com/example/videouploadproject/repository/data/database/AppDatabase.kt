package com.example.videouploadproject.repository.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.videouploadproject.repository.data.dao.VideoInfoDao
import com.example.videouploadproject.repository.data.entity.Video

@Database(entities = [Video::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun videoinfoDao(): VideoInfoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}
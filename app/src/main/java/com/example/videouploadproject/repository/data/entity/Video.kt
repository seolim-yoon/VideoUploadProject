package com.example.videouploadproject.repository.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video")
data class Video(var title: String, var uri: String) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
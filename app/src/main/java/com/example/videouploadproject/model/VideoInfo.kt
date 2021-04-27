package com.example.videouploadproject.model

import java.io.Serializable

data class VideoInfo(
    var title: String,
    var uri: String,
    var isChecked: Boolean,
) : Serializable
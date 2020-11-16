package com.example.videouploadproject.item

import java.io.Serializable

data class VideoInfo(
    var title: String,
    var uri: String,
    var isChecked: Boolean,
    var isDeleteMode: Boolean
) : Serializable
package com.example.videouploadproject.view.adapter.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageViewBindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(ivThumnail: ImageView, url: String) {
        Glide.with(ivThumnail.context)
            .load(url)
            .centerCrop()
            .into(ivThumnail)
    }
}
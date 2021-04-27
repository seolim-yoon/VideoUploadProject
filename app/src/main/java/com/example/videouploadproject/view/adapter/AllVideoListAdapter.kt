package com.example.videouploadproject.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videouploadproject.databinding.HolderVideoListBinding
import com.example.videouploadproject.model.VideoInfo
import kotlin.collections.ArrayList

class AllVideoListAdapter(private val context: Context?,
                          var videoInfo: ArrayList<VideoInfo>,
                          val videoItemClick: (VideoInfo) -> Unit
): RecyclerView.Adapter<AllVideoListAdapter.VideoListHolder>() {
    inner class VideoListHolder(private val binding: HolderVideoListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(info: VideoInfo) {
            binding.videoinfo = info

            binding.ivThumnail.setOnClickListener {
                videoItemClick(info)
                binding.invalidateAll()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListHolder
            = VideoListHolder(HolderVideoListBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: VideoListHolder, position: Int) {
        holder.bind(videoInfo[position])
    }

    override fun getItemCount(): Int = videoInfo.size
}
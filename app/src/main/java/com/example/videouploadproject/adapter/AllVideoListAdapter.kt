package com.example.videouploadproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.videouploadproject.databinding.HolderVideoListBinding
import com.example.videouploadproject.item.VideoInfo
import kotlin.collections.ArrayList

class AllVideoListAdapter(private val context: Context?, var videoInfo: ArrayList<VideoInfo>): RecyclerView.Adapter<AllVideoListAdapter.VideoListHolder>() {
    var selectedList: ArrayList<VideoInfo> = ArrayList()

    inner class VideoListHolder(private val binding: HolderVideoListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(info: VideoInfo) {
            binding.videoinfo = info

            binding.ivThumnail.setOnClickListener {
                with(binding.ivSelectImage) {
                    info.takeIf { it.isChecked }?.let {
                        info.isChecked = false
                        visibility = View.GONE
                        selectedList.remove(info)
                    } ?: run {
                        selectedList.takeIf { 5 <= it.size }?.let {
                            Toast.makeText(itemView.context, "최대 5개까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        info.isChecked = true
                        visibility = View.VISIBLE
                        selectedList.add(info)
                    }
                }
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
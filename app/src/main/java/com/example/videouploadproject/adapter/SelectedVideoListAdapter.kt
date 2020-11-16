package com.example.videouploadproject.adapter

import android.content.Context
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videouploadproject.databinding.HolderVideoListBinding
import com.example.videouploadproject.item.VideoInfo
import java.util.ArrayList

class SelectedVideoListAdapter(private val context: Context, val itemLongClick: () -> Unit): RecyclerView.Adapter<SelectedVideoListAdapter.SelectedVideoListHolder>() {
    var videoInfo: ArrayList<VideoInfo> = ArrayList()

    inner class SelectedVideoListHolder(private val binding: HolderVideoListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: VideoInfo) {
            binding.videoinfo = info

            with(binding.ivDeleteImage) {
                visibility = if (info.isDeleteMode) {
                                View.VISIBLE
                             } else {
                                View.GONE
                             }

                setOnClickListener {
                    deleteVideoFile(info)
                }
            }

            itemView.setOnLongClickListener{
                itemLongClick()
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedVideoListHolder
            = SelectedVideoListHolder(HolderVideoListBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: SelectedVideoListHolder, position: Int) {
        holder.bind(videoInfo[position])
    }

    override fun getItemCount(): Int = videoInfo.size

    fun setDeleteMode() {
        videoInfo.forEach{ it.isDeleteMode = true }
        notifyDataSetChanged()

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE))
        } else{
            vibrator.vibrate(300)
        }
    }

    fun setNormalMode() {
        videoInfo.forEach{ it.isDeleteMode = false }
        notifyDataSetChanged()
    }

    fun deleteVideoFile(info: VideoInfo) {
        videoInfo.remove(info)
        notifyDataSetChanged()
    }
}
package com.example.videouploadproject.view.adapter

import android.content.Context
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videouploadproject.databinding.HolderVideoListBinding
import com.example.videouploadproject.databinding.HolderVideoMainListBinding
import com.example.videouploadproject.model.VideoInfo
import com.example.videouploadproject.repository.data.entity.Video
import java.util.ArrayList

class SelectedVideoListAdapter(private val context: Context,
                               val videoItemClick: (Video) -> Unit,
                               val videoItemLongClick: (Video) -> Unit
): RecyclerView.Adapter<SelectedVideoListAdapter.SelectedVideoListHolder>() {
    private var videoInfo: List<Video> = listOf()

    inner class SelectedVideoListHolder(private val binding: HolderVideoMainListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: Video) {
            binding.video = info

            with(binding.ivDeleteImage) {
                setOnClickListener {
                    videoItemClick(info)
                    binding.invalidateAll()
                }
            }

            binding.root.setOnLongClickListener{
                videoItemLongClick(info)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedVideoListHolder
            = SelectedVideoListHolder(HolderVideoMainListBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: SelectedVideoListHolder, position: Int) {
        holder.bind(videoInfo[position])
    }

    override fun getItemCount(): Int = videoInfo.size

    fun setDeleteMode() {
        notifyDataSetChanged()

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, DEFAULT_AMPLITUDE))
        } else{
            vibrator.vibrate(300)
        }
    }

    fun setNormalMode() {
        notifyDataSetChanged()
    }

    fun setVideoInfos(video: List<Video>) {
        this.videoInfo = video
        notifyDataSetChanged()
    }
}
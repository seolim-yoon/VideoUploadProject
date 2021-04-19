package com.example.videouploadproject.view.ui

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videouploadproject.R
import com.example.videouploadproject.view.adapter.AllVideoListAdapter
import com.example.videouploadproject.databinding.DlgVideoLoadBinding
import com.example.videouploadproject.model.VideoInfo

class VideoLoadDialog(
    private var x: Int,
    private var y: Int,
    val loadListener: (ArrayList<VideoInfo>) -> Unit
): DialogFragment() {
    private lateinit var binding: DlgVideoLoadBinding
    private lateinit var allListAdapter: AllVideoListAdapter
    private var selectedList: ArrayList<VideoInfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
       binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dlg_video_load,
            container,
            false
        )
        binding.dialog = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        dialog?.window?.setLayout(x, y)
        allListAdapter = AllVideoListAdapter(context, getVideo())
        binding.rvVideoAlbumList.layoutManager = GridLayoutManager(context, 2)
        binding.rvVideoAlbumList.adapter = allListAdapter

    }

    fun selectOKClick(view: View) {
        dismiss()
        selectedList = allListAdapter.selectedList
        loadListener(selectedList)
    }

    fun selectCancelClick(view: View) {
        dismiss()
    }

    private fun getVideo(): ArrayList<VideoInfo> {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_TAKEN
        )

        val uri: Uri = MediaStore.Video.Media.getContentUri("external")
        val sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC"
        val cursor: Cursor? = context?.contentResolver?.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )
        val videoInfo: ArrayList<VideoInfo> = ArrayList()

        cursor?.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                val title = cursor.getString(1)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Video.Media.getContentUri("external"),
                    id.toString()
                )

                videoInfo.add(VideoInfo(title, contentUri.toString(), false, false))
            }
            cursor.close()
        }

        return videoInfo
    }
}
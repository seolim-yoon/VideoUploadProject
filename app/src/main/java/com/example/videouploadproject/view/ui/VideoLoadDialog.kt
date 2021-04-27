package com.example.videouploadproject.view.ui

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videouploadproject.R
import com.example.videouploadproject.view.adapter.AllVideoListAdapter
import com.example.videouploadproject.databinding.DlgVideoLoadBinding
import com.example.videouploadproject.model.VideoInfo
import com.example.videouploadproject.viewmodel.MainViewModel

class VideoLoadDialog(
    context: Context,
    private var x: Int,
    private var y: Int,
    val loadListener: (ArrayList<VideoInfo>) -> Unit
): DialogFragment() {
    private lateinit var binding: DlgVideoLoadBinding
    private lateinit var allListAdapter: AllVideoListAdapter
    private var selectedList: ArrayList<VideoInfo> = ArrayList()
    private val mainViewModel: MainViewModel = MainViewModel(context?.applicationContext as Application)

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
        allListAdapter = AllVideoListAdapter(context, mainViewModel.getVideo()){
            info ->
            Log.v("seolim", "info.isChecked : " + info.isChecked)
            info.isChecked = !info.isChecked
            if(info.isChecked) selectedList.add(info) else selectedList.remove(info)

            selectedList.takeIf { 5 <= it.size }?.let {
                android.widget.Toast.makeText(context, "최대 5개까지 선택할 수 있습니다.", android.widget.Toast.LENGTH_SHORT).show()
                return@AllVideoListAdapter
            }
        }
        binding.rvVideoAlbumList.layoutManager = GridLayoutManager(context, 2)
        binding.rvVideoAlbumList.adapter = allListAdapter
    }

    fun selectOKClick(view: View) {
        dismiss()
        loadListener(selectedList)
    }

    fun selectCancelClick(view: View) {
        dismiss()
    }
}
package com.example.videouploadproject.view.ui

import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videouploadproject.R
import com.example.videouploadproject.view.adapter.SelectedVideoListAdapter
import com.example.videouploadproject.databinding.ActivityMainBinding
import com.example.videouploadproject.model.VideoInfo
import com.example.videouploadproject.repository.data.entity.Video
import com.example.videouploadproject.service.UploadService
import com.example.videouploadproject.viewmodel.MainViewModel
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast

const val INTENT_UPLOAD_LIST = "uploadlist"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var selectListAdapter: SelectedVideoListAdapter

    private val permissionList = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this@MainActivity
        checkPermission()
        initView()
    }

    private fun initView() {
        selectListAdapter = SelectedVideoListAdapter(applicationContext, { video ->
            mainViewModel.delete(video)
        }, {
            mainViewModel.isDeleteMode = true
            selectListAdapter.setDeleteMode()
        })

        binding.rvSelectVideoList.layoutManager = GridLayoutManager(baseContext, 1)
        binding.rvSelectVideoList.adapter = selectListAdapter

        mainViewModel.getAll().observe(this, Observer { video ->
            selectListAdapter.setVideoInfos(video)
        })
    }

    private fun checkPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        for(permission in permissionList) {
            val check = checkCallingOrSelfPermission(permission)
            if(check == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissionList, 0)
                break
            }
        }
    }

    fun saveClick(view: View) {

    }

    fun addVideoClick(view: View) {
        val size = Point()

        windowManager.defaultDisplay.getSize(size)
        supportFragmentManager.let {
            VideoLoadDialog(applicationContext, size.x, size.y) {
                Log.d("seolim", "size : " + it.size)
                it.forEach {
                    mainViewModel.insert(Video(it.title, it.uri))
                }
            }.show(it, "")
        }
    }

    override fun onBackPressed() {
        if(mainViewModel.isDeleteMode) {
            mainViewModel.isDeleteMode = false
            selectListAdapter.setNormalMode()
        } else{
            finish()
        }
    }
}
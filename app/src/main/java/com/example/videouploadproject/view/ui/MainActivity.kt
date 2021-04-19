package com.example.videouploadproject.view.ui

import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.videouploadproject.R
import com.example.videouploadproject.view.adapter.SelectedVideoListAdapter
import com.example.videouploadproject.databinding.ActivityMainBinding
import com.example.videouploadproject.view.ui.VideoLoadDialog
import com.example.videouploadproject.model.VideoInfo
import com.example.videouploadproject.service.UploadService
import org.jetbrains.anko.startService
import org.jetbrains.anko.toast

const val INTENT_UPLOAD_LIST = "uploadlist"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var selectListAdapter: SelectedVideoListAdapter
    private var selectVideoInfo: ArrayList<VideoInfo> = java.util.ArrayList()
    var isDeleteMode = false

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
        selectListAdapter = SelectedVideoListAdapter(applicationContext) {
            isDeleteMode = true
            selectListAdapter.setDeleteMode()
        }

        binding.rvSelectVideoList.layoutManager = GridLayoutManager(baseContext, 2)
        binding.rvSelectVideoList.adapter = selectListAdapter
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

    fun uploadClick(view: View) {
        if(selectVideoInfo.size == 0) {
            toast("업로드 할 파일이 없습니다")
        } else {
            toast("${selectVideoInfo.size}개의 파일을 업로드 합니다")
            startService<UploadService> (
                INTENT_UPLOAD_LIST to selectVideoInfo
            )
        }
    }

    fun addVideoClick(view: View) {
        val size = Point()

        windowManager.defaultDisplay.getSize(size)
        supportFragmentManager.let {
            VideoLoadDialog(size.x, size.y) {
                selectVideoInfo = it
                selectListAdapter.videoInfo = it
                selectListAdapter.notifyDataSetChanged()
            }.show(it, "")
        }
    }

    override fun onBackPressed() {
        if(isDeleteMode) {
            isDeleteMode = false
            selectListAdapter.setNormalMode()
        } else{
            finish()
        }
    }
}